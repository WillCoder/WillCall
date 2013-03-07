package com.will.willcall;

import java.util.ArrayList;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class CallReceiver extends BroadcastReceiver{
	
	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub
		String action = intent.getAction();
	    if(action.equals("android.intent.action.BOOT_COMPLETED"))//与receiver的action android:name保持一致
	    {
		   	Intent service = new Intent(Intent.ACTION_RUN);
		   	service.setClass(context, CallService.class); 
		   	context.startService(service);
	    }
		if(action.equals("android.intent.action.PHONE_STATE"))
		{
			if(!isPushServiceWork(context))
			{
				String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
				TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
				int state = telephony.getCallState();
//				this.abortBroadcast();W
			   	Intent service = new Intent("com.will.willcall.action.PHONE_STATE");
			   	service.putExtra("callstate",state);
			   	service.putExtra("phonenumber",phoneNumber);
			   	service.setClass(context, CallService.class); 
			   	context.startService(service);
//				switch (state) {
//				case TelephonyManager.CALL_STATE_RINGING:
//					callStateRINGING(phoneNumber);
////					Log.i(TAG, "[Broadcast]等待接电话=" + phoneNumber);
//					break;
//				case TelephonyManager.CALL_STATE_IDLE:
////					Log.i(TAG, "[Broadcast]电话挂断=" + phoneNumber);
//					break;
//				case TelephonyManager.CALL_STATE_OFFHOOK:
////					Log.i(TAG, "[Broadcast]通话中=" + phoneNumber);
//					break;
//				}	
			}
		}
	}

	// 判断service是否在运行
	public boolean isPushServiceWork(Context context) {
		ActivityManager myManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
				.getRunningServices(30);// 30是最大值
		for (int i = 0; i < runningService.size(); i++) {
			if (runningService.get(i).service
					.getClassName()
					.toString()
					.equals("com.will.willcall.CallService")) {
				return true;
			}
		}
		return false;
	}
}
