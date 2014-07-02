package com.will.willcall;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

public class CallService extends Service {

    //	private TelephonyManager teleMgr = null;
//	private Context context = null;
    private int CallState = TelephonyManager.CALL_STATE_IDLE;
    private CallTimer mCallTimer = null;
//	private boolean Flag = false;


    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
//		State = flags;
        super.onStartCommand(intent, flags, startId);
//		TelephonyManager teleMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
//        teleMgr.listen(new mPhoneStateListener(),PhoneStateListener.LISTEN_CALL_STATE);
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
        // TODO Auto-generated method stub

        Intent service = new Intent(Intent.ACTION_RUN);
        service.setClass(this, CallService.class);
        startService(service);
        super.onDestroy();
    }


    class mPhoneStateListener extends PhoneStateListener {
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            // TODO Auto-generated method stub
            super.onCallStateChanged(state, incomingNumber);
            CallState = mOnCallStateChanged(state, CallState, incomingNumber);
        }
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
//    	Toast.makeText(this,"CALL_STATE_IDLE:"+incomingNumber,Toast.LENGTH_SHORT).show();
        if (lastState == TelephonyManager.CALL_STATE_RINGING) {
            final long time = mCallTimer.end();
            new Thread(new Runnable() {

                @Override
                public void run() {
                    // TODO Auto-generated method stub
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

//    	   	Intent intent = new Intent(this,MissingCallActivtiy.class);
//    	   	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//    	   	intent.putExtra("time",mCallTimer.end());
//    	   	intent.putExtra("incomingNumber",incomingNumber);
//    	   	this.startActivity(intent);
//    		Toast.makeText(this,"Time:"+mTime.second,Toast.LENGTH_SHORT).show();
        }
    }

    private void callStateRINGING(int lastState, String incomingNumber) {
//    	Toast.makeText(this,"CALL_STATE_RINGING:"+incomingNumber,Toast.LENGTH_SHORT).show();
//    	if(lastState == TelephonyManager.CALL_STATE_IDLE)
//    	{
        mCallTimer = new CallTimer(incomingNumber);
        mCallTimer.start();
//    	}
    }

    private void callStateOFFHOOK(int lastState, String incomingNumber) {
//    	mCallTimer.clear();
//    	Toast.makeText(this,"CALL_STATE_OFFHOOK:"+incomingNumber,Toast.LENGTH_SHORT).show();
    }

}