package com.flchy.blog.privilege.config.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleElementDao;
import com.flchy.blog.privilege.config.dao.IBaseMapRolePanelDao;
import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRolePanelEntity;
import com.flchy.blog.privilege.config.service.IBaseMapRolePanelService;
import com.flchy.blog.privilege.enums.StatusEnum;
import com.flchy.blog.utils.NewMapUtil;

/**
 * 角色面板资源映射表
 * 
 * @author nieqs
 *
 */
@Service
public class BaseMapRolePanelServiceImpl implements IBaseMapRolePanelService {
	@Autowired
	private IBaseMapRolePanelDao iBaseMapRolePanelDao;
	@Autowired
	private IBaseMapRoleElementDao iBaseMapRoleElementDao;

	@Override
	public List<BaseMapRolePanelEntity> selectRolePanelByRoleIds(Integer[] roleIds) {
		return iBaseMapRolePanelDao.selectRolePanelByRoleIds(roleIds);
	}

	/**
	 * 查询已选
	 * 
	 * @param roleId
	 *            角色ID
	 * @param menuId
	 *            菜单id
	 * @return
	 */
	@Override
	public List<BaseMapRolePanelEntity> selectRolePanelBySelected(Integer roleId, Integer menuId) {
		return iBaseMapRolePanelDao.selectRolePanelBySelected(roleId, menuId);
	}

	@Transactional
	@Override
	public boolean updateRolePanel(Integer roleId, Integer menuId,Integer[] panelId,String userName) {
		List<BaseMapRolePanelEntity> selectRolePanelBySelected = this.selectRolePanelBySelected(roleId, menuId);
		List<Integer> collect = selectRolePanelBySelected.stream().map(s->s.getPanelId()).collect(Collectors.toList());
		//修改list
		List<Integer> panelIdLists=Arrays.asList(panelId);
		//需要删除数据库已有的panel
		List<Integer> deleteIds=selectRolePanelBySelected.stream().filter(l->{
			return !panelIdLists.contains(l.getPanelId());
		}).map(l->l.getId()).collect(Collectors.toList());
		if(deleteIds!=null && deleteIds.size()>0){
			boolean delete = iBaseMapRolePanelDao.delete(deleteIds);
		}
		//需要添加数据库已有的panel
		List<BaseMapRolePanelEntity> createList=panelIdLists.stream().filter(l->{
			return !collect.contains(l);
		}).map(l->{
			BaseMapRolePanelEntity baseMapRolePanelEntity	=new BaseMapRolePanelEntity();
			baseMapRolePanelEntity.setPanelId(l);
			baseMapRolePanelEntity.setRoleId(roleId);
			baseMapRolePanelEntity.setStatus(StatusEnum.NORMAL.getValues());
			baseMapRolePanelEntity.setCreateTime(new Date());
			baseMapRolePanelEntity.setCreateUser(userName);
			return baseMapRolePanelEntity;
		}).collect(Collectors.toList());
		if(createList!=null && createList.size()>0){
			boolean insertCodeBatch = iBaseMapRolePanelDao.insertCodeBatch(createList);
		}

		return true;
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
		boolean updateUser = deleteRolePanels(Arrays.asList(arrInteger));
		if (updateUser) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, id);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}
	/**
	 * 级联删除
	 * @param rolePanelIds
	 * @return
	 */
	@Transactional
	private boolean deleteRolePanels(List<Integer> rolePanelIds) {
		//查询出所有需要删除的数据
		List<BaseMapRolePanelEntity> selectRolePanelById = iBaseMapRolePanelDao.selectRolePanelById(rolePanelIds);
		//按角色分组
		Map<Integer, List<BaseMapRolePanelEntity>> collect = selectRolePanelById.stream().collect(Collectors.groupingBy(BaseMapRolePanelEntity::getRoleId));
		//删除角色元素权限
		for (Integer roleId : collect.keySet()) { 
			List<BaseMapRolePanelEntity> list = collect.get(roleId);
			List<Integer> panelIds = list.stream().map(l->{return l.getPanelId();}).collect(Collectors.toList());
			 List<BaseMapRoleElementEntity> selectRoleElementByPanelId = iBaseMapRoleElementDao.selectRoleElementByPanelId(panelIds, roleId);
			 List<Integer> roleElementId = selectRoleElementByPanelId.stream().map(l->{return l.getId();}).collect(Collectors.toList());
			 if(roleElementId!=null && roleElementId.size()>0){
				 iBaseMapRoleElementDao.delete(roleElementId);
			 }
		}
		boolean updateUser = iBaseMapRolePanelDao.delete(rolePanelIds);
		return updateUser;
	}

	@Override
	public boolean insert(BaseMapRolePanelEntity baseMapRolePanelEntity) {
		return iBaseMapRolePanelDao.insert(baseMapRolePanelEntity);
	}
	
	
	/**
	 * 批量添加
	 */
	@Override
	public boolean insertCodeBatch(Integer[] panelId,Integer roleId,String userName){
		List<BaseMapRolePanelEntity> selectRolePanelByRoleIds = selectRolePanelByRoleIds(new Integer[]{roleId});
		List<Integer> panelIds=Arrays.asList(panelId);
		//去掉数据库已有的面板       避免重复提交
		List<Integer> panel = selectRolePanelByRoleIds.stream().map(l->{return l.getPanelId();}).collect(Collectors.toList());
		
		List<BaseMapRolePanelEntity> collect = panelIds.stream().filter(l->{return !panel.contains(l);}).map(l->{
			BaseMapRolePanelEntity baseMapRolePanelEntity=new BaseMapRolePanelEntity();
			baseMapRolePanelEntity.setCreateTime(new Date());
			baseMapRolePanelEntity.setRoleId(roleId);
			baseMapRolePanelEntity.setPanelId(l);
			baseMapRolePanelEntity.setStatus(StatusEnum.NORMAL.getValues());
			baseMapRolePanelEntity.setCreateUser(userName);
			return baseMapRolePanelEntity;
		}).collect(Collectors.toList());
		if(collect!=null && collect.size()>0){
			iBaseMapRolePanelDao.insertCodeBatch(collect);
		}
		return true;
	}

}
