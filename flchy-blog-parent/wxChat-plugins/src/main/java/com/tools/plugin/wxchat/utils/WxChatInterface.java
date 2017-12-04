package com.tools.plugin.wxchat.utils;

public class WxChatInterface {

	//获取access_token  
	//文档地址:https://mp.weixin.qq.com/wiki?t=resource/res_main&id=mp1421140183
	//grant_type	是	获取access_token填写client_credential
	//appid	是	第三方用户唯一凭证
	//secret	是	第三方用户唯一凭证密钥，即appsecret
	public static final String GET_ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token";
	

}
