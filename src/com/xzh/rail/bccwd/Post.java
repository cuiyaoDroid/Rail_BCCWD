package com.xzh.rail.bccwd;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

public class Post {
	public String doPost(String url,String[][] nameValues) {
		HttpClient httpclient = HttpsClient.getInstance().getHttpsClient(); 
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
	
	public String readJSON(String urlPath) {
		System.out.println(urlPath);
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
			}else if(statusCode == 403){

			}
		} catch (ClientProtocolException e) {

		}catch(ConnectTimeoutException e){  
		}catch(SocketTimeoutException e){
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
		}
		return builder.toString();

	}
}
