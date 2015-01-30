package com.xzh.rail.bccwd;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;


import android.app.Activity;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.xzh.rail.bccwd.entry.App;
import com.xzh.rail.bccwd.entry.JsonDataApp;
import com.xzh.rail.bccwd.entry.JsonDataLogin;

public class HttpTool {
	public String webUrl = "http://115.28.4.107:8280";
	private static HttpTool httpTool = null;
	DefineProgressDialog progressDialog;
	public static HttpTool getInstance(){
		if(httpTool == null){
			httpTool = new HttpTool();
		}
		return httpTool;
	}
	public String doPost(String url,String[][] nameValues) {
		HttpClient httpclient = new DefaultHttpClient(); 
		String returnStr = "";
		HttpContext context = new BasicHttpContext();
		HttpEntity entity = null;
		HttpPost httppost = new HttpPost(url);
		try {
			List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(); 
			//Your DATA 
			for (int i = 0; i < nameValues.length; i++) {
				System.out.println(nameValues[i][0]+"----"+nameValues[i][1]);
				if(!"".equals(nameValues[i][1])){
					
					nameValuePairs.add(new BasicNameValuePair(nameValues[i][0], nameValues[i][1])); 
				}
			}
			httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,HTTP.UTF_8)); 

			HttpResponse response = httpclient.execute(httppost, context);
			if(response.getStatusLine().getStatusCode() == 403){

			}else {
				entity = response.getEntity();
				if (entity != null) {
					BufferedReader reader = new BufferedReader(
							new InputStreamReader(entity.getContent(), "UTF-8"));
					String line = null;
					while ((line = reader.readLine()) != null) {
						returnStr += line;
					}
					if (entity != null) {
						entity.consumeContent();
					}
					// 释放资源给manger
				}
				httppost.abort();
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		System.out.println("-------returnstr:"+returnStr);
		return returnStr;
	}
	/**
	 * 读取json数据
	 * @param urlPath
	 * @return
	 */
	public static String readJSON(String urlPath) {
		StringBuilder builder = new StringBuilder();
		HttpClient client = HttpsClient.getInstance().getHttpsClient();
		HttpGet httpGet = new HttpGet(urlPath);

		try {

			HttpResponse response = client.execute(httpGet);

			StatusLine statusLine = response.getStatusLine();

			int statusCode = statusLine.getStatusCode();

			if (statusCode == 200) {

				HttpEntity entity = response.getEntity();

				InputStream content = entity.getContent();

				BufferedReader reader = new BufferedReader(

						new InputStreamReader(content));

				String line;

				while ((line = reader.readLine()) != null) {

					builder.append(line);
				}
				System.out.println(builder.toString());
			}else if (statusCode == 403){

			}
		} catch (ClientProtocolException e) {

		} catch (IOException e) {
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
		}
		return builder.toString();

	}
	/**
	 * 登录功能
	 * @param userName
	 * @param password
	 */
	JsonDataLogin toLogin(Activity activity,String userName,String password){
		JsonDataLogin jsonDataLogin = null;
		String url = webUrl+"/login.json";
		System.out.println(url);
//		HttpsClient.getInstance().init(activity);
		String [][] nameValues = {
				{"loginName",userName},
				{"password",password}
		};
		String json = doPost(url, nameValues);
		if(!"".equals(json)){
			try {
				jsonDataLogin = JSON.parseObject(json, JsonDataLogin.class);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}catch (Exception e) {
				// TODO: handle exception
			}
		}
		return jsonDataLogin;
	}
	/**
	 * 获取更新信息
	 * @param id
	 */
	Version getCheck(int id){
		String url = webUrl+"/app/getByProductId.json?productId="+id;
		try {
			String json = readJSON(url);
			if(json != null && !"".equals(json)){
				VersionInfo versionInfo = JSON.parseObject(json, VersionInfo.class);
				if(versionInfo != null){
					return versionInfo.getVersionInfo();
				}
			}
		} catch (JSONException e) {
			// TODO: handle exception
		}catch (NullPointerException e) {
			// TODO: handle exception
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return null;
	}
    List<App> getApp(){
    	String url = webUrl+"/app/list.json";
    	String json = readJSON(url);
    	JsonDataApp jsonDataApp = JSON.parseObject(json,JsonDataApp.class);
    	return jsonDataApp.getAppList();
    }
	public String saveFile(Activity activity,String url, String fileName) {   
		HttpsClient.getInstance().init(activity);
		HttpClient httpClient = HttpsClient.getInstance().getHttpsClient();   
		HttpGet get = new HttpGet(webUrl+url);   
		System.out.println(webUrl+url);
		try {   
			ResponseHandler<byte[]> handler = new ResponseHandler<byte[]>() {   
				public byte[] handleResponse(HttpResponse response)   
						throws ClientProtocolException, IOException {   
					HttpEntity entity = response.getEntity();   
					if (entity != null) {   
						return EntityUtils.toByteArray(entity);   
					} else {   
						return null;   
					}   
				}   
			};   

			byte[] charts = httpClient.execute(get, handler);   
			FileOutputStream out = new FileOutputStream(fileName);   
			out.write(charts);   
			out.close();   

		} catch (Exception e) {   
			e.printStackTrace();   
		} finally {   
			httpClient.getConnectionManager().shutdown();   
		} 
		System.out.println(fileName);
		return fileName;
	}  
	
}
