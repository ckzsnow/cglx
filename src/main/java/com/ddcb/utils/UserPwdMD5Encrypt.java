package com.ddcb.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserPwdMD5Encrypt {

	private static final Logger logger = LoggerFactory
			.getLogger(UserPwdMD5Encrypt.class);
	
	public static String getPasswordByMD5Encrypt(String pwd) {
		byte[] buf = pwd.getBytes();
		MessageDigest md5;
		StringBuilder sb = new StringBuilder();
		try {
			md5 = MessageDigest.getInstance("MD5");
			md5.update(buf);
			byte[] tmp = md5.digest();
			for (byte b : tmp) {
				sb.append(Integer.toHexString(b & 0xff));
			}
		} catch (NoSuchAlgorithmException e) {
			logger.error(e.toString());
		}
		return sb.toString();
	}
	
	public static void main(String[] args) {
		System.out.println(UserPwdMD5Encrypt.getPasswordByMD5Encrypt("123456"));
	}
}
