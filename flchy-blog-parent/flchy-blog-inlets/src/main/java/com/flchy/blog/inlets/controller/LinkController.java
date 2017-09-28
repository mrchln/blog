package com.flchy.blog.inlets.controller;

import java.util.Date;
import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.inlets.service.ILinkService;
import com.flchy.blog.pojo.Link;

/**
 * <p>
 * 友情链接 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-08-08
 */
@RestController
@RequestMapping("link")
public class LinkController {

	@Autowired
	private ILinkService iLinkService;

	@PostMapping
	public Object insert(Link entity) {
		entity.setCreateTime(new Date());
		boolean isok = iLinkService.insert(entity);
		if (!isok) {
			throw new BusinessException("Add failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}

	@PutMapping
	public Object update(Link entity) {
		if (entity.getId() == null) {
			throw new BusinessException("ID must preach");
		}
		entity.setUpdateTime(new Date());
		boolean isok = iLinkService.update(entity, new EntityWrapper<Link>().where("id={0}", entity.getId()));
		if (!isok) {
			throw new BusinessException("Update failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}

	@DeleteMapping
	public Object delete(Link entity) {
		boolean isok = iLinkService.delete(new EntityWrapper<Link>(entity));
		if (!isok) {
			throw new BusinessException("Delete failed");
		}
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}

	@GetMapping
	public Object selectKey(@QueryParam("id") Integer id) {
		if (id != null) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, iLinkService.selectById(id));
		}
		List<Link> response = iLinkService.selectList(new EntityWrapper<Link>());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, response);
	}

}
