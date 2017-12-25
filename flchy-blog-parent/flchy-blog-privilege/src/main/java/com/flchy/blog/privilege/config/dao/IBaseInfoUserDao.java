package com.flchy.blog.privilege.config.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.privilege.config.entity.BaseInfoUserEntity;

@ConfigRepository
public interface IBaseInfoUserDao {

	public List<BaseInfoUserEntity> selectUserByUserName(@Param("userName") String userName);

	public List<BaseInfoUserEntity> selectUserByUser(BaseInfoUserEntity baseInfoUserEntity);
	
	public List<BaseInfoUserEntity> verificationUserByUser(BaseInfoUserEntity baseInfoUserEntity);

	public List<BaseInfoUserEntity> selectDeletedUser(BaseInfoUserEntity baseInfoUserEntity);

	@Insert("INSERT INTO `conf_info_user` (`user_name`,`nick_name`,`password`,`nick_name_qp`,`sex`,`email`,`phone_no`,`address`,`postcode`,`remark`,`valid_begin_date`,`valid_end_date`,`photo_path`,`birthday`,`status`,`create_user`,`create_time`)VALUES(#{userName},#{nickName},#{passWord},#{nickNameQp},#{sex},#{email},#{phoneNo},#{address},#{postcode},#{remark},#{validBegin},#{validEnd},#{photoPath},#{birthday},#{status},#{createUser},#{createTime})")
	public Integer insertUser(BaseInfoUserEntity baseInfoUserEntity);

	public boolean updateUser(BaseInfoUserEntity baseInfoUserEntity);

	public boolean deleteUser(List<Integer> userId);

	public boolean restoreUser(List<Integer> userId);

	@Update("UPDATE `conf_info_user` SET `password`=#{passWord} WHERE `pk_id` =#{userId}")
	public boolean updatePwd(BaseInfoUserEntity baseInfoUserEntity);

	public BaseInfoUserEntity selectUserByUserId(Integer userId);

	public List<Map<String, Object>> selectUserByRoleIdSelected(@Param("roleId") Integer roleId,@Param("nickName") String nickName);

	public List<BaseInfoUserEntity> selectUserByRoleIdNotSelected(@Param("roleId") Integer roleId,@Param("nickName") String nickName);

}
