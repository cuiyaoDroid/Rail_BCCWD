package com.xianzhi.service;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.Toast;

import com.tingshuo.tool.L;
import com.xzh.rail.bccwd.R;

public class NotificationService extends BaseRongYunService {
	private ActivityManager mActivityManager;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mActivityManager = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
		registerReceiver(mMasterResetReciever, new IntentFilter(
				Intent.ACTION_SCREEN_OFF));

	}

	// 接收
	BroadcastReceiver mMasterResetReciever = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			//Toast.makeText(NotificationService.this, intent.getAction(),
					//Toast.LENGTH_SHORT).show();
			//L.i("=======mMasterResetReciever============");
			new Handler().postDelayed(new Runnable() {
				
				@Override
				public void run() {
					// TODO Auto-generated method stub
					startNotification();
				}
			}, 3000);
		}
	};
	private void startNotification(){
//		notifyAction(R.drawable.icon_main_safe, "com.xianzhi.baichengtraining",
//				"1", "动态预警", "一条新的预警，发布人：刘义", 4);

		notifyAction(R.drawable.icon_main_emergency,
				"com.xianzhi.baichengtraining", "2",
				"应急预案 2015-01-29 10:54:51",
				"已启动2级应急预案：防洪应急预案\r\n您在 应急领导组 担任 组员\r\n您的任务是:组员 任务2", 2);

		notifyAction(R.drawable.icon_main_train,
				"com.xianzhi.baichengtraining", "3", "职教管理", "新教学计划：职教科 三新培训",
				1);

		notifyAction(R.drawable.icon_main_train,
				"com.xianzhi.baichengtraining", "4", "职教管理", "月度考核，点击开始", 3);
		notifyAction(R.drawable.icon_main_train,
				"com.xianzhi.baichengtraining", "5", "动态预警", "刘义于2015-1-5发布的信息于炳仁已阅。", 5);
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		return START_STICKY;
	}

	private void notifyInsidePackage(Class<?> cls, String id, String title,
			String content) {
		Intent mNotificationIntent = new Intent(getApplicationContext(), cls);
		notifyClient(R.drawable.launch3, id, title, content,
				mNotificationIntent);
	}

	private void notifyAction(int icon, String packagename, String id,
			String title, String content, int type) {
		Intent mNotificationIntent = new Intent(packagename);
		mNotificationIntent.putExtra("type", type);
		notifyClient(icon, id, title, content, mNotificationIntent);
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	public boolean isAppOnForeground() {
		List<RunningTaskInfo> taskInfos = mActivityManager.getRunningTasks(1);
		if (taskInfos.size() > 0
				&& TextUtils.equals(getPackageName(),
						taskInfos.get(0).topActivity.getPackageName())) {
			return true;
		}
		return false;
	}

}
