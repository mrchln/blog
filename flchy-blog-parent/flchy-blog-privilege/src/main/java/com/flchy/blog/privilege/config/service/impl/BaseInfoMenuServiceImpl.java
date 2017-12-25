package com.flchy.blog.privilege.config.service.impl;

import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.collections.map.CaseInsensitiveMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.flchy.blog.base.holder.PropertiesHolder;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.VisitsMapResult;
import com.flchy.blog.privilege.config.bean.BaseMenu;
import com.flchy.blog.privilege.config.cache.MenuResourceCache;
import com.flchy.blog.privilege.config.dao.IBaseInfoMenuDao;
import com.flchy.blog.privilege.config.dao.IBaseMapRoleUserDao;
import com.flchy.blog.privilege.config.dao.IBaseResElementDao;
import com.flchy.blog.privilege.config.dao.IBaseResPanelDao;
import com.flchy.blog.privilege.config.entity.BaseConfPrivEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRoleUserEntity;
import com.flchy.blog.privilege.config.service.IBaseConfPrivService;
import com.flchy.blog.privilege.config.service.IBaseInfoMenuService;
import com.flchy.blog.privilege.enums.FormatEnum;
import com.flchy.blog.privilege.support.impl.ResourceServiceImpl;
import com.flchy.blog.utils.NewMapUtil;
import com.flchy.blog.utils.convert.BeanConvertUtil;
import com.flchy.blog.utils.convert.MapConvertUtil;

@Service
public class BaseInfoMenuServiceImpl implements IBaseInfoMenuService {
	private static Logger logger = LoggerFactory.getLogger(BaseInfoMenuServiceImpl.class);
	@Autowired
	private IBaseInfoMenuDao baseInfoMenuDao;
	@Autowired
	private IBaseConfPrivService iBaseConfPrivService;
	@Autowired
	private IBaseMapRoleUserDao iBaseMapRoleUserDao;
	@Autowired
	private IBaseResPanelDao iBaseResPanelDao;
	@Autowired
	private IBaseResElementDao iBaseResElementDao;

	@Override
	public BaseInfoMenuEntity selectMenuInfoById(int menuId) {
		return baseInfoMenuDao.selectMenuInfoById(menuId);
	}

	@Override
	public List<BaseInfoMenuEntity> selectAllMenuList() {
		return baseInfoMenuDao.selectAllMenuList();
	}

	@Override
	@Transactional
	public boolean insertMenu(BaseInfoMenuEntity baseInfoMenuEntity) {
		boolean menuId = baseInfoMenuDao.insert(baseInfoMenuEntity);
		System.out.println(baseInfoMenuEntity.getMenuId());
		System.out.println(menuId);
		if (!menuId) {
			throw new RuntimeException("添加异常");
		}
		BaseConfPrivEntity baseConfPrivEntity = new BaseConfPrivEntity();
		baseConfPrivEntity.setPrivVisitId(FormatEnum.VISIT.getCode() + baseInfoMenuEntity.getMenuId());
		baseConfPrivEntity.setCreateUser(baseInfoMenuEntity.getCreateUser());
		baseConfPrivEntity.setCreateTime(baseInfoMenuEntity.getCreateTime());
		baseConfPrivEntity.setStatus(baseInfoMenuEntity.getStatus());
		baseConfPrivEntity.setMenuId(baseInfoMenuEntity.getMenuId());
		boolean insert = iBaseConfPrivService.insert(baseConfPrivEntity);
		if (!insert) {
			throw new RuntimeException("添加异常");
		}
		MenuResourceCache.getInstance().initialize();
		return true;
	}

	@Override
	public boolean update(BaseInfoMenuEntity baseInfoMenuEntity) {
		boolean update = baseInfoMenuDao.update(baseInfoMenuEntity);
		MenuResourceCache.getInstance().initialize();
		return update;
	}

