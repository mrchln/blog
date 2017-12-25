package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleElementDao;
import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;
import com.flchy.blog.privilege.config.service.IBaseMapRoleElementService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.utils.NewMapUtil;

@Service
public class BaseMapRoleElementServiceImpl implements IBaseMapRoleElementService {
	@Autowired
	private IBaseMapRoleElementDao iBaseMapRoleElementDao;

	/**
	 * 查询已选
	 * @param roleId 角色ID
	 * @param menuId 菜单id 
	 * @param panelId 面板权限id 
	 */
	@Override
	public List<BaseMapRoleElementEntity> selectRoleElementBySelected(Integer roleId, Integer menuId, Integer panelId) {
		return iBaseMapRoleElementDao.selectRoleElementBySelected(roleId, menuId, panelId);
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
		boolean update= iBaseMapRoleElementDao.delete(Arrays.asList(arrInteger));
		if (update) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, id);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	
	@Override
	public boolean insert(BaseMapRoleElementEntity baseMapRoleElementEntity) {
		return iBaseMapRoleElementDao.insert(baseMapRoleElementEntity);
	}
	
	
	/**
	 * 批量添加
	 */
	@Override
	public boolean insertCodeBatch(Integer[] elementId,Integer roleId,String userName){
		List<Integer> panelIds=Arrays.asList(elementId);
		List<BaseMapRoleElementEntity> collect = panelIds.stream().map(l->{
			BaseMapRoleElementEntity baseMapRoleElementEntity=new BaseMapRoleElementEntity();
			baseMapRoleElementEntity.setCreateTime(new Date());
			baseMapRoleElementEntity.setRoleId(roleId);
			baseMapRoleElementEntity.setElementId(l);
			baseMapRoleElementEntity.setStatus(StatusEnum.NORMAL.getValues());
			baseMapRoleElementEntity.setCreateUser(userName);
			return baseMapRoleElementEntity;
		}).collect(Collectors.toList());
		return iBaseMapRoleElementDao.insertCodeBatch(collect);
	}
}
