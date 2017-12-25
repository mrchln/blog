package com.flchy.blog.privilege.support.impl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.flchy.blog.privilege.config.bean.ConfUrlBean;
import com.flchy.blog.privilege.config.entity.BaseConfPrivEntity;
import com.flchy.blog.privilege.config.entity.BaseConfUrlEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoRoleEntity;
import com.flchy.blog.privilege.config.entity.BaseInfoUserEntity;
import com.flchy.blog.privilege.config.entity.BaseMapPrivUrlEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRoleDimAuthEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRoleElementEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRolePanelEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRolePrivEntity;
import com.flchy.blog.privilege.config.entity.BaseMapRoleUserEntity;
import com.flchy.blog.privilege.config.service.IBaseConfPrivService;
import com.flchy.blog.privilege.config.service.IBaseConfUrlService;
import com.flchy.blog.privilege.config.service.IBaseInfoMenuService;
import com.flchy.blog.privilege.config.service.IBaseInfoRoleService;
import com.flchy.blog.privilege.config.service.IBaseInfoUserService;
import com.flchy.blog.privilege.config.service.IBaseMapPrivRoleService;
import com.flchy.blog.privilege.config.service.IBaseMapPrivUrlService;
import com.flchy.blog.privilege.config.service.IBaseMapResRoleService;
import com.flchy.blog.privilege.config.service.IBaseMapUserRoleService;
import com.flchy.blog.privilege.config.service.IBaseResInfoService;
import com.flchy.blog.privilege.support.IAuthServeService;

@Service
public class AuthServeServiceImpl implements IAuthServeService {
	private static Logger logger = LoggerFactory.getLogger(AuthServeServiceImpl.class);
	@Autowired //用户数据操作类
	private IBaseInfoUserService baseInfoUserService;
	@Autowired //菜单数据操作类
	private IBaseInfoMenuService baseInfoMenuService;
	@Autowired //角色数据操作类
	private IBaseInfoRoleService baseInfoRoleService;
	@Autowired //用户和角色映射类
	private IBaseMapUserRoleService baseMapUserRoleService;
	@Autowired //访问权限表，一个菜单对应一个访问权限，新建一个菜单记录，则自动创建一个访问权限记录
	private IBaseConfPrivService baseConfPrivService;
	@Autowired //访问Url路径： 每次启动自动扫描，添加当前表，若不存在的，则标记为失效，增量维护，不删除
	private IBaseConfUrlService baseConfUrlService;
	@Autowired //访问权限与URL映射关系表，为每个访问权限对应的Url地址,需要配置，不配置则无法访问
	private IBaseMapPrivUrlService baseMapPrivUrlService;
	@Autowired //访问权限和角色映射表
	private IBaseMapPrivRoleService baseMapPrivRoleService;
 	@Autowired // 资源数据操作类; 含（面板资源、元素资源、数据资源）
	private IBaseResInfoService baseResInfoService;
 	@Autowired // 资源和角色映射表; 含（面板资源、元素资源、数据资源）
	private IBaseMapResRoleService baseMapResRoleService;
 	
	/**
	 * 根据用户名获取用户对象登录
	 */
	@Override
	public BaseInfoUserEntity selectUserByUserName(String userName) {
		List<BaseInfoUserEntity>  userLocalList =  baseInfoUserService.selectUserByUserName(userName);
		if(null!=userLocalList && !userLocalList.isEmpty()){
			return userLocalList.get(0);
		}
		return null;
	}

	/**
	 * 依据菜单资源ID获取菜单对象
	 */
	@Override
	public BaseInfoMenuEntity selectMenuInfoById(int menuId) {
		return baseInfoMenuService.selectMenuInfoById(menuId);
	}

	/**
	 * 依据账号ID，获取对应的角色集合
	 */
	@Override
	public List<BaseInfoRoleEntity> selectRolesByUserId(int userId) {
		List<BaseMapRoleUserEntity> userRoleListEntities = baseMapUserRoleService.selectRolesByUserId(userId);
		if (userRoleListEntities == null || userRoleListEntities.isEmpty()) {
			return null;
		}
		Integer[] roleIds = new Integer[userRoleListEntities.size()];
		for (int i = 0; i < userRoleListEntities.size(); i++) {
			roleIds[i] = userRoleListEntities.get(i).getRoleId();
		}
		// 获取角色对象集合
		return baseInfoRoleService.selectRolesByRoleIds(roleIds);
	}