	@Override
	@Transactional
	public Object delete(String menuId) {
		String subfirst = menuId.substring(0, 1);
		String subLast = menuId.substring(menuId.length() - 1, menuId.length());
		if (",".equals(subfirst)) {
			menuId = menuId.substring(1, menuId.length());
		}
		if (",".equals(subLast)) {
			menuId = menuId.substring(0, menuId.length() - 1);
		}
		String[] split = menuId.split(",");
		Integer[] arrInteger = new Integer[split.length];

		try {
			for (int i = 0; i < split.length; i++) {
				if (!split[i].trim().equals("") && split[i] != null)
					arrInteger[i] = Integer.valueOf(split[i].trim().toString());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "The roleId format is incorrect!").get()));
		}
		if (split.length < 1) {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Please choose the option to delete!").get()));
		}
		boolean deleteMenus = deleteMenus(Arrays.asList(arrInteger));
		MenuResourceCache.getInstance().initialize();
		if (deleteMenus) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, menuId);
		} else {
			return new ResponseCommand(ResponseCommand.STATUS_ERROR, new VisitsMapResult(new NewMapUtil("message", "Delete failure！").get()));
		}
	}

	@Transactional
	public boolean deleteMenus(List<Integer> menuIds) {
		// 删除菜单下面的url
		iBaseConfPrivService.delete(menuIds);
		// 删除菜单下面的元素
		iBaseResElementDao.deleteByMenuId(menuIds);
		// 删除菜单下面的面板
		iBaseResPanelDao.deleteByMenuId(menuIds);
		// 删除菜单
		baseInfoMenuDao.delete(menuIds);
		return true;
	}

	@Override
	public BaseMenu selectMenuByUserId(Integer userId) {
		List<BaseInfoMenuEntity> selectMenuByUserId;
		String roleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(roleId)) {
			List<BaseMapRoleUserEntity> selectRolesByUserId = iBaseMapRoleUserDao.selectRolesByUserId(userId);
			List<BaseMapRoleUserEntity> collect = selectRolesByUserId.stream().filter(l -> {
				return roleId.equals(String.valueOf(l.getRoleId()));
			}).collect(Collectors.toList());
			if (collect.size() > 0) {
				selectMenuByUserId = baseInfoMenuDao.selectAllMenuList();
			} else {
				selectMenuByUserId = baseInfoMenuDao.selectMenuByUserId(userId);
			}
		} else {
			selectMenuByUserId = baseInfoMenuDao.selectMenuByUserId(userId);
		}
		if (CollectionUtils.isEmpty(selectMenuByUserId)) {
			return null;
		}

		Map<Integer, BaseMenu> menuMap = new CaseInsensitiveMap(selectMenuByUserId.size());
		for (BaseInfoMenuEntity menuResEntity : selectMenuByUserId) {
			BaseMenu menuBean = BeanConvertUtil.map(menuResEntity, BaseMenu.class);
			menuBean.setLabel(menuBean.getMenuName());

			menuMap.put(menuResEntity.getMenuId(), menuBean);
		}
		BaseMenu rootMenuBean = new BaseMenu(ResourceServiceImpl.MENU_ROOT_ID);
		for (BaseMenu menuBean : menuMap.values()) {
			Integer pid = menuBean.getMenuPid();
			if (null != pid && pid.equals(rootMenuBean.getMenuId())) {
				rootMenuBean.addChild(menuBean);
			} else {
				BaseMenu parentMenuBean = menuMap.get(pid);
				if (null != parentMenuBean) {
					parentMenuBean.addChild(menuBean);
				}
			}
		}
		menuMap.put(-1, rootMenuBean);
		// 按sort_index进行菜单排序
		rootMenuBean.sort();
		return menuMap.get(-1);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> selectMenuByRoleId(Integer roleId) {
		String adminRoleId = PropertiesHolder.getProperty("role.superadmin");
		if (StringUtils.hasText(adminRoleId)) {
			if (roleId.toString().equals(adminRoleId)) {
				List<BaseInfoMenuEntity> selectAllMenuList = selectAllMenuList();
				List<Map<String, Object>> list = new ArrayList<>();
				for (BaseInfoMenuEntity baseInfoMenuEntity : selectAllMenuList) {
					Map<String, Object> convertBeanToMap = null;
					try {
						convertBeanToMap = MapConvertUtil.convertBeanToMap(baseInfoMenuEntity, true);
					} catch (IllegalAccessException | InvocationTargetException | IntrospectionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					list.add(convertBeanToMap);
				}
				return list;
			}
		}
		return baseInfoMenuDao.selectMenuByRoleId(roleId);
	}

	@Override
	public BaseMenu selectByRoleIdAllMenu(Integer roleId) {

		List<Map<String, Object>> menuList = baseInfoMenuDao.selectByRoleIdAllMenu(roleId);

		if (CollectionUtils.isEmpty(menuList)) {
			return null;
		}

		Map<Integer, BaseMenu> menuMap = new CaseInsensitiveMap(menuList.size());
		for (Map<String, Object> menuResEntity : menuList) {
			BaseMenu menuBean = null;
			try {
				menuBean = (BaseMenu) MapConvertUtil.convertMapToBean(BaseMenu.class, menuResEntity);
			} catch (IllegalAccessException | InstantiationException | InvocationTargetException | IntrospectionException e) {
				e.printStackTrace();
			}
			String adminRoleId = PropertiesHolder.getProperty("role.superadmin");
			if (StringUtils.hasText(adminRoleId)) {
				if (roleId.toString().equals(adminRoleId)) {
					menuBean.setSelected("true");
				}

			}
			menuBean.setLabel(menuBean.getMenuName());
			menuMap.put(Integer.valueOf(menuResEntity.get("menuId").toString()), menuBean);
		}
		BaseMenu rootMenuBean = new BaseMenu(ResourceServiceImpl.MENU_ROOT_ID);
		for (BaseMenu menuBean : menuMap.values()) {
			Integer pid = menuBean.getMenuPid();
			if (null != pid && pid.equals(rootMenuBean.getMenuId())) {
				rootMenuBean.addChild(menuBean);
			} else {
				BaseMenu parentMenuBean = menuMap.get(pid);
				if (null != parentMenuBean) {
					parentMenuBean.addChild(menuBean);
				}
			}
		}
		menuMap.put(-1, rootMenuBean);
		// 按sort_index进行菜单排序
		rootMenuBean.sort();
		return menuMap.get(-1);
	}

}
