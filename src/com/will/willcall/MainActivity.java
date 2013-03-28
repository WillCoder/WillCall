package com.will.willcall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;

public class MainActivity extends Activity {

	public int Switch = -1;
	public static final int STATE_OPEN = 1;
	public static final int STATE_CLOSE = 0;
	
	TextView mTextView = null;
	
    @SuppressWarnings("deprecation")
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main); 
    /************************ AdMod add *********************************/
        AdView adView = (AdView)this.findViewById(R.id.adView);
        AdRequest mAdRequest = new AdRequest();
        mAdRequest.setTesting(false);
        adView.loadAd(mAdRequest);
    /************************ AdMod add *********************************/
        Switch = getSharedPreferences(getPackageName(),MODE_PRIVATE).getInt("state",STATE_CLOSE);
        
	   	Intent service = new Intent(Intent.ACTION_RUN);
	   	service.setClass(this, CallService.class); 
	   	startService(service);
	   	mTextView = (TextView)findViewById(R.id.state_textView);
		TextView versionText = (TextView)findViewById(R.id.about_version);
		versionText.setText(getAppVersionName(this));
	   	Button mButton = (Button)findViewById(R.id.button);
	   	Button mTestButton = (Button)findViewById(R.id.test_button);
	   	mTestButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),MissingCallActivtiy.class);
			   	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   	intent.putExtra("time",Long.valueOf(3000));
			   	intent.putExtra("incomingNumber","13669290819");
			   	v.getContext().startActivity(intent);
			   	
//			   	Intent intent = new Intent(this,MissingCallFragmentActivtiy.class);
			   	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   	intent.putExtra("time",Long.valueOf(8000));
			   	intent.putExtra("incomingNumber","2999999999");
			   	v.getContext().startActivity(intent);
			   	
			   	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   	intent.putExtra("time",Long.valueOf(23000));
			   	intent.putExtra("incomingNumber","3999999999");
			   	v.getContext().startActivity(intent);
			   	
			   	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			   	intent.putExtra("time",Long.valueOf(34000));
			   	intent.putExtra("incomingNumber","4999999999");
			   	v.getContext().startActivity(intent);
			}
		});
//	   	switchTextView = (TextView)findViewById(R.id.textView1);
	   	setSwitchText(mTextView,Switch);
	   	mButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Switch = setSwitchText(v,Switch);
		    	if(Switch==STATE_CLOSE)
		    	{
		    		Switch = STATE_OPEN;
		    		setSwitchText(mTextView,Switch);
		    	}
		    	else if(Switch==STATE_OPEN)
		    	{
		    		Switch = STATE_CLOSE;
		    		setSwitchText(mTextView,Switch);
		    	}
			}
		}); 
    }
    public void setSwitchText(View v,int Switch)
    {
    	if(Switch==STATE_CLOSE)
    	{
    		((TextView) v).setText(v.getContext().getString(R.string.already_close));

    	}
    	else if(Switch==STATE_OPEN)
    	{
    		((TextView) v).setText(v.getContext().getString(R.string.already_open));

    	}
		SharedPreferences.Editor editor = getSharedPreferences(getPackageName(),MODE_PRIVATE).edit();
		editor.putInt("state", Switch);
		editor.commit();
    }
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		EasyTracker.getInstance().activityStart(this);
	}
	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		EasyTracker.getInstance().activityStop(this);
	}
	public static String getAppVersionName(Context context) {  
	    String versionName = "";  
	    try {  
	        // Get the package info  
	        PackageManager pm = context.getPackageManager();  
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);  
	        versionName = pi.versionName;  
	        if (TextUtils.isEmpty(versionName)) {  
	            return "";  
	        }  
	    } catch (Exception e) {  
//	        Log.e(THIS_FILE, "Exception", e);  
	    }  
	    return versionName;  
	}
}
