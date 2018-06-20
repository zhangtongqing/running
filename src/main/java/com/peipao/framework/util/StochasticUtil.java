package com.peipao.framework.util;

import java.util.Random;
import java.util.UUID;

/**
 *@author: dongll
 *@date: 2015-8-15上午09:50:03
 *@version:
 *@description：生成随机数字/字符串/uuid等工具
 */
public class StochasticUtil {

	private static final long LIMIT = 10000000000L;
	private static long last = 0;


	/**
	 * 随机整数
	 * @param num
	 * @return
	 */
	public static int getRandom(int num){
		Random random = new Random();
		return random.nextInt(num);
	}
	
	
	/**
	 * 随机字母
	 * @param num
	 * @return
	 */
	public static String getRandom(double num){
		String pool = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM";
		String str = "";
		for (long i = 0; i < num; i++) {
			int j = new Random().nextInt(pool.length());
			str += pool.charAt(j);
		}
		return str;
	}
	
	
	/**
	 * 生成随机UUID
	 * @return
	 */
	public static String getUUID(){
		UUID uuid = UUID.randomUUID();
		return uuid.toString().replaceAll("-", "");
	}
	
	
	/**
	 * 根据字符串生成固定uuid
	 * @param name
	 * @return
	 */
	public static String getUUID(String name){
		UUID uuid=UUID.nameUUIDFromBytes(name.getBytes());
	    return uuid.toString().replace("-", "");
	}

	public static String getLongID() {
		// 10 digits.
		long id = System.currentTimeMillis() % LIMIT;
		if ( id <= last ) {
			id = (last + 1) % LIMIT;
		}
		last = id;

		return Long.toString(last);
	}
	
	
	public static void main(String args[]){
		System.out.println(StochasticUtil.getLongID());
	}
	
}
