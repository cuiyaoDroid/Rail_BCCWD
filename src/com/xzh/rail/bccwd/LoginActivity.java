package com.xzh.rail.bccwd;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import com.xzh.rail.bccwd.entry.JsonDataLogin;
import com.xzh.rail.bccwd.entry.User;

public class LoginActivity extends Total {
	private CheckBox save_check;
	private ImageButton login_btn;
	private EditText usr_edit;
	private EditText pass_edit;
	DefineProgressDialog progressDialog;
	SharedPreferences userInfo;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		save_check = (CheckBox) findViewById(R.id.save_check);
		login_btn = (ImageButton) findViewById(R.id.login_btn);
		usr_edit = (EditText) findViewById(R.id.usr_edit);
		pass_edit = (EditText) findViewById(R.id.pass_edit);
		 
		getShare();
		login_btn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					progressDialog = new DefineProgressDialog(LoginActivity.this);
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
							String userName = usr_edit.getText().toString().trim();
							String password = pass_edit.getText().toString().trim();
							JsonDataLogin jsonDataLogin = HttpTool.getInstance().toLogin(LoginActivity.this,userName, Md5.Md5(password));
							if(jsonDataLogin.getError() != null){
								showToast(jsonDataLogin.getError());
							}else {
								User user = jsonDataLogin.getUser();
								if(user != null){
									showToast("登录成功");
									saveShare(userName, password);
									Intent intent = new Intent(LoginActivity.this, MainActivity.class);
									intent.putExtra("jsonDataLogin", jsonDataLogin);
									startActivity(intent);
									finish();
									overridePendingTransition(0, 0);
								}
							}

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
		});
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		try {
			if(checkNet(LoginActivity.this)){
				Version version = HttpTool.getInstance().getCheck(19);
				float serVer = Float.parseFloat(version.getVersion());//服务器端版本号
				if(serVer > getVersion()){
					showUpdateDialog(version);
				}
			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	public float getVersion(){
		// 获取packagemanager的实例
		PackageManager packageManager = getPackageManager();
		// getPackageName()是你当前类的包名，0代表是获取版本信息
		PackageInfo packInfo = null;
		try {
			packInfo = packageManager.getPackageInfo(getPackageName(),0);
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String version = packInfo.versionName;
		return Float.parseFloat(version);
	}


	/**
	 * 更新弹出框
	 * @param force 是否强制更新 0 为非强制更新 1 强制更新
	 * @param id
	 */
	void showUpdateDialog(final Version version){
		new AlertDialog.Builder(LoginActivity.this)
		.setTitle("更新提示")
		.setMessage("当前版本有最新版本，是否更新？")
		.setNeutralButton("更新", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//调用下载功能
				String downloadUrl = version.getUrl();
				String name = downloadUrl.substring(downloadUrl.lastIndexOf("/")+1);
				new DownloadFileAsync().execute(downloadUrl);
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				//				boolean isForce = Float.parseFloat(version.getLowestVersion()) - getVersion() >0;//判断最低版本是否大于当前版本，确定是否需要强制更新
				switch (version.getForceUpdate()) {
				case 0:
					startActivity(new Intent(LoginActivity.this.getBaseContext(), MainActivity.class));
					finish();
					break;
				case 1:
					System.exit(0);
					finish();
					break;
				default:
					break;
				}
			}
		}).show();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

	}

	void saveShare(String userName,String password){
		userInfo = getSharedPreferences("bc_userInfo", MODE_PRIVATE);
		//存入数据
		Editor editor = userInfo.edit();
		editor.putString("userName", userName);

		if(save_check.isChecked()){
			editor.putString("password", password);
			editor.putInt("check", 1);
		}else{
			editor.putString("password", "");
			editor.putInt("check", 0);
		}
		editor.commit();
	}
	void getShare(){
		userInfo = getSharedPreferences("bc_userInfo", MODE_PRIVATE);
		String userName = userInfo.getString("userName","");
		String password = userInfo.getString("password", "");
		int check = userInfo.getInt("check", 0);
		usr_edit.setText(userName);
		pass_edit.setText(password);
		save_check.setChecked(check == 1 ? true : false);
	}
}
