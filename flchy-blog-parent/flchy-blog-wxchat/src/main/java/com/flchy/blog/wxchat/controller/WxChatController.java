package com.flchy.blog.wxchat.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.tools.plugin.wxchat.AccessUtil;

@RestController
@RequestMapping("wxChat")
public class WxChatController {

	@GetMapping
	public Object get(String signature, String timestamp, String nonce, String echostr)
			throws NoSuchAlgorithmException {
		if (AccessUtil.checkSignature(timestamp, nonce, signature))
			return echostr;
		else
			return false;
	}

	@PostMapping
	public Object post(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "url", required = true) String url) throws IOException, DocumentException {
		// url解码
//		url = URLDecoder.decode(url, "UTF-8");
//		Map<String, String> parseXml = WxChatUtils.parseXml(request);
//		String str = WxChatUtils.DisposeXml(parseXml);
//		System.out.println(str);
//		return str;
		return false;
	}

}
