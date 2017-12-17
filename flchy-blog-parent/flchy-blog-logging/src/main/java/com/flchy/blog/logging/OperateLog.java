package com.flchy.blog.logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flchy.blog.base.datasource.ConfigDataSource;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.common.service.ScheduledService;
import com.flchy.blog.utils.StringUtil;
import com.flchy.blog.utils.sql.JdbcUtil;
import com.flchy.blog.utils.sql.SqlUtils;



/**
 * 记录功能操作日志批量添加
 * @author nieqs
 *
 */

@Component("operateLog")
public class OperateLog  implements AbstractLog ,ScheduledService{
	
	public static OperateLog getInstance() {
		return SpringContextHolder.getBean(OperateLog.class);
	}
	
	private static Logger logger = LoggerFactory.getLogger(OperateLog.class);
	
	private ArrayBlockingQueue<Map<String,Object>> arrayBlockingQueue;
	private final static int DEFAULT_QUEUE_CAPACITY = 1024;
	private final static int BATCH_INSERT_COUNT = 128;
	private String sqlInsert ="INSERT INTO `bi_config`.`log_operate` (`log_id`, `session_id`, `res_id`, `opr_type`, `opr_obj`, `opr_cont`, `opr_user_id`, `main_account`, `opr_begin_time`, `opr_end_time`, `is_error`, `err_msg`, `server_ip`, `client_ip`, `user_agent`, `browser_type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private String sqlInsertWithoutValues;
	
	private final int[] PARAM_TYPES = new int[] {
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.TIMESTAMP,
				Types.TIMESTAMP,
				Types.INTEGER,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
	};
	
	@Autowired
	private ConfigDataSource configDataSource;
	
	public OperateLog() {
		this.arrayBlockingQueue = new ArrayBlockingQueue<Map<String,Object>>(DEFAULT_QUEUE_CAPACITY);
	}
	
	
	@Override
	public void init() {
		
	}

	@Override
	public void start(Map<String, Object> visitMap) {
		this.offer(visitMap);
	}
	
	/**
	 * 查询数据转换并offer
	 * @param rslist
	 */
	private void offer(Map<String, Object> rsMap) {
		// offer存入到队列
		// add to queue to wait for flushing
		if (!this.arrayBlockingQueue.offer(rsMap)) {
			// the queue is full, flush first
			flush();
			if (!this.arrayBlockingQueue.offer(rsMap)) {
				// fail again, maybe an error
				logger.error("failed to add message ods info to flushing queue.");
			}
		}
	}

