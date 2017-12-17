/**
 * 
 */
package com.flchy.blog.logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.flchy.blog.base.datasource.BaseDataSource;
import com.flchy.blog.base.datasource.ConfigDataSource;
import com.flchy.blog.base.holder.SpringContextHolder;
import com.flchy.blog.common.service.ScheduledService;
import com.flchy.blog.utils.StringUtil;
import com.flchy.blog.utils.sql.JdbcUtil;
import com.flchy.blog.utils.sql.SqlUtils;



/**
 * 登录日志批量添加
 * @author nieqs
 *
 */

@Component("loginLog")
public class LoginLog  implements AbstractLog ,ScheduledService{
	
	public static LoginLog getInstance() {
		return SpringContextHolder.getBean(LoginLog.class);
	}
	
	private static Logger logger = LoggerFactory.getLogger(LoginLog.class);
	
	private ArrayBlockingQueue<Map<String,Object>> arrayBlockingQueue;
	private final static int DEFAULT_QUEUE_CAPACITY = 1024;
	private final static int BATCH_INSERT_COUNT = 10;
	private String sqlInsert ="INSERT INTO `bi_config`.`log_login` (`log_id`, `session_id`, `opr_user_id`, `main_account`, `server_ip`, `client_ip`, `login_time`, `user_agent`, `browser_type`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private String sqlInsertWithoutValues;
	
	private final int[] PARAM_TYPES = new int[] {
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.VARCHAR,
				Types.TIMESTAMP,
				Types.VARCHAR,
				Types.VARCHAR,
				
	};
	
	@Autowired
	private ConfigDataSource configDataSource;
	
	public LoginLog() {
		this.arrayBlockingQueue = new ArrayBlockingQueue<Map<String,Object>>(DEFAULT_QUEUE_CAPACITY);
	}
	
	
	@Override
	public void init() {
		
	}

	@Override
	public void start(Map<String, Object> logMap) {
		this.offer(logMap);
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
							messageLog.get("opr_user_id"),
							messageLog.get("main_account"),
							messageLog.get("server_ip"),
							messageLog.get("client_ip"),
							messageLog.get("login_time"),
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
								pstmt.setString(3, String.valueOf(messageLog.get("opr_user_id")));
								pstmt.setString(4, String.valueOf(messageLog.get("main_account")));
								pstmt.setString(5, String.valueOf(messageLog.get("server_ip")));
								pstmt.setString(6, String.valueOf(messageLog.get("client_ip")));
								pstmt.setObject(7,messageLog.get("login_time"));
								pstmt.setString(8, String.valueOf(messageLog.get("user_agent")));
								pstmt.setString(9, String.valueOf(messageLog.get("browser_type")));
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
		return "登录日志批量添加";
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
