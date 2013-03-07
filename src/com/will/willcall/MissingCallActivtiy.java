package com.will.willcall;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

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
		String incomingNumber = mMissingCallAdapter.getIncomingNumber();
		TextView mcdText_1 = (TextView)findViewById(R.id.mcd_name_1);	
		TextView mcdText_2 = (TextView)findViewById(R.id.mcd_name_2);	
		TextView timeText = (TextView)findViewById(R.id.mcd_time);
		
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
