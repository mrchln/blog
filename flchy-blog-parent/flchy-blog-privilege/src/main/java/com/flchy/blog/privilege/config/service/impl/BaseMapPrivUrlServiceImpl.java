package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseMapPrivUrlDao;
import com.flchy.blog.privilege.config.entity.BaseMapPrivUrlEntity;
import com.flchy.blog.privilege.config.service.IBaseMapPrivUrlService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.utils.NewMapUtil;

@Service
public class BaseMapPrivUrlServiceImpl implements IBaseMapPrivUrlService {
	private static Logger logger = LoggerFactory.getLogger(BaseMapPrivUrlServiceImpl.class);
	@Autowired
	private IBaseMapPrivUrlDao baseMapPrivUrlDao;

	@Override
	public List<BaseMapPrivUrlEntity> selectPrivUrlMapByPrivVisitIds(String[] privVisitIdArray) {
		return baseMapPrivUrlDao.selectPrivUrlMapByPrivVisitIds(privVisitIdArray);
	}
	
	/**
	 * 查询已选
	 * @param roleId 角色ID
	 */
	@Override
	public List<BaseMapPrivUrlEntity> selectPrivUrlMapBySelected(String privVisitId) {
		String[] privVisitIds=new String[1];
		privVisitIds[0]=privVisitId;
		return baseMapPrivUrlDao.selectPrivUrlMapByPrivVisitIds(privVisitIds);
	}

	@Override
	public Object delete(String id) {
		String subfirst = id.substring(0, 1);
		String subLast = id.substring(id.length() - 1, id.length());
		if (",".equals(subfirst)) {
			id = id.substring(1, id.length());
		}
		if (",".equals(subLast)) {
			id = id.substring(0, id.length() - 1);
		}
		String[] split = id.split(",");
		Integer[] arrInteger = new Integer[split.length];
		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null)
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The id format is incorrect!").get()));
		}
		if (split.length < 1) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Please choose the option to delete!").get()));
		}
		boolean update= baseMapPrivUrlDao.delete(Arrays.asList(arrInteger));
		if (update) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, id);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	
	@Override
	public boolean insert(BaseMapPrivUrlEntity baseMapPrivUrlEntity) {
		return baseMapPrivUrlDao.insert(baseMapPrivUrlEntity);
	}
	
	/**
	 * 批量添加
	 */
	@Override
	public boolean insertCodeBatch(Integer[] urlId,String privVisitId,String userName){
		List<Integer> urlIds=Arrays.asList(urlId);
		List<BaseMapPrivUrlEntity> collect = urlIds.stream().map(l->{
			BaseMapPrivUrlEntity baseMapPrivUrlEntity=new BaseMapPrivUrlEntity();
			baseMapPrivUrlEntity.setCreateTime(new Date());
			baseMapPrivUrlEntity.setPrivVisitId(privVisitId);
			baseMapPrivUrlEntity.setUrlId(l);
			baseMapPrivUrlEntity.setStatus(StatusEnum.NORMAL.getValues());
			baseMapPrivUrlEntity.setCreateUser(userName);
			return baseMapPrivUrlEntity;
		}).collect(Collectors.toList());
		return baseMapPrivUrlDao.insertCodeBatch(collect);
	}
}
