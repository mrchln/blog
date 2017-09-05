package com.flchy.blog.wxchat.utils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.net.BCodec;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.flchy.blog.base.holder.PropertiesHolder;

public class WxChatUtils {

	// 请求消息类型：文本
	public static final String REQ_MESSAGE_TYPE_TEXT = "text";
	// 请求消息类型：图片
	public static final String REQ_MESSAGE_TYPE_IMAGE = "image";
	// 请求消息类型：语音
	public static final String REQ_MESSAGE_TYPE_VOICE = "voice";
	// 请求消息类型：视频
	public static final String REQ_MESSAGE_TYPE_VIDEO = "video";
	// 请求消息类型：地理位置
	public static final String REQ_MESSAGE_TYPE_LOCATION = "location";
	// 请求消息类型：链接
	public static final String REQ_MESSAGE_TYPE_LINK = "link";
	// 请求消息类型：事件推送
	public static final String REQ_MESSAGE_TYPE_EVENT = "event";
	// 事件类型：subscribe(订阅)
	public static final String EVENT_TYPE_SUBSCRIBE = "subscribe";
	// 事件类型：unsubscribe(取消订阅)
	public static final String EVENT_TYPE_UNSUBSCRIBE = "unsubscribe";
	// 事件类型：scan(用户已关注时的扫描带参数二维码)
	public static final String EVENT_TYPE_SCAN = "scan";
	// 事件类型：LOCATION(上报地理位置)
	public static final String EVENT_TYPE_LOCATION = "LOCATION";
	// 事件类型：CLICK(自定义菜单)
	public static final String EVENT_TYPE_CLICK = "CLICK";
	// 响应消息类型：文本
	public static final String RESP_MESSAGE_TYPE_TEXT = "text";
	// 响应消息类型：图片
	public static final String RESP_MESSAGE_TYPE_IMAGE = "image";
	// 响应消息类型：语音
	public static final String RESP_MESSAGE_TYPE_VOICE = "voice";
	// 响应消息类型：视频
	public static final String RESP_MESSAGE_TYPE_VIDEO = "video";
	// 响应消息类型：音乐
	public static final String RESP_MESSAGE_TYPE_MUSIC = "music";
	// 响应消息类型：图文
	public static final String RESP_MESSAGE_TYPE_NEWS = "news";

	/**
	 * 验证
	 * 
	 * @param timestamp
	 * @param nonce
	 * @param signature
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean checkSignature(String timestamp, String nonce, String signature)
			throws NoSuchAlgorithmException {
		String[] tmpArr = { PropertiesHolder.getProperty("wxChat.token"), timestamp, nonce };
		Arrays.sort(tmpArr);
		String str = "";
		for (String s : tmpArr) {
			str = str + s;
		}
		// ＳＨＡ－１加密
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		md.update(str.getBytes());
		byte[] bt = md.digest();
		String codeString = new BigInteger(1, bt).toString(16);
		if (codeString.equals(signature)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 解析微信发来的请求（XML）
	 * 
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static Map<String, String> parseXml(HttpServletRequest request) throws IOException, DocumentException {

		Map<String, String> map = new HashMap<String, String>();
		InputStream inputstream = request.getInputStream();
		// 读取输入流
		SAXReader reader = new SAXReader();
		Document doc = reader.read(inputstream);
		// 得到xml根元素
		Element em = doc.getRootElement();
		// 得到所有子元素
		@SuppressWarnings("unchecked")
		List<Element> list = em.elements();
		// 遍历所有子节点
		for (Element e : list)
			map.put(e.getName(), e.getText());
		// 释放资源
		inputstream.close();
		inputstream = null;
		return map;
	}
	
	
	//得到数据返回微信需要的xml文件
		public static String DisposeXml(Map<String, String> map) {
			String MsgType = map.get("MsgType");
			// 开发者微信号
			String FromUserName = map.get("FromUserName");
			// 接收方帐号（收到的OpenID）
			String ToUserName = map.get("ToUserName");

			if (MsgType.equals(WxChatUtils.REQ_MESSAGE_TYPE_EVENT)) {
				String Event = map.get("Event");
				switch (Event) {
				// 订阅
				case WxChatUtils.EVENT_TYPE_SUBSCRIBE:
					return WxChatNews.createText(FromUserName, ToUserName, "感谢关注");
				case WxChatUtils.EVENT_TYPE_UNSUBSCRIBE:
					// 取消订阅
					// 用户接收不到消息
					return null;
				case WxChatUtils.EVENT_TYPE_CLICK:
					// 自定义菜单点击事件
					// 事件KEY值，与创建菜单时的key值对应
					String eventKey = map.get("EventKey");
					// 根据key值判断用户点击的按钮
//					switch (eventKey) {
//					case CreatewxMenu.EVENT_TYPE_dt:
//						break;
//					case CreatewxMenu.EVENT_TYPE_gj:
//						break;
//					case CreatewxMenu.EVENT_TYPE_zj:
//						break;
//					default:
//						return "";
//					}
					return WxChatNews.createText(ToUserName, FromUserName,eventKey);
				default:
//					str += "<MsgType><![CDATA[text]]></MsgType>";
//					str += EventUtil.eventNo(map);
//					break;
					return "";
				}
			} else {
				switch (MsgType) {
				case WxChatUtils.REQ_MESSAGE_TYPE_TEXT:
					
					return WxChatNews.createText(FromUserName,ToUserName , "11111");
				default:
					return WxChatNews.createText(ToUserName, FromUserName, "123123");
				}
			}
		}
	
	
}
