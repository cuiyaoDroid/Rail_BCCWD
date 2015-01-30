package com.xzh.rail.bccwd;

import com.xianzhi.service.NotificationService;

import android.app.Application;
import android.content.Intent;

public class bcApp extends Application{
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		Intent intent=new Intent(getApplicationContext(),NotificationService.class);
		startService(intent);
	}
}
