package com.flchy.blog.privilege.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseInfoDimAuthEntity;

@ConfigRepository
public interface IBaseInfoDimAuthDao {

	public List<BaseInfoDimAuthEntity> selectDimAuthInfoByDimAuthIds(@Param("dimAuthIdArray")Integer[] dimAuthIdArray);

	public List<BaseInfoDimAuthEntity> selectDimAuth(BaseInfoDimAuthEntity baseInfoDimAuthEntity);

	public boolean update(BaseInfoDimAuthEntity baseInfoDimAuthEntity);

	@Insert("INSERT INTO `conf_info_dataauth` (`json_info`, `remark`, `status`, `create_user`, `create_time`) VALUES (#{jsonInfo}, #{remark}, #{status}, #{createUser}, #{createTime})")
	public boolean insert(BaseInfoDimAuthEntity baseInfoDimAuthEntity);

	public boolean delete(List<Integer> asList);
	
	public List<BaseInfoDimAuthEntity> selectByUser(@Param("userId") Integer userId,@Param("remark") String remark);
	
	public List<Map<String, Object>> selectByRole(@Param("roleId") Integer roleId,@Param("remark") String remark);
	
	public List<BaseInfoDimAuthEntity> selectByRoleNotSelected(@Param("roleId") Integer roleId,@Param("remark") String remark);
}
