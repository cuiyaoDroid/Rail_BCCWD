package com.xzh.rail.bccwd;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.util.EncodingUtils;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.xianzhi.service.NotificationService;
import com.xzh.rail.bccwd.entry.App;
import com.xzh.rail.bccwd.entry.JsonDataLogin;
import com.xzh.rail.bccwd.entry.User;

public class MainActivity extends Total {
	GridView gridView;
	JsonDataLogin jsonDataLogin;
	User user;
	List<App> apps = new ArrayList<App>();
	DefineProgressDialog progressDialog;
	MainAdapter mainAdapter;
	boolean second=false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		jsonDataLogin = (JsonDataLogin) getIntent().getSerializableExtra("jsonDataLogin");
		user = jsonDataLogin.getUser();
		gridView = (GridView)findViewById(R.id.gridview);
		mainAdapter = new MainAdapter();
		gridView.setAdapter(mainAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				if(arg2==1){
					second=true;
					arg2--;
				}
				getPlugin(apps.get(arg2));
			}
		});
		getData();
		
	}
	void getPlugin(App app){
		int productId = app.getProductId();
		if(getInsert(app.getPackageName())){//已经安装
			if(checkNet(MainActivity.this)){
				try {
					String version = getVersion(app.getProductId());
										float ver = Float.parseFloat(version);//本地版本号
						float serVer = Float.parseFloat(app.getVersion());//服务器端版本号
						if(serVer > ver){//服务器版本大于当前版本，进行更新操作
							showUpdateDialog(app);
						}else {//服务器版本不大于当前版本，打开插件
							openPlugin(app.getProductId(),app.getPackageName(),app.getClassName());
						}
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					showToast("插件未安装，正在下载...");
					if(checkNet(MainActivity.this)){
						addApp = app;
						userAdd = user;
						String downloadUrl = app.getUrl();
						String name = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
//						putVersion(app.getId()+"",app.getVersion());
						new DownloadFileAsync().execute(downloadUrl);
					}
				}
			}else {
				openPlugin(app.getProductId(),app.getPackageName(),app.getClassName());
			}
		}else {//未安装
			showToast("插件未安装，正在下载...");
			if(checkNet(MainActivity.this)){
				 addApp = app;
				 userAdd = user;
				String downloadUrl = app.getUrl();
				String name = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
//				putVersion(app.getId()+"",app.getVersion());
				new DownloadFileAsync().execute(downloadUrl);
			}
		}
	}
	boolean getInsert(String packageName){
		PackageInfo packageInfo;

		try {
			packageInfo = this.getPackageManager().getPackageInfo(
					packageName, 0);
		} catch (NameNotFoundException e) {
			packageInfo = null;
			e.printStackTrace();
		}
		if(packageInfo ==null){
			return false;
		}else{
			return true;
		}
	}
	class MainAdapter extends BaseAdapter{
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return apps.size();
		}

		@Override
		public App getItem(int position) {
			// TODO Auto-generated method stub
			return apps.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			convertView = getLayoutInflater().inflate(R.layout.main_item, null);
			ImageView ItemImg = (ImageView)convertView.findViewById(R.id.ItemImage);
			TextView ItemText = (TextView)convertView.findViewById(R.id.ItemText);
			App app = getItem(position);
			String path = SDCardRoot+RAIL+"/"+Md5.Md5(app.getIco())+".jpg";
			File file = new File(path);
			if(!file.exists()){
				HttpTool.getInstance().saveFile(MainActivity.this,app.getIco(), path);
			}
			Bitmap bitmap = BitmapFactory.decodeFile(path);
			ItemImg.setImageBitmap(bitmap);
			ItemText.setText(apps.get(position).getDisplayName());
			return convertView;
		}

	}

	/**
	 * 获取本地版本号
	 * @param id
	 * @return
	 */
	public String getVersion(int id){
		String txt = "";  
		try {  
			// 文件路径  
			String filename = SDCardRoot+RAIL+"/"+id+".txt"; 
			System.out.println("要读取的"+filename);
			// 文件流读取文件  
			FileInputStream fin = new FileInputStream(filename);  
			// 获得字符长度  
			int length = fin.available();  
			// 创建字节数组  
			byte[] buffer = new byte[length];  
			// 把字节流读入数组中  
			fin.read(buffer);  
			// 关闭文件流  
			fin.close();  
			// 获得编码格式  
			String type = codetype(buffer);  
			// 使用编码格式获得内容  
			txt = EncodingUtils.getString(buffer, type); 
			System.out.println("读取到的"+txt);
			return txt;
		}  
		catch(Exception e) {  
			// TODO: handle exception  
			e.printStackTrace();
		}  
		return "1.0";

	}
	public static void putVersion(String id,String s)
	{
		try 
		{
			FileOutputStream outStream = new FileOutputStream(SDCardRoot+RAIL+"/"+id+".txt",false);
			OutputStreamWriter writer = new OutputStreamWriter(outStream,"gb2312");
			writer.write(s);
			writer.flush();
			writer.close();//记得关闭

			outStream.close();
		} 
		catch (Exception e)
		{
			System.out.println("write to sdcard for error");
		} 
	}

	/**
	 * 更新弹出框
	 * @param force 是否强制更新 0 为非强制更新 1 强制更新
	 * @param id
	 */
	void showUpdateDialog(final App app){
		new AlertDialog.Builder(MainActivity.this)
		.setTitle("更新提示")
		.setMessage("当前版本有最新版本，是否更新？")
		.setNeutralButton("更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//调用下载功能
				String downloadUrl = app.getUrl();
				String name = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
//				putVersion(app.getProductId()+"",app.getVersion());
				new DownloadFileAsync().execute(downloadUrl);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				boolean isForce = Float.parseFloat(app.getLowestVersion()) - Float.parseFloat(getVersion(app.getProductId())) >0;//判断最低版本是否大于当前版本，确定是否需要强制更新
				switch (app.getForceUpdate()) {
				case 0:
					if(!isForce){//非强制更新情况，点击取消进入插件
						openPlugin(app.getProductId(),app.getPackageName(),app.getClassName());
					}
					break;
				case 1:
					break;
				default:
					break;
				}
			}
		}).show();
	}

	/**
	 * 打开插件
	 * @param packageName
	 * @param className
	 */
	public void openPlugin(int productId,String packageName,String className){
		try {
			Intent intent = new Intent(packageName); 
			intent.putExtra("token", user.getToken());
			intent.putExtra("userId", user.getId());
			if(second){
				second=false;
				intent.putExtra("type", 6);
			}
			intent.putExtra("weburl", HttpTool.getInstance().webUrl);
			startActivity(intent);  
		} catch (ActivityNotFoundException e) {
			// TODO: handle exception
			showToast("插件未安装");
		}
	}
	void getData(){
		try {
			progressDialog = new DefineProgressDialog(MainActivity.this, "正在加载");
			progressDialog.show();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					apps = HttpTool.getInstance().getApp();
					handler.sendEmptyMessage(0);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					try {
						progressDialog.dismiss();
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}).start();
	}
	Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				mainAdapter.notifyDataSetChanged();
				break;

			default:
				break;
			}
		}
	};
}
