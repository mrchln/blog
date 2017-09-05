package com.flchy.blog.wxchat.controller;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.DocumentException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.flchy.blog.wxchat.utils.WxChatUtils;

@RestController
@RequestMapping("wxChat")
public class WxChatController {

	@GetMapping
	public Object get(String signature, String timestamp, String nonce, String echostr)
			throws NoSuchAlgorithmException {
		if (WxChatUtils.checkSignature(timestamp, nonce, signature))
			return echostr;
		else
			return false;
	}

	@PostMapping
	public Object post(HttpServletRequest request) throws IOException, DocumentException{
		Map<String, String> parseXml = WxChatUtils.parseXml(request);
		String str= 	WxChatUtils.DisposeXml(parseXml);
//		System.out.println(str);
		return str;
		
	}

}
