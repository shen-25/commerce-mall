package com.zengshen.common.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	/**
	 * 对字符串进行md5加密
	 */
	public static String getMD5Str(String strValue){
		MessageDigest md5 = null;
		try {
			md5 = MessageDigest.getInstance("MD5");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		assert md5 != null;
		return Base64.encodeBase64String(md5.digest(strValue.getBytes()));
	}

	public static void main(String[] args) {
		try {
			String md5 = getMD5Str("123456");
			System.out.println(md5);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
