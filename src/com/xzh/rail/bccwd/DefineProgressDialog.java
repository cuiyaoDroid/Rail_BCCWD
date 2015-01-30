package com.xzh.rail.bccwd;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;



public class DefineProgressDialog extends ProgressDialog{
	private String message;
	private TextView define_progress_msg;
	public DefineProgressDialog(Context context) {
		super(context);
		message = "正在登录...";
		setCanceledOnTouchOutside(false);
		setCancelable(true);
		// TODO Auto-generated constructor stub
	}
	public DefineProgressDialog(Context context,String message){
		super(context);
		this.message = message;
		
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.define_progress_dialog);
		define_progress_msg = (TextView) findViewById(R.id.define_progress_msg);
		define_progress_msg.setText(message);
	}
	
}

