package com.peipao.framework.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class HttpClientUtil {

	public static <T> T getResponse(String jsonString, String elementKey, Class<T> elementClass) throws Exception {

		JsonUtil mapper = new JsonUtil();

		return mapper.fromJson(mapper.toJson(getResponse(jsonString).get(elementKey)), elementClass);
	}

	public static Map<String, String> getResponse(String jsonString) throws Exception {

		JsonUtil mapper = new JsonUtil();
		Map<String, String> jsonObject = mapper.fromJson(jsonString, Map.class);

		Object result = jsonObject.get("result");
		if(result != null ){
			if(!result.equals(0))
				throw new Exception(jsonObject.get("reason"));
		}

		return jsonObject;
	}

	public static String post(String url, Map<String, String> params) {


		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		HttpPost post = postForm(url, params);

		body = invoke(httpclient, post);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	public static String get(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient();
		String body = null;

		HttpGet get = new HttpGet(url);
		body = invoke(httpclient, get);

		httpclient.getConnectionManager().shutdown();

		return body;
	}

	private static String invoke(DefaultHttpClient httpclient, HttpUriRequest httpost) {

		HttpResponse response = sendRequest(httpclient, httpost);
		String body = paseResponse(response);

		return body;
	}

	private static String paseResponse(HttpResponse response) {
		HttpEntity entity = response.getEntity();

		String charset = EntityUtils.getContentCharSet(entity);

		String body = null;
		try {
			body = EntityUtils.toString(entity);
		} catch (ParseException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return body;
	}

	private static HttpResponse sendRequest(DefaultHttpClient httpclient, HttpUriRequest httpost) {
		HttpResponse response = null;

		try {
			response = httpclient.execute(httpost);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return response;
	}

	private static HttpPost postForm(String url, Map<String, String> params) {

		HttpPost httpost = new HttpPost(url);
		List<NameValuePair> nvps = new ArrayList<NameValuePair>();

		Set<String> keySet = params.keySet();
		for (String key : keySet) {
			nvps.add(new BasicNameValuePair(key, params.get(key)));
		}

		try {
			httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		return httpost;
	}

	/*
	 * public String postFile(String url,String filePath) throws
	 * ClientProtocolException, IOException { FileBody fileBody = null;
	 * DefaultHttpClient httpclient = new DefaultHttpClient(); HttpPost httppost
	 * = new HttpPost(url); File file = new File(filePath); if (file != null) {
	 * fileBody = new FileBody(file); }
	 * 
	 * StringBody usernameStringBody = new StringBody(username); StringBody
	 * md5orsha1StringBody = new StringBody(md5orsha1);
	 * 
	 * MultipartEntity reqEntity = new MultipartEntity();
	 * 
	 * reqEntity.addPart(KEY_USERNAME, usernameStringBody);
	 * reqEntity.addPart(KEY_MD5ORSHA1, md5orsha1StringBody);
	 * reqEntity.addPart(KEY_FILE, fileBody);
	 * 
	 * httppost.setEntity(reqEntity); System.out.println("0000000000000" +
	 * httppost.getRequestLine()); HttpResponse response =
	 * httpclient.execute(httppost); System.out.println("statusCode is " +
	 * response.getStatusLine().getStatusCode()); HttpEntity resEntity =
	 * response.getEntity();
	 * System.out.println("----------------------------------------");
	 * System.out.println(response.getStatusLine()); if (resEntity != null) {
	 * System.out.println("-----***" + resEntity.getContentLength());
	 * System.out.println("-----" + resEntity.getContentType()); InputStream in
	 * = resEntity.getContent(); String returnValue = inputStream2String(in);
	 * System.out.println("returnValue:" + returnValue); return returnValue; }
	 * if (resEntity != null) { resEntity.consumeContent(); } return null; }
	 */
}
