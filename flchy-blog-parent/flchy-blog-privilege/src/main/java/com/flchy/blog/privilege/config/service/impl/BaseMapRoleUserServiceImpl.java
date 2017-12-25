package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleUserDao;
import com.flchy.blog.privilege.config.entity.BaseMapRoleUserEntity;
import com.flchy.blog.privilege.config.service.IBaseMapUserRoleService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.utils.NewMapUtil;

@Service
public class BaseMapRoleUserServiceImpl implements IBaseMapUserRoleService {
	private static Logger logger = LoggerFactory.getLogger(BaseMapRoleUserServiceImpl.class);
	@Autowired
	private IBaseMapRoleUserDao baseMapRoleUserDao;

	@Override
	public List<BaseMapRoleUserEntity> selectRolesByUserId(int userId) {
		return baseMapRoleUserDao.selectRolesByUserId(userId);
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
		boolean updateUser = baseMapRoleUserDao.delete(Arrays.asList(arrInteger));
		if (updateUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, id);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	
	@Override
	public boolean insert(BaseMapRoleUserEntity baseMapRoleUserEntity) {
		return baseMapRoleUserDao.insert(baseMapRoleUserEntity);
	}

	@Override
	public boolean insertCodeBatch(Integer[] userId,Integer roleId,String userName){
		List<Integer> userIds=Arrays.asList(userId);
		List<BaseMapRoleUserEntity> collect = userIds.stream().map(l->{
			BaseMapRoleUserEntity baseMapRoleUserEntity=new BaseMapRoleUserEntity();
			baseMapRoleUserEntity.setCreateTime(new Date());
			baseMapRoleUserEntity.setRoleId(roleId);
			baseMapRoleUserEntity.setUserId(l);
			baseMapRoleUserEntity.setStatus(StatusEnum.NORMAL.getValues());
			baseMapRoleUserEntity.setCreateUser(userName);
			return baseMapRoleUserEntity;
		}).collect(Collectors.toList());
		return baseMapRoleUserDao.insertCodeBatch(collect);
	}
	@Transactional
	@Override
	public boolean updateCodeBatch(Integer userId,Integer[] roleId,String userName){
		List<BaseMapRoleUserEntity> selectRolesByUserId = selectRolesByUserId(userId);
		List<Integer> collect = selectRolesByUserId.stream().map(s->s.getRoleId()).collect(Collectors.toList());
		List<Integer> roleIdLists=Arrays.asList(roleId);
			//需要删除数据库已有的角色
				List<Integer> deleteIds=selectRolesByUserId.stream().filter(l->{
					return !roleIdLists.contains(l.getRoleId());
				}).map(l->l.getId()).collect(Collectors.toList());
				
				if(deleteIds!=null && deleteIds.size()>0){
					boolean delete = baseMapRoleUserDao.delete(deleteIds);
				}
				//需要添加数据库的角色
				List<BaseMapRoleUserEntity> createList=roleIdLists.stream().filter(l->{
					return !collect.contains(l);
				}).map(l->{
					BaseMapRoleUserEntity baseMapRoleUserEntity	=new BaseMapRoleUserEntity();
					baseMapRoleUserEntity.setRoleId(l);
					baseMapRoleUserEntity.setUserId(userId);
					baseMapRoleUserEntity.setStatus(StatusEnum.NORMAL.getValues());
					baseMapRoleUserEntity.setCreateTime(new Date());
					baseMapRoleUserEntity.setCreateUser(userName);
					return baseMapRoleUserEntity;
				}).collect(Collectors.toList());
				if(createList!=null && createList.size()>0){
					boolean insertCodeBatch = baseMapRoleUserDao.insertCodeBatch(createList);
				}
		return true;
	}
}
