package com.flchy.blog.privilege.config.service;

import java.util.List;

import com.flchy.blog.privilege.config.entity.BaseMapRoleDimAuthEntity;

public interface IBaseMapRoleDimAuthService {

	List<BaseMapRoleDimAuthEntity> selectRoleDimAuthBySelected(Integer roleId);

	Object delete(String id);

	boolean insert(BaseMapRoleDimAuthEntity baseMapRoleDimAuthEntity);

	boolean insertCodeBatch(Integer[] dimauthId, Integer roleId, String userName);

}
