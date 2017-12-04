package com.tools.plugin.wxchat;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.tools.plugin.wxchat.holder.PropertiesHolder;

public class AccessUtil {
	/**
	 * 验证消息的确来自微信服务器
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
}
