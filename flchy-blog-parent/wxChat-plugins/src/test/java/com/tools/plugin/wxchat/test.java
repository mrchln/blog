package com.tools.plugin.wxchat;

import java.util.HashMap;
import java.util.Map;

import com.tools.plugin.wxchat.holder.PropertiesHolder;
import com.tools.plugin.wxchat.utils.HttpRequestor;
import com.tools.plugin.wxchat.utils.WxChatInterface;

public class test {
	public static void main(String[] args) throws Exception {
		HttpRequestor httpRequestor=new HttpRequestor();
		Map<String, String> postHeaders =new HashMap<>();
		postHeaders.put("grant_type", "client_credential");
		System.out.println( PropertiesHolder.getProperty("wxChat.appID"));
		postHeaders.put("appid", PropertiesHolder.getProperty("wxChat.appID"));
		postHeaders.put("secret", PropertiesHolder.getProperty("wxChat.appsecret"));
		String queryString = httpRequestor.formatGetprParameter(postHeaders);
		  System.out.println(queryString);
		String doGet = httpRequestor.doGet(WxChatInterface.GET_ACCESS_TOKEN+ httpRequestor.formatGetprParameter(postHeaders), null);
		System.out.println(doGet);
//		String doGet = httpRequestor.doGet("https://api.weixin.qq.com/cgi-bin/getcallbackip", postHeaders);
	}	

}