	/**
	 * 获取所有的授权菜单资源Id
	 */
	@Override
	public Set<Integer> getAuthMenuIds(int userId) {
		// 获取账号对应的菜单资源
		return this.getAuthResMenuIds(userId);
	}
	
	/**
     *  获取所有的访问权限Id
     */
	@Override
    public Set<String> getAuthPrivIds(int userId){
		// 获取账号对应的访问权限
		return this.getAuthPrivVisitIds(userId);
    }

	/**
	 * 获取所有的授权访问Url
	 */
	@Override
	public Set<ConfUrlBean> getAuthUrlPaths(int userId) {
		// 获取账号对应的访问Url
		return this.getAuthVisitUrls(userId);
	}
	/**
	 * 获取所有的授权元素资源Id
	 */
	@Override
	public Set<Integer> getAuthElemIds(int userId) {
		// 获取账号对应的元素资源
		return this.getAuthResElemIds(userId);
	}

	/**
	 *  获取所有的授权面板资源Id
	 */
	@Override
	public Set<Integer> getAuthPanelIds(int userId) {
		// 获取账号对应的面板资源
		return getAuthResPanelIds(userId);
	}

	/**
	 * 获取所有的授权维表资源数据
	 */
	@Override
	public List<BaseInfoDimAuthEntity> selectDimAuthByUserId(int userId) {
		return getResDimAuthByUserId(userId);
	}

	/**
	 * 根据账号编码查询返回该账号拥有访问菜单资源Set集合
	 *
	 * @param accountId
	 * @param systemId
	 * @return
	 */
	private Set<Integer> getAuthResMenuIds(int userId) {
		List<BaseConfPrivEntity> listPrivVisitConf = this.selectConfPrivByUserId(userId);
		if (null != listPrivVisitConf && !listPrivVisitConf.isEmpty()) {
			Set<Integer> PrivVisitIdSet = new HashSet<>();
			for (Iterator<BaseConfPrivEntity> item = listPrivVisitConf.iterator(); item.hasNext();) {
				PrivVisitIdSet.add(item.next().getMenuId());
			}
			return PrivVisitIdSet;
		}
		return null;
	}
	
	/**
	 * 获取所有的访问权限Id
	 * @param userId
	 * @return
	 */
	private Set<String>  getAuthPrivVisitIds(int userId) {
		Set<Integer> roleIdSet = this.getRolesSetByUserId(userId);
		if (roleIdSet == null || roleIdSet.isEmpty()) {
			return null;
		}
		Integer[] roleIdArray = new Integer[roleIdSet.size()];
		roleIdSet.toArray(roleIdArray);
		if (null != roleIdArray && roleIdArray.length > 0) {
			return this.selectPrivIdsSetByRoleIds(roleIdArray);
		}
		return null;
	}

	/**
	 * 根据账号编码查询返回该账号拥有访问Url资源Set集合
	 * @param userId
	 * @return
	 */
	private Set<ConfUrlBean> getAuthVisitUrls(int userId) {
		Set<Integer> roleIdSet = this.getRolesSetByUserId(userId);
		if (roleIdSet == null || roleIdSet.isEmpty()) {
			return null;
		}
		Integer[] roleIdArray = new Integer[roleIdSet.size()];
		roleIdSet.toArray(roleIdArray);
		if (null != roleIdArray && roleIdArray.length > 0) {
			List<BaseMapPrivUrlEntity> mapPrivUrlList = this.selectPrivVisitUrlMapByUserId(userId);
			if (null != mapPrivUrlList) {
				Set<Integer> urlIdSet = new HashSet<>();
				for (BaseMapPrivUrlEntity baseMapPrivUrlEntity : mapPrivUrlList) {
					urlIdSet.add(baseMapPrivUrlEntity.getUrlId());
				}
				if (roleIdSet == null || roleIdSet.isEmpty()) {
					return null;
				}
				Integer[] urlIdArray = new Integer[urlIdSet.size()];
				urlIdSet.toArray(urlIdArray);
				List<BaseConfUrlEntity> baseConfUrlList = baseConfUrlService.selectConfUrlByUrlIds(urlIdArray);
				if (null != baseConfUrlList) {
					Set<ConfUrlBean> urlPathSet = new HashSet<>();
					for (BaseConfUrlEntity baseConfUrlEntity : baseConfUrlList) {
						ConfUrlBean confUrlBean=new ConfUrlBean();
						confUrlBean.setMethod(baseConfUrlEntity.getMethod());
						confUrlBean.setUrlPath(baseConfUrlEntity.getUrlPath());
						urlPathSet.add( confUrlBean);
					}
					return urlPathSet;
				}
			}
		}
		return null;
	}
	/**
	 * 根据账号编码查询返回该角色所拥有访问权限
	 */
	private List<BaseConfPrivEntity> selectConfPrivByUserId(int userId) {
		Set<Integer> roleIdSet = this.getRolesSetByUserId(userId);
		if (roleIdSet == null || roleIdSet.isEmpty()) {
			return null;
		}
        Integer[] roleIdArray = new Integer[roleIdSet.size()];
		roleIdSet.toArray(roleIdArray);
		if (null != roleIdArray && roleIdArray.length > 0) {
			return this.selectConfPrivByRoleIds(roleIdArray);
		}
		return null;
	}

