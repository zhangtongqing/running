package com.peipao.framework.util;

import java.io.InputStream;
import java.util.Properties;

/**
 *@author: dongll
 *@date: 2015-8-14下午01:51:16
 *@version:
 *@description：
 */
public class PropertiesUtil {
	
	public static String url;
	
	public static String getProperty(String fileName, String propertyName){

		Properties pros = new Properties();
		try{
			InputStream in = PropertiesUtil.class.getResourceAsStream("/" + fileName);
			pros.load(in);
		}catch(Exception e){
			e.printStackTrace();
		}

		return (String) pros.get(propertyName);
		
	}
}