	/**
	 * 批量flush数据入库
	 */
	private void flush(){
		if (this.arrayBlockingQueue.size() == 0) {
			return;
		}
		
		String batchSqlInsert = null;
		if (!StringUtil.isNullOrEmpty(this.sqlInsert)) {
			this.sqlInsertWithoutValues = this.sqlInsert;
			// 开启多values插入方式，准备手动构建多值插入的SQL
			int delimiter = this.sqlInsert.indexOf("VALUES ");
			if (delimiter == -1) {
				delimiter = this.sqlInsert.indexOf("values ");
			}
			if (delimiter != -1) {
				this.sqlInsertWithoutValues = this.sqlInsert.substring(0, delimiter);
			}
			batchSqlInsert = this.sqlInsertWithoutValues;
		}
//		logger.info("flushing {}...", this.getName());
		
		List<Map<String,Object>> messagesToFlush = new ArrayList<Map<String,Object>>();
		this.arrayBlockingQueue.drainTo(messagesToFlush);
		
		// 将entriesToFlush按shardingKey分组flush，防止某一组数据库down机时，隔离其影响（单组数据库down机只影响局部数据）
		Map<String, List<Map<String,Object>>> shardingFlushEntriesMap = new HashMap<String, List<Map<String,Object>>>();
		for (Map<String, Object> message : messagesToFlush) {
			String shardingKey = String.valueOf( message.get("browser_type"));
			List<Map<String, Object>> shardingFlushEntries = shardingFlushEntriesMap.get(shardingKey);
			if (shardingFlushEntries == null) {
				shardingFlushEntries = new java.util.ArrayList<Map<String, Object>>(128);
				shardingFlushEntriesMap.put(shardingKey, shardingFlushEntries);
			}
			shardingFlushEntries.add(message);
		}
				
		for(List<Map<String,Object>> shardingFlushEntries : shardingFlushEntriesMap.values()) {		
			Connection connection = null;
			boolean autoCommit0 = false;
			try {
				connection = configDataSource.clusterDataSource().getConnection();
				autoCommit0 = connection.getAutoCommit();
				Statement pstmt = connection.createStatement();
				int count = 0;
				StringBuilder sqlBuilder = new StringBuilder(batchSqlInsert);
				for(Map<String,Object> messageLog : shardingFlushEntries) {
					Object[] params = new Object[] {
							messageLog.get("log_id"),
							messageLog.get("session_id"),
							messageLog.get("res_id"),
							messageLog.get("opr_type"),
							messageLog.get("opr_obj"),
							messageLog.get("opr_cont"),
							messageLog.get("opr_user_id"),
							messageLog.get("main_account"),
							messageLog.get("opr_begin_time"),
							messageLog.get("opr_end_time"),
							messageLog.get("is_error"),
							messageLog.get("err_msg"),
							messageLog.get("server_ip"),
							messageLog.get("client_ip"),
							messageLog.get("user_agent"),
							messageLog.get("browser_type"),
					};
					
					SqlUtils.appendSqlValues(sqlBuilder, params, PARAM_TYPES);
					++count;
					if(count >= BATCH_INSERT_COUNT) {
						pstmt.executeUpdate(sqlBuilder.toString());
						if(!autoCommit0) connection.commit();
						count = 0;
						sqlBuilder = new StringBuilder(batchSqlInsert);
					}
				}
							
				if(count > 0) {
					pstmt.executeUpdate(sqlBuilder.toString());
					if(!autoCommit0) connection.commit();
				}
				pstmt.close();
			} catch (SQLException ex) {
				if (JdbcUtil.isHardError(ex)) {
					// 致命错误，可能数据库已经down掉或无法连接，取消flush，等待下次重试
					logger.error("fatal error while flushing " + this.getName() + ", message: " + ex.getMessage(), ex);
				} else {
					logger.error("SQL exception while flushing " + this.getName() + ": " + ex.getMessage(), ex);
					// 非致命错误（如字段值超过数据库定义等常规异常），尝试单条flush，尽量减少失败的影响
					try {
						if (!autoCommit0)
							connection.rollback();
						String singlSqlInsert = this.sqlInsert;
//						if(!StringUtil.isNullOrEmpty(singlSqlInsert)){
//							singlSqlInsert = singlSqlInsert.replace("ods_message_log_dm_yyyymm", "ods_message_log_dm_" + optime_hour_yesmonth);
//						}
//						
						// try again in non-batch mode
						PreparedStatement pstmt = connection.prepareStatement(singlSqlInsert);
						for (Map<String, Object> messageLog : shardingFlushEntries) {
							try {
								pstmt.setString(1,String.valueOf(messageLog.get("log_id")));
								pstmt.setString(2, String.valueOf(messageLog.get("session_id")));
								pstmt.setString(3, String.valueOf(messageLog.get("res_id")));
								pstmt.setString(4, String.valueOf(messageLog.get("opr_type")));
								pstmt.setString(5, String.valueOf(messageLog.get("opr_obj")));
								pstmt.setString(6, String.valueOf(messageLog.get("opr_cont")));
								pstmt.setString(7, String.valueOf(messageLog.get("opr_user_id")));
								pstmt.setString(8, String.valueOf(messageLog.get("main_account")));
								pstmt.setObject(9,(Date)messageLog.get("opr_begin_time"));
								pstmt.setObject(10,(Date)messageLog.get("opr_end_time"));
								pstmt.setObject(11, messageLog.get("is_error"));
								pstmt.setString(12, String.valueOf(messageLog.get("err_msg")));
								pstmt.setString(13, String.valueOf(messageLog.get("server_ip")));
								pstmt.setString(14, String.valueOf(messageLog.get("client_ip")));
								pstmt.setString(15, String.valueOf(messageLog.get("user_agent")));
								pstmt.setString(16, String.valueOf(messageLog.get("browser_type")));
								pstmt.executeUpdate();
							} catch (SQLException ex2) {
								logger.error("SQL exception while save ods user info : " + ex2.getMessage() + ", failed message: \n\t" + messageLog.toString(), ex2);
							}
						}
						if (!autoCommit0)
							connection.commit();
						pstmt.close();
					} catch (SQLException e) {
						logger.error("error while rollback " + this.getName() + ": " + e.getMessage(), e);
					}
				}
			} finally {
				try {
					if (null != connection) {
						connection.close();
					}
				} catch (SQLException e) {
					logger.error("error while connection close " + this.getName() + ": " + e.getMessage(), e);
				}
			}
		}
		
		logger.debug("{} flushed with {} items.", this.getName(), messagesToFlush.size());
	}
	
	public String getName() {
		return "记录功能操作日志批量添加";
	}
	
	@Override
	public void stop() {
		this.flush();
	}


	@Override
	public void schedule() {
		this.flush();
	}

}
