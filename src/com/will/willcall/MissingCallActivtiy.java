package com.will.willcall;

import java.io.InputStream;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextPaint;
import android.text.format.Time;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;

public class MissingCallActivtiy extends Activity{

	private int PageCount = 0;
	private ArrayList<MissingCallAdapter> mMissingCallAdapter = new ArrayList<MissingCallAdapter>();
	private Button ButtonToLeft = null;
	private Button ButtonToRight = null;
	private int Theme = 0;
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Theme = 1;
		switch(Theme)
		{
			case 0:
			{
				setContentView(R.layout.missing_call_dialog);
			}
				break;
			case 1:
			{
				setContentView(R.layout.missing_call_dialog_win_theme);
			}
				break;
				default:
				{
					setContentView(R.layout.missing_call_dialog);
				}
					break;
		}

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
					if(Theme == 0)
					{
						v.setVisibility(View.GONE);
					}
					else if(Theme == 1)
					{
						startAnimation();
						v.setVisibility(View.INVISIBLE);
					}
					else
					{
						v.setVisibility(View.INVISIBLE);
					}
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
					if(Theme == 0)
					{
						v.setVisibility(View.GONE);
					}
					else if(Theme == 1)
					{
						startAnimation();
						v.setVisibility(View.INVISIBLE);
					}
					else
					{
						v.setVisibility(View.INVISIBLE);
					}
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
		if(Theme == 0)
		{
			long mCallTimer = mMissingCallAdapter.getTime();
			final String incomingNumber = mMissingCallAdapter.getIncomingNumber();
			TextView mcdText_1			= (TextView)findViewById(R.id.mcd_name_1);	
			TextView mcdText_2			= (TextView)findViewById(R.id.mcd_name_2);	
			TextView timeText			= (TextView)findViewById(R.id.mcd_time);
			ImageView reCallBreath		= (ImageView)findViewById(R.id.recall_breath);
			Time mTime = new Time();
			mTime.set(mCallTimer);
			
			String ContactsName = getContactsInfo(incomingNumber);
			
	
			if (ContactsName == null) {
				mcdText_1.setText(incomingNumber);
				mcdText_2.setVisibility(View.GONE);
			} else {
				mcdText_1.setText(ContactsName);
				mcdText_2.setText(incomingNumber);
				mcdText_2.setVisibility(View.VISIBLE);
			}

			timeText.setText(getString(R.string.ringing_Time) + mTime.second
					+ getString(R.string.second));
			timeText.setTextColor(getTimeColor(mTime.second));

			Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
			alpha.setDuration(getBreathTime(mTime.second));
//			reCallBreath.setImageResource(getBreathImage(mTime.second));
			reCallBreath.setAnimation(alpha);
//			Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
//			reCallButton.setAnimation(shake);
			reCallBreath.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	                String phone = incomingNumber;  
//					if (isValid(phone)) {
						Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
						startActivity(callIntent);
//					} else {
//						Toast.makeText(v.getContext(), "非法电话号码", Toast.LENGTH_SHORT).show();
//					}
				}
			});
//			phoneNumberText.setText(ContactsName);
		}
		else if(Theme == 1)
		{
			startAnimation();
			long mCallTimer = mMissingCallAdapter.getTime();
			final String incomingNumber = mMissingCallAdapter.getIncomingNumber();
			TextView mcdText_1			= (TextView)findViewById(R.id.mcd_name_1);	
			TextView mcdText_2			= (TextView)findViewById(R.id.mcd_name_2);	
			TextView timeText			= (TextView)findViewById(R.id.mcd_time);
			ImageView reCallIcon		= (ImageView)findViewById(R.id.btn_recall_icon);
			View reCallButton			= (View)findViewById(R.id.block_view_3);
			View contactDefaultIcon		= (View)findViewById(R.id.default_contact_icon);
			View BlockView_1 			= (View)findViewById(R.id.block_view_1);

			Time mTime = new Time();
			mTime.set(mCallTimer);
			
			String ContactsName = getContactsInfo(incomingNumber);
			Bitmap ContactsPhoto = getContactsPhoto(incomingNumber);

			if (ContactsName == null) {
				mcdText_1.setText(getString(R.string.app_name));
				mcdText_2.setText(incomingNumber);
				mcdText_2.setVisibility(View.VISIBLE);
			} 
			else {
				mcdText_1.setText(ContactsName);
				mcdText_2.setText(incomingNumber);
				mcdText_2.setVisibility(View.VISIBLE);
			}
			if (ContactsPhoto != null)
			{
				BlockView_1.setBackgroundDrawable(new BitmapDrawable(ContactsPhoto));
				contactDefaultIcon.setVisibility(View.INVISIBLE);
			}
			else
			{
				BlockView_1.setBackgroundResource(R.color.block_1);
				contactDefaultIcon.setVisibility(View.VISIBLE);
			}
			reCallIcon.setImageResource(getReCallIcon(mTime.second));
			timeText.setText(mTime.second + "”");
			setFakeBoldText(mcdText_1);
			setFakeBoldText(mcdText_2);
			setFakeBoldText(timeText);
			reCallButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent callIntent = new Intent(Intent.ACTION_CALL, Uri
							.parse("tel:" + incomingNumber));
					startActivity(callIntent);
				}
			});
		}
	}
	private void startAnimation()
	{
		if(Theme != 1)
		{
			return;
		}
		View BlockView_1 			= (View)findViewById(R.id.block_view_1);
		View BlockView_2 			= (View)findViewById(R.id.block_view_2);
		View BlockView_3 			= (View)findViewById(R.id.block_view_3);
		View BlockView_4 			= (View)findViewById(R.id.block_view_4);
		Animation animation_1 = AnimationUtils.loadAnimation(this, R.anim.translate_1);
		BlockView_1.startAnimation(animation_1);
		Animation animation_2 = AnimationUtils.loadAnimation(this, R.anim.translate_2);
		BlockView_2.startAnimation(animation_2);
		Animation animation_3 = AnimationUtils.loadAnimation(this, R.anim.translate_3);
		BlockView_3.startAnimation(animation_3);
		Animation animation_4 = AnimationUtils.loadAnimation(this, R.anim.translate_4);
		BlockView_4.startAnimation(animation_4);

	}
	public void setFakeBoldText(TextView text)
	{
		TextPaint tp = text.getPaint(); 
		tp.setFakeBoldText(true); 
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
			personName = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.PhoneLookup.DISPLAY_NAME));
		}
		phoneCursor.close();
		return personName;
	}
	public Bitmap getContactsPhoto(String address) {
		Bitmap personName = null;
		Uri personUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, address);
		Cursor phoneCursor = getContentResolver().query(
				personUri,
				new String[] { ContactsContract.PhoneLookup._ID}, 
				null, 
				null, 
				null);
		if (phoneCursor.moveToFirst()) {
			long contactId  = phoneCursor.getLong(phoneCursor.getColumnIndex(ContactsContract.PhoneLookup._ID));
			Uri uri =ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI,contactId);
			InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),uri);
			personName = BitmapFactory.decodeStream(input);
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
	public int getReCallIcon(int time)
	{
		int ret = 0;
		if(time<=5)
		{
			ret = R.drawable.recall_poor_win_theme;
		}
		else if(time<=10&&time>5)
		{
			ret = R.drawable.recall_warming_win_theme;
		}
		else if(time>10)
		{
			ret = R.drawable.recall_good_win_theme;
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
