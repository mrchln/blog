package com.flchy.blog.inlets.controller;

import java.util.List;

import javax.ws.rs.QueryParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.flchy.blog.base.annotation.Log;
import com.flchy.blog.base.enums.OperateCodeEnum;
import com.flchy.blog.base.exception.BusinessException;
import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.inlets.holder.ConfigHolder;
import com.flchy.blog.inlets.service.IConfigService;
import com.flchy.blog.pojo.Config;

/**
 * <p>
 * 配置信息表 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-11-22
 */
@RestController
@RequestMapping("config")
public class ConfigController {
	@Autowired
	private IConfigService iConfigService;
	
	
	@PostMapping(value = "/page")
	@Log(value="查询配置信息分页",type=OperateCodeEnum.SELECT)
	public Object selectArticlePage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size, Config article) {
			Page<Config> page = new Page<>(Integer.valueOf(current), Integer.valueOf(size));
			iConfigService.selectPage(page, new EntityWrapper<Config>(article));
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, new ResultPage(page));
	}
	
	@PostMapping
	@Log(value="添加配置信息",type=OperateCodeEnum.INSERT)
	public Object insert(Config entity) {
		boolean isok = iConfigService.insert(entity);
		if (!isok) {
			throw new BusinessException("Add failed");
		}
		ConfigHolder.refreshCache();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}

	@PutMapping
	@Log(value="修改配置信息",type=OperateCodeEnum.UPDATE)
	public Object update(Config entity) {
		if (entity.getId() == null) {
			throw new BusinessException("ID must preach");
		}
		boolean isok = iConfigService.updateById(entity);
		if (!isok) {
			throw new BusinessException("Update failed");
		}
		ConfigHolder.refreshCache();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}

	@DeleteMapping
	@Log(value="删除配置信息",type=OperateCodeEnum.DELETE)
	public Object delete(Config entity) {
		if(entity.getId()==null){
			throw new BusinessException("ID must preach");
		}
		boolean isok = iConfigService.deleteById(entity.getId());
		if (!isok) {
			throw new BusinessException("Delete failed");
		}
		ConfigHolder.refreshCache();
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, entity);
	}

	@GetMapping
	@Log(value="获取配置信息",type=OperateCodeEnum.SELECT)
	public Object selectKey(@QueryParam("id") Integer id) {
		if (id != null) {
			return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, iConfigService.selectById(id));
		}
		List<Config> response = iConfigService.selectList(new EntityWrapper<Config>());
		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS, response);
	}
}
