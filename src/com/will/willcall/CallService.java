package com.will.willcall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.TelephonyManager;

public class CallService extends Service {

    private int CallState = TelephonyManager.CALL_STATE_IDLE;
    private CallTimer mCallTimer = null;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (intent != null) {
            if (intent.getAction() != null) {
                if (intent.getAction().equals("com.will.willcall.action.PHONE_STATE")) {
                    String phoneNumber = intent.getStringExtra("phonenumber");
                    int state = intent.getIntExtra("callstate", -1);
                    CallState = mOnCallStateChanged(state, CallState, phoneNumber);
                }
            }
        }
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {

        Intent service = new Intent(Intent.ACTION_RUN);
        service.setClass(this, CallService.class);
        startService(service);
        super.onDestroy();
    }


    private int mOnCallStateChanged(int newState, int lastState, String incomingNumber) {
        int flag = getSharedPreferences(getPackageName(), MODE_PRIVATE).getInt("state", MainActivity.STATE_CLOSE);
        if (flag == MainActivity.STATE_OPEN) {
            switch (newState) {
                case TelephonyManager.CALL_STATE_IDLE:
                    callStateIDLE(lastState, incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_RINGING:
                    callStateRINGING(lastState, incomingNumber);
                    break;
                case TelephonyManager.CALL_STATE_OFFHOOK:
                    callStateOFFHOOK(lastState, incomingNumber);
                    break;
                default:
                    break;
            }
        }
        return newState;
    }

    /**
     * @param lastState
     * @param incomingNumber this param is not available since 4.4
     */
    private void callStateIDLE(int lastState, final String incomingNumber) {

        if (lastState == TelephonyManager.CALL_STATE_RINGING) {
            final long time = mCallTimer.end();
            new Thread(new Runnable() {

                @Override
                public void run() {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getBaseContext(), MissingCallActivtiy.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("time", time);
                    intent.putExtra("incomingNumber", mCallTimer.getIncomingNumber());
                    startActivity(intent);
                }
            }).start();
        }
    }

    private void callStateRINGING(int lastState, String incomingNumber) {
        mCallTimer = new CallTimer(incomingNumber);
        mCallTimer.start();
    }

    private void callStateOFFHOOK(int lastState, String incomingNumber) {
    }

}