	/**
	 * 根据角色编码查询返回该角色所拥有访问权限
	 */
	private List<BaseConfPrivEntity> selectConfPrivByRoleIds(Integer[] roleIds) {
		// 获取对应的菜单访问权限
		List<BaseMapRolePrivEntity> privRoleVisitEntities = baseMapPrivRoleService.selectConfPrivByRoleIds(roleIds);
		if (privRoleVisitEntities == null || privRoleVisitEntities.isEmpty()) {
			return null;
		}
		// 过滤重复的权限编码，如果角色存在重复的权限可将重复的权限编码过滤掉
		Set<String> privVisitIdSet = new HashSet<>();
		for (Iterator<BaseMapRolePrivEntity> item = privRoleVisitEntities.iterator(); item.hasNext();) {
			privVisitIdSet.add(item.next().getPrivVisitId());
		}

		String[] privVisitIdArray = new String[privVisitIdSet.size()];
		privVisitIdSet.toArray(privVisitIdArray);
		if (null != privVisitIdArray && privVisitIdArray.length > 0) {
			// 依据访问权限，获取对应的菜单资源编码
			return baseConfPrivService.selectConfPrivByPrivVisitIds(privVisitIdArray);
		}
		return null;
	}
	
	/**
	 * 根据角色编码查询返回该角色所拥有访问权限编码集合
	 * @param roleIds
	 * @return
	 */
	private Set<String> selectPrivIdsSetByRoleIds(Integer[] roleIds) {
		// 获取角色对应的访问权限
		List<BaseConfPrivEntity> listPrivVisitConf = this.selectConfPrivByRoleIds(roleIds);
		if (listPrivVisitConf == null || listPrivVisitConf.isEmpty()) {
			return null;
		}
		Set<String> privVisitIdSet = new HashSet<>();
		for (Iterator<BaseConfPrivEntity> item = listPrivVisitConf.iterator(); item.hasNext();) {
			privVisitIdSet.add(item.next().getPrivVisitId());
		}
		return privVisitIdSet;
	}

	/**
	 * 根据账号编码查询返回该账号拥有访问按钮资源Set集合
	 *
	 * @param accountId
	 * @param systemId
	 * @return
	 */
	private Set<Integer> getAuthResElemIds(int userId) {
		Set<Integer> roleIdSet = this.getRolesSetByUserId(userId);
		if (roleIdSet == null || roleIdSet.isEmpty()) {
			return null;
		}
		Integer[] roleIdArray = new Integer[roleIdSet.size()];
		roleIdSet.toArray(roleIdArray);
		if (null != roleIdArray && roleIdArray.length > 0) {
			List<BaseMapRoleElementEntity> privRoleElementEntities = baseMapResRoleService.selectRoleElementByRoleIds(roleIdArray);
			if (privRoleElementEntities == null || privRoleElementEntities.isEmpty()) {
				return null;
			}
			Set<Integer> elementIds = new HashSet<>();
			for (BaseMapRoleElementEntity privRoleElementEntity : privRoleElementEntities) {
				elementIds.add(privRoleElementEntity.getElementId());
			}
			return elementIds;
		}
		return null;
	}

	/**
	 * 根据账号编码查询返回该账号拥有访问按钮资源Set集合
	 *
	 * @param accountId
	 * @param systemId
	 * @return
	 */
	private Set<Integer> getAuthResPanelIds(int userId) {
		Set<Integer> roleIdSet = this.getRolesSetByUserId(userId);
		if (roleIdSet == null || roleIdSet.isEmpty()) {
			return null;
		}
		Integer[] roleIdArray = new Integer[roleIdSet.size()];
		roleIdSet.toArray(roleIdArray);
		if (null != roleIdArray && roleIdArray.length > 0) {
			List<BaseMapRolePanelEntity> privRolePanelEntities = baseMapResRoleService.selectRolePanelByRoleIds(roleIdArray);
			if (privRolePanelEntities == null || privRolePanelEntities.isEmpty()) {
				return null;
			}
			Set<Integer> resPanelIds = new HashSet<>();
			for (BaseMapRolePanelEntity privRolePanelEntity : privRolePanelEntities) {
				resPanelIds.add(privRolePanelEntity.getPanelId());
			}
			return resPanelIds;
		}
		return null;
	}

