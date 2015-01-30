package com.xzh.rail.bccwd;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.http.conn.ConnectTimeoutException;

import com.xzh.rail.bccwd.entry.App;
import com.xzh.rail.bccwd.entry.User;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnCancelListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

public class Total extends Activity{
	public static Activity activity;
	public static String SDCardRoot = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator;
	public static String RAIL = ".bc_rail";
	Toast toast = null;
	Handler handler = new Handler();
	public static App addApp = null;
	public static User userAdd = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		activity = this;
		isDirExist(RAIL);
	}
	public static void isDirExist(String dir){
		File file = new File(SDCardRoot + dir + File.separator);
		if(!file.exists()){
			file.mkdir();  //如果不存在则创建
		}else{
			return;
		}
	}
	public void showToast(final String msg){
		handler.post(new Runnable() {

			public void run() {
				// TODO Auto-generated method stub
				if(toast == null){
					toast= Toast.makeText(Total.this, msg, Toast.LENGTH_SHORT);
				}else{
					toast.setText(msg);
				}
				toast.setGravity(Gravity.CENTER,0,0);
				toast.show();
			}
		});
	}
	/**
	 * 检查当前网络是否可用
	 * @return	网络是否可用
	 */
	public boolean checkNet(Context acitivity){
		try {
			ConnectivityManager manager = (ConnectivityManager) acitivity   
					.getApplicationContext().getSystemService(   
							Context.CONNECTIVITY_SERVICE);   
			if (manager == null) { 
				return false;   
			}   
			NetworkInfo networkinfo = manager.getActiveNetworkInfo();   
			if (networkinfo == null || !networkinfo.isAvailable()) { 
				return false;   
			}else {
				return true;   
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} 
	}
	/**
	 * 安装方法
	 * @param path
	 */
	public void update(String path) {
		try {
			path = path.replace("file:///", "");
			File file = new File(path);
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.setDataAndType(Uri.fromFile(file),
					"application/vnd.android.package-archive");
			startActivity(intent);
		} catch (NullPointerException e) {
			// TODO: handle exception
			Toast.makeText(this, "包解析失败", 0).show();
		}catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(this, "包解析失败", 0).show();
		}

	}

	private ProgressDialog mProgressDialog; 
	public static final int DIALOG_DOWNLOAD_PROGRESS = 1; 
	protected Dialog onCreateDialog(int id) { 
		switch (id) { 

		case DIALOG_DOWNLOAD_PROGRESS: 
			mProgressDialog = new ProgressDialog(this); 
			mProgressDialog.setMessage("正在下载，请稍候..."); 
			mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL); 
			mProgressDialog.setCancelable(true); 
			mProgressDialog.setCanceledOnTouchOutside(false);
			mProgressDialog.setOnCancelListener(new OnCancelListener() {

				@Override
				public void onCancel(DialogInterface dialog) {
					// TODO Auto-generated method stub
					error = 0;
				}
			});
			mProgressDialog.show(); 
			return mProgressDialog; 
		default: 
			return null; 
		} 
	} 
	int error = 1;
	class DownloadFileAsync extends AsyncTask<String, String, String> { 
		String fileName="";
		OutputStream output = null;
		InputStream input = null;
		@Override 
		protected void onPreExecute() { 
			super.onPreExecute(); 
			try {
				error = 1;
				showDialog(DIALOG_DOWNLOAD_PROGRESS); 
			} catch (Exception e) {
				// TODO: handle exception
			}

		} 

		@Override 
		protected String doInBackground(String... aurl) { 
			int count; 

			try { 
				System.out.println(aurl[0]);
				URL url = new URL(aurl[0]); 
				URLConnection conexion = url.openConnection(); 
				conexion.connect(); 
				conexion.setConnectTimeout(50000);
				conexion.setReadTimeout(100000);
				int lenghtOfFile = conexion.getContentLength(); 

				input = new BufferedInputStream(url.openStream()); 
				fileName = aurl[0].substring(aurl[0].lastIndexOf("/"));
				output = new FileOutputStream(SDCardRoot+RAIL+"/"+fileName); 

				byte data[] = new byte[1024]; 

				long total = 0; 

				while ((count = input.read(data)) != -1) { 
					total += count; 
					publishProgress(""+(int)((total*100)/lenghtOfFile)); 
					output.write(data, 0, count); 
				} 

				output.flush(); 
				output.close(); 
				input.close(); 
			} catch (ConnectTimeoutException e) {
				// TODO: handle exception
				error = 0;
				showToast("网络连接异常,下载已停止");
				File file = new File(SDCardRoot+RAIL+"/"+fileName);
				file.delete();
				try {
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				} catch (Exception e1) {
					// TODO: handle exception
				} 
			}catch (Exception e) { 
				error = 0;
				Log.e("error",e.getMessage().toString()); 
				System.out.println(e.getMessage().toString()); 
				if(!checkNet(activity) || !isConnByHttp()){
					showToast("网络连接异常,下载已停止");
				}else{
					showToast("下载已停止");
				}
				File file = new File(SDCardRoot+RAIL+"/"+fileName);
				file.delete();
				try {
					dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
				} catch (Exception e1) {
					// TODO: handle exception
				} 
			}
			return null; 

		} 
		protected void onProgressUpdate(String... progress) { 
			Log.d("ANDRO_ASYNC",progress[0]); 
			if(error != 0){
				mProgressDialog.setProgress(Integer.parseInt(progress[0])); 
			}else {
				try {
					output.flush();
					output.close(); 
					input.close(); 
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 

				File file = new File(SDCardRoot+RAIL+"/"+fileName);
				file.delete();
			} 
		} 

		@Override 
		protected void onPostExecute(String unused) { 
			try {
				dismissDialog(DIALOG_DOWNLOAD_PROGRESS);
			} catch (Exception e) {
				// TODO: handle exception
			} 
			if(error == 1){
				update(SDCardRoot+RAIL+"/"+fileName);
			}

		} 
	} 

	public boolean isConnByHttp(){
		boolean isConn = false;
		URL url;
		HttpURLConnection conn = null;
		try {
			url = new URL("http://v.sy-railway.xzh-soft.com:8180");
			conn = (HttpURLConnection)url.openConnection();
			conn.setConnectTimeout(1000*5);
			if(conn.getResponseCode()==200){
				isConn = true;
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			conn.disconnect();
		}
		return isConn;
	}
	String codetype(byte[] head) {  
		String type = "";  
		byte[] codehead = new byte[3];  
		System.arraycopy(head, 0, codehead, 0, 3);  
		if(codehead[0] == -1 && codehead[1] == -2) {  
			type = "UTF-16";  
		}  
		else if(codehead[0] == -2 && codehead[1] == -1) {  
			type = "UNICODE";  
		}  
		else if(codehead[0] == -17 && codehead[1] == -69 && codehead[2] == -65) {  
			type = "UTF-8";  
		}  
		else {  
			type = "GB2312";  
		}  
		return type;  
	}


}
