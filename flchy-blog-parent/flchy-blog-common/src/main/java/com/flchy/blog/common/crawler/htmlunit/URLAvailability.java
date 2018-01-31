package com.flchy.blog.common.crawler.htmlunit;
import java.net.HttpURLConnection;  
import java.net.URL;  
import org.apache.log4j.Logger;  
public class URLAvailability {
	private static Logger logger = Logger.getLogger(URLAvailability.class);
	private static URL urlStr;
	private static HttpURLConnection connection;
	private static int state = -1;
	private static String succ;

	/**
	 * 功能描述 : 检测当前URL是否可连接或是否有效, 最多连接网络 5 次, 如果 5 次都不成功说明该地址不存在或视为无效地址.
	 * 
	 * @param url
	 *            指定URL网络地址
	 * 
	 * @return String
	 */
	 synchronized String isConnect(String url) {
		int counts = 0;
		succ = null;
		if (url == null || url.length() <= 0) {
			return succ;
		}
		while (counts < 5) {
			try {
				urlStr = new URL(url);
				connection = (HttpURLConnection) urlStr.openConnection();
				state = connection.getResponseCode();
				if (state == 200) {
					succ = connection.getURL().toString();
				}
				break;
			} catch (Exception ex) {
				counts++;
				logger.info("loop :" + counts);
				continue;
			}
		}
		return succ;
	}
	public static void main(String[] args) {
		URLAvailability availability=new URLAvailability();
		String connect = availability.isConnect("http://img.alicdn.com/bao/uploaded/i1/2091294084/TB13KDvcxrI8KJjy0FpXXb5hVXa_!!0-item_pic.jpg");
		System.out.println(connect);
	}
}
