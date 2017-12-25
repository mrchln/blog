package com.flchy.blog.privilege.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseInfoMenuEntity;

@ConfigRepository
public interface IBaseInfoMenuDao {

	public BaseInfoMenuEntity selectMenuInfoById(@Param("menuId") int menuId);
	
	public List<BaseInfoMenuEntity> selectAllMenuList();
	
	public boolean insert(BaseInfoMenuEntity baseInfoMenuEntity);

	public boolean update(BaseInfoMenuEntity baseInfoMenuEntity);

	public boolean delete(List<Integer> asList);
	
	public List<BaseInfoMenuEntity> selectMenuByUserId(@Param("userId") Integer userId);
	
	public List<Map<String, Object>> selectMenuByRoleId(@Param("roleId") Integer roleId);
	
	public List<Map<String, Object>> selectByRoleIdAllMenu(@Param("roleId") Integer roleId);
	
	
	
}
