package com.flchy.blog.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class test {
	@Value("${foo}")
	private String vo;

	@GetMapping("/vo")
	public Object getVo() {
		return vo;
	}

}
