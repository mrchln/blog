package com.flchy.blog.base.dbconfig.dao;
import java.util.List;

import org.apache.ibatis.annotations.Insert;

import com.flchy.blog.base.datasource.annotation.ConfigRepository;
import com.flchy.blog.base.dbconfig.bean.EnumDicBean;
import com.flchy.blog.base.dbconfig.entity.EnumDicEntity;


@ConfigRepository
public interface IEnumDicDao {

    public List<EnumDicEntity> selectEnumsListByObj(EnumDicBean enumDicBean);
    
	@Insert("INSERT INTO `dic_enum` (`enum_code`, `enum_group`, `enum_value`, `enum_txt`, `remark`, `sort_index`, `is_cache`, `status`, `create_user`, `create_time`) VALUES (#{enumCode}, #{enumGroup},#{enumValue}, #{enumTxt}, #{remark}, #{sortIndex}, #{isCache}, #{status}, #{createUser}, #{createTime})")
	public boolean insertEnum(EnumDicEntity enumDicEntity);
	
	
	public boolean deleteEnum(List<Integer> asList);
	
	public boolean updateEnum(EnumDicEntity enumDic);
	
	
}