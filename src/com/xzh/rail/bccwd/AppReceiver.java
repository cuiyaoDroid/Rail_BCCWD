package com.xzh.rail.bccwd;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AppReceiver extends BroadcastReceiver{
	private static final int PACKAGE_NAME_START_INDEX = 8;
	@Override
	public void onReceive(Context context, Intent intent)
	{
		if(intent == null)
		{
			return;
		}

		if(intent.getAction().equals(Intent.ACTION_PACKAGE_ADDED) 
				|| intent.getAction().equals(Intent.ACTION_PACKAGE_REMOVED)){
			String data = intent.getDataString();

			if(data == null || data.length() <= PACKAGE_NAME_START_INDEX)
			{
				return;
			}

			String packageName = data.substring(PACKAGE_NAME_START_INDEX);
			System.out.println(packageName);
			try {
				if(packageName.equals(Total.addApp.getPackageName()) ){
					Intent newIntent = new Intent(packageName);
					newIntent.putExtra("token", Total.userAdd.getToken());
					newIntent.putExtra("userId",Total.userAdd.getId());
					newIntent.putExtra("weburl", HttpTool.getInstance().webUrl);
					newIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);   
					context.startActivity(newIntent);   
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

}

