package com.flchy.blog.inlets.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.base.response.ResponseCommand;
import com.flchy.blog.base.response.ResultPage;
import com.flchy.blog.inlets.service.IImagesService;
import com.flchy.blog.pojo.Images;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author nieqs
 * @since 2017-12-03
 */
@RestController
@RequestMapping("/images")
public class ImagesController {
	@Autowired
	private IImagesService iImagesService;

	@PostMapping(value = "/page")
	public Object selectImagePage(@RequestParam(value = "current", required = true) Integer current,
			@RequestParam(value = "size", required = true) Integer size, Images images) {

		return new ResponseCommand(ResponseCommand.STATUS_SUCCESS,
				new ResultPage(iImagesService.selectImagePage(current, size, images)));
	}
}
