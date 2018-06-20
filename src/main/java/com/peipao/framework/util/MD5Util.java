package com.peipao.framework.util;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author hebo
 *
 */
public class MD5Util {

//	public static String getMD5(String instr) {
//		String s = null;
//		// 用来将字节转换成 16 进制表示的字
//		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
//		try {
//			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
//			md.update(instr.getBytes());
//			byte tmp[] = md.digest();
//			char str[] = new char[16 * 2];
//			int k = 0;
//			for (int i = 0; i < 16; i++) {
//				byte byte0 = tmp[i];
//				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
//				str[k++] = hexDigits[byte0 & 0xf];
//			}
//			s = new String(str); //.toUpperCase()
//		} catch (Exception e) {
//		}
//		return decode(s);
//	}

	public static String getMD5(String string) {
		byte[] hash;
		try {
			hash = MessageDigest.getInstance("MD5").digest(
					string.getBytes("UTF-8"));
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException("Huh, MD5 should be supported?", e);
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException("Huh, UTF-8 should be supported?", e);
		}

		StringBuilder hex = new StringBuilder(hash.length * 2);
		for (byte b : hash) {
			if ((b & 0xFF) < 0x10)
				hex.append("0");
			hex.append(Integer.toHexString(b & 0xFF));
		}
		return hex.toString();
	}

	public static String decode(String p) {
		StringBuffer sb = new StringBuffer();
		sb.append(p.charAt(p.length() - 1));
		sb.append(p.substring(1, 4));
		sb.append(p.charAt(p.length() - 5));
		sb.append(p.substring(5, p.length() - 5));
		sb.append(p.charAt(4));
		sb.append(p.substring(p.length() - 4, p.length() - 1));
		sb.append(p.charAt(0));
		return sb.toString();
	}


}
