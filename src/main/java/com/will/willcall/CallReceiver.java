package com.will.willcall;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

import java.util.ArrayList;

/**
 * if device start and phone ring ,start the service
 *
 * @author Will
 */
public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        if (action.equals("android.intent.action.BOOT_COMPLETED")) {
            Intent service = new Intent(Intent.ACTION_RUN);
            service.setClass(context, CallService.class);
            context.startService(service);
        }
        if (action.equals("android.intent.action.PHONE_STATE")) {
            String phoneNumber = intent.getStringExtra(TelephonyManager.EXTRA_INCOMING_NUMBER);
            TelephonyManager telephony = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            int state = telephony.getCallState();
            Intent service = new Intent("com.will.willcall.action.PHONE_STATE");
            service.putExtra("callstate", state);
            service.putExtra("phonenumber", phoneNumber);
            service.setClass(context, CallService.class);
            context.startService(service);
        }
    }

    public boolean isPushServiceWork(Context context) {
        ActivityManager myManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        ArrayList<RunningServiceInfo> runningService = (ArrayList<RunningServiceInfo>) myManager
                .getRunningServices(Integer.MAX_VALUE);// 30是最大值
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
