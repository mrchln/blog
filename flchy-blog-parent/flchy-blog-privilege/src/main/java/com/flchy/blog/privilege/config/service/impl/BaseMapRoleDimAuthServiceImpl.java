package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleDimAuthDao;
import com.flchy.blog.privilege.config.entity.BaseMapRoleDimAuthEntity;
import com.flchy.blog.privilege.config.service.IBaseMapRoleDimAuthService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.utils.NewMapUtil;

@Service
public class BaseMapRoleDimAuthServiceImpl implements IBaseMapRoleDimAuthService {
	@Autowired
	private  IBaseMapRoleDimAuthDao baseMapRoleDimAuthDao;
	/**
	 * 查询已选
	 * @param roleId 角色ID
	 */
	@Override
	public List<BaseMapRoleDimAuthEntity> selectRoleDimAuthBySelected(Integer roleId) {
		Integer[] roleIdArray=new Integer[1];
		roleIdArray[0]=roleId;
		return baseMapRoleDimAuthDao.selectRoleDimAuthByRoleIds(roleIdArray);
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
		boolean update= baseMapRoleDimAuthDao.delete(Arrays.asList(arrInteger));
		if (update) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, id);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	
	@Override
	public boolean insert(BaseMapRoleDimAuthEntity baseMapRoleDimAuthEntity) {
		return baseMapRoleDimAuthDao.insert(baseMapRoleDimAuthEntity);
	}
	
	
	/**
	 * 批量添加
	 */
	@Override
	public boolean insertCodeBatch(Integer[] dimauthId,Integer roleId,String userName){
		List<Integer> dimauthIds=Arrays.asList(dimauthId);
		List<BaseMapRoleDimAuthEntity> collect = dimauthIds.stream().map(l->{
			BaseMapRoleDimAuthEntity baseMapRoleElementEntity=new BaseMapRoleDimAuthEntity();
			baseMapRoleElementEntity.setCreateTime(new Date());
			baseMapRoleElementEntity.setRoleId(roleId);
			baseMapRoleElementEntity.setDimAuthId(l);
			baseMapRoleElementEntity.setStatus(StatusEnum.NORMAL.getValues());
			baseMapRoleElementEntity.setCreateUser(userName);
			return baseMapRoleElementEntity;
		}).collect(Collectors.toList());
		return baseMapRoleDimAuthDao.insertCodeBatch(collect);
	}
}
