package com.flchy.blog.base.dbconfig.service;
import java.util.List;

import com.flchy.blog.base.dbconfig.bean.EnumDicBean;
import com.flchy.blog.base.dbconfig.entity.EnumDicEntity;

public interface IEnumDicService {

    public List<EnumDicEntity> selectEnumsListByObj(EnumDicBean enumDicBean);


	boolean updateEnum(EnumDicBean enumDicBean);

	boolean insertEnum(EnumDicBean enumDicBean);


	Boolean deleteEnum(List<Integer> enumId);

}