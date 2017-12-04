package com.tools.plugin.wxchat.utils;

import java.text.MessageFormat;
import java.util.Date;

public class WxChatNews {
	private static String text="<xml><ToUserName><![CDATA[{0}]]></ToUserName><FromUserName><![CDATA[{0}]]></FromUserName><CreateTime>{2}</CreateTime><MsgType><![CDATA[text]]></MsgType><Content><![CDATA[{3}]]></Content></xml>";

	
	
	public static String createText(String ToUserName,String FromUserName,String Content){
		return MessageFormat.format(text,ToUserName,FromUserName,new Date().getTime(),Content);
	}
}
