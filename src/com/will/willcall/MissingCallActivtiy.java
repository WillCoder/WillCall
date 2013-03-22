package com.will.willcall;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.analytics.tracking.android.EasyTracker;

public class MissingCallActivtiy extends Activity{

//	private long mCallTimer = -1;
//	private String incomingNumber = null;
	public int PageCount = 0;
	public ArrayList<MissingCallAdapter> mMissingCallAdapter = new ArrayList<MissingCallAdapter>();
	Button ButtonToLeft = null;
	Button ButtonToRight = null;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.missing_call_dialog);
		long mCallTimer = getIntent().getLongExtra("time", -1);
		String incomingNumber = getIntent().getStringExtra("incomingNumber");
//		mMissingCallAdapter = new ArrayList<MissingCallAdapter>();
		mMissingCallAdapter.add(new MissingCallAdapter(mCallTimer, incomingNumber));
		setMissingInfo(mMissingCallAdapter.get(PageCount));
//		TextView mcdText_1 = (TextView)findViewById(R.id.mcd_name_1);	
//		TextView mcdText_2 = (TextView)findViewById(R.id.mcd_name_2);	
//		TextView timeText = (TextView)findViewById(R.id.mcd_time);
//		
//		Time mTime = new Time();
//		mTime.set(mCallTimer);
//		
//		String ContactsName = getContactsInfo(incomingNumber);
//		if(ContactsName==null)
//		{
//			mcdText_1.setText(incomingNumber);
//			mcdText_2.setVisibility(View.GONE);
//		}
//		else
//		{
//			mcdText_1.setText(ContactsName);
//			mcdText_2.setText(incomingNumber);
//			mcdText_2.setVisibility(View.VISIBLE);
//		}
////		phoneNumberText.setText(ContactsName);
//		timeText.setText("响铃时长:"+mTime.second +"秒");
//		timeText.setTextColor(getTimeColor(mTime.second));
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		long mCallTimer = intent.getLongExtra("time", -1);
		String incomingNumber = intent.getStringExtra("incomingNumber");
		
		mMissingCallAdapter.add(new MissingCallAdapter(mCallTimer, incomingNumber));
		ButtonToLeft = (Button)findViewById(R.id.btn_to_left);
		ButtonToRight = (Button)findViewById(R.id.btn_to_right);
		ButtonToRight.setVisibility(View.VISIBLE);
		ButtonToRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PageCount++;
				ButtonToLeft.setVisibility(View.VISIBLE);
				if(PageCount==mMissingCallAdapter.size()-1)
				{
					v.setVisibility(View.GONE);
				}
				else
				{

				}
				setMissingInfo(mMissingCallAdapter.get(PageCount));
			}
		});
//		ButtonToLeft.setVisibility(View.VISIBLE);
		ButtonToLeft.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PageCount--;
				ButtonToRight.setVisibility(View.VISIBLE);
				if(PageCount==0)
				{
					v.setVisibility(View.GONE);
				}
				else
				{

				}
				setMissingInfo(mMissingCallAdapter.get(PageCount));
			}
		});
	}
	public void setMissingInfo(MissingCallAdapter mMissingCallAdapter)
	{
		long mCallTimer = mMissingCallAdapter.getTime();
		final String incomingNumber = mMissingCallAdapter.getIncomingNumber();
		TextView mcdText_1			= (TextView)findViewById(R.id.mcd_name_1);	
		TextView mcdText_2			= (TextView)findViewById(R.id.mcd_name_2);	
		TextView timeText			= (TextView)findViewById(R.id.mcd_time);
//		ImageButton reCallButton	= (ImageButton)findViewById(R.id.btn_recall);
		ImageView reCallBreath		= (ImageView)findViewById(R.id.recall_breath);
		Time mTime = new Time();
		mTime.set(mCallTimer);
		
		String ContactsName = getContactsInfo(incomingNumber);
		if(ContactsName==null)
		{
			mcdText_1.setText(incomingNumber);
			mcdText_2.setVisibility(View.GONE);
		}
		else
		{
			mcdText_1.setText(ContactsName);
			mcdText_2.setText(incomingNumber);
			mcdText_2.setVisibility(View.VISIBLE);
		}
		Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
		alpha.setDuration(getBreathTime(mTime.second));
//		reCallBreath.setImageResource(getBreathImage(mTime.second));
		reCallBreath.setAnimation(alpha);
//		Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//		reCallButton.setAnimation(shake);
		reCallBreath.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
                String phone = incomingNumber;  
//				if (isValid(phone)) {
					Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
					startActivity(callIntent);
//				} else {
//					Toast.makeText(v.getContext(), "非法电话号码", Toast.LENGTH_SHORT).show();
//				}
			}
		});
//		phoneNumberText.setText(ContactsName);
		timeText.setText(getString(R.string.ringing_Time)+mTime.second +getString(R.string.second));
		timeText.setTextColor(getTimeColor(mTime.second));
	}

	public String getContactsInfo(String address) {
		String personName = null;
		Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor phoneCursor = getContentResolver().query(
				personUri,
				new String[] { ContactsContract.PhoneLookup.DISPLAY_NAME }, 
				null, 
				null, 
				null);
		if (phoneCursor.moveToFirst()) {
			int indexPersonId = phoneCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME);
			personName = phoneCursor.getString(indexPersonId);
		}
		phoneCursor.close();
		return personName;
	}
	public int getBreathImage(int time)
	{
		int ret = 0;
		if(time<=5)
		{
			ret = R.drawable.recall_breath_red;
		}
		else if(time<=10&&time>5)
		{
			ret = R.drawable.recall_breath_white;
		}
		else if(time>10)
		{
			ret = R.drawable.recall_breath_green;
		}
		return ret;
	}
	public int getTimeColor(int time)
	{
		int ret = 0;
		if(time<=5)
		{
			ret = Color.parseColor("#FF4444");
		}
		else if(time<=10&&time>5)
		{
			ret = Color.parseColor("#FFBB33");
		}
		else if(time>10)
		{
			ret = Color.parseColor("#99CC00");
		}
		return ret;
	}
	public long getBreathTime(int time)
	{
		long ret = 0;
		if(time<=5)
		{
			ret = 900;
		}
		else if(time<=10&&time>5)
		{
			ret = 1000;
		}
		else if(time>10)
		{
			ret = 1100;
		}
		return ret;
	}
//    private boolean isValid(String input){  
//        boolean flag = true;  
//        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";  
//        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";          
//        // 创建Pattern  
//        Pattern pattern = Pattern.compile(expression);  
//        // 将Pattern以参数传入Matcher作Regular expression  
//        Matcher matcher = pattern.matcher(input);  
//        Pattern pattern2 = Pattern.compile(expression2);  
//        Matcher matcher2 = pattern2.matcher(input);  
//        if(matcher.matches() || matcher2.matches()){  
//            flag = true;  
//        }else{  
//            flag = false;  
//        }  
//        return flag;  
//    } 

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
	
}