	/**
	 * 根据账号编码查询返回该账号拥有数据权限资源
	 *
	 * @param accountId
	 * @param systemId
	 * @return
	 */
	private List<BaseInfoDimAuthEntity> getResDimAuthByUserId(int userId) {
		Set<Integer> roleIdSet = this.getRolesSetByUserId(userId);
		if (roleIdSet == null || roleIdSet.isEmpty()) {
			return null;
		}
		Integer[] roleIdArray = new Integer[roleIdSet.size()];
		roleIdSet.toArray(roleIdArray);
		if (null != roleIdArray && roleIdArray.length > 0) {
			List<BaseMapRoleDimAuthEntity> roleDimAuthMapList = baseMapResRoleService.selectRoleDimAuthByRoleIds(roleIdArray);
			if (null != roleDimAuthMapList) {
				Set<Integer> dimAuthIdSet = new HashSet<>();
				for (BaseMapRoleDimAuthEntity baseMapRoleDimAuthEntity : roleDimAuthMapList) {
					dimAuthIdSet.add(baseMapRoleDimAuthEntity.getDimAuthId());
				}
				if (dimAuthIdSet == null || dimAuthIdSet.isEmpty()) {
					return null;
				}
				Integer[] dimAuthIdArray = new Integer[dimAuthIdSet.size()];
				dimAuthIdSet.toArray(dimAuthIdArray);
				if (null != dimAuthIdArray && dimAuthIdArray.length > 0) {
					return baseResInfoService.selectDimAuthInfoByDimAuthIds(dimAuthIdArray);
				}
			}
		}
		return null;
	}

	
	/**
	 * 根据账号编码查询返回该角色所拥有访问菜单资源
	 */
	private List<BaseMapPrivUrlEntity> selectPrivVisitUrlMapByUserId(int userId) {
		Set<Integer> roleIdSet = this.getRolesSetByUserId(userId);
		if (roleIdSet == null || roleIdSet.isEmpty()) {
			return null;
		}
		Integer[] roleIdArray = new Integer[roleIdSet.size()];
		roleIdSet.toArray(roleIdArray);
		if (null != roleIdArray && roleIdArray.length > 0) {
			return this.selectPrivUrlMapByRoleIds(roleIdArray);
		}
		return null;
	}

	/**
	 * 根据角色编码查询返回该角色所拥有访问菜单资源
	 */
	private List<BaseMapPrivUrlEntity> selectPrivUrlMapByRoleIds(Integer[] roleIds) {
		Set<String> privVisitIdSet = this.selectPrivIdsSetByRoleIds(roleIds);
		if (null == privVisitIdSet || privVisitIdSet.isEmpty()) {
			return null;
		}
		String[] privVisitIdArray = new String[privVisitIdSet.size()];
		privVisitIdSet.toArray(privVisitIdArray);
		if (null != privVisitIdArray && privVisitIdArray.length > 0) {
			// 依据权限获取对应的URL
			return baseMapPrivUrlService.selectPrivUrlMapByPrivVisitIds(privVisitIdArray);
		}
		return null;
	}
	
	

	/**
	 * 根据账号编码查询返回所有的角色ID
	 */
	@Override
	public Set<Integer> getRolesSetByUserId(int userId) {
		// 通过账号编码(accountId)获取账号与角色的关联关系
		List<BaseMapRoleUserEntity> accountRoleEntities = baseMapUserRoleService.selectRolesByUserId(userId);
		if (accountRoleEntities == null || accountRoleEntities.isEmpty()) {
			return null;
		}
		// 过滤重复的角色编码，目的是做组角色和用户直接关联到的角色的合并，避免出现角色重复
		Set<Integer> roleIdSet = new HashSet<>();
		if (null != accountRoleEntities && !accountRoleEntities.isEmpty()) {
			for (Iterator<BaseMapRoleUserEntity> item = accountRoleEntities.iterator(); item.hasNext();) {
				roleIdSet.add(item.next().getRoleId());
			}
		}
		return roleIdSet;
	}

}
