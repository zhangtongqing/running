/**
 * @author Glan.duanyj
 * @date 2014-05-12
 * @project rest_demo
 */
package com.peipao.qdl.sms.util;


import com.cloopen.rest.sdk.utils.EncryptUtil;
import com.peipao.framework.constant.WebConstants;
import com.peipao.framework.util.PropertiesUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import java.io.ByteArrayInputStream;

public class SmsHttpUtil {

	public static StringBuffer getStringBuffer() throws Exception {
		String server= PropertiesUtil.getProperty(WebConstants.SMS.PROPERTY_FILE, WebConstants.SMS.SERVER);
		
		return new StringBuffer(server);
	}
	
	public static String getSignature(String accountSid, String authToken,String timestamp, EncryptUtil encryptUtil) throws Exception{
		String sig = accountSid + authToken + timestamp;
		String signature = encryptUtil.md5Digest(sig);
		return signature;
	}
	public static HttpResponse get(String cType, String accountSid, String authToken, String timestamp, String url, DefaultHttpClient httpclient, EncryptUtil encryptUtil) throws Exception{
		HttpGet httppost = new HttpGet(url);
		httppost.setHeader("Accept", cType);//
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization",auth);
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}
	@SuppressWarnings("deprecation")
	public static HttpResponse post(String cType, String accountSid, String authToken, String timestamp, String url, DefaultHttpClient httpclient, EncryptUtil encryptUtil, String body) throws Exception{
		HttpPost httppost = new HttpPost(url);
		httppost.setHeader("Accept", cType);
		httppost.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httppost.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httppost.setEntity(requestBody);
        // 执行客户端请求
		HttpResponse response = httpclient.execute(httppost);
		return response;
	}
	public static HttpResponse delete(String cType, String accountSid, String authToken, String timestamp, String url, DefaultHttpClient httpclient, EncryptUtil encryptUtil) throws Exception{
		HttpDelete httpDelete=new HttpDelete(url);
		httpDelete.setHeader("Accept", cType);
		httpDelete.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httpDelete.setHeader("Authorization", auth);
		HttpResponse response = httpclient.execute(httpDelete);
		return response;
	}
	public static HttpResponse put(String cType, String accountSid, String authToken, String timestamp, String url, DefaultHttpClient httpclient, EncryptUtil encryptUtil, String body) throws Exception{
		HttpPut httpPut = new HttpPut(url);
		httpPut.setHeader("Accept", cType);
		httpPut.setHeader("Content-Type", cType+";charset=utf-8");
		String src = accountSid + ":" + timestamp;
		String auth = encryptUtil.base64Encoder(src);
		httpPut.setHeader("Authorization", auth);
		BasicHttpEntity requestBody = new BasicHttpEntity();
        requestBody.setContent(new ByteArrayInputStream(body.getBytes("UTF-8")));
        requestBody.setContentLength(body.getBytes("UTF-8").length);
        httpPut.setEntity(requestBody);
		HttpResponse response = httpclient.execute(httpPut);
		return response;
	}
}
