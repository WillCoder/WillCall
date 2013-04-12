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
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextPaint;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.analytics.tracking.android.EasyTracker;
/**
 * 
 * @author Will
 *	create at 2013/3
 */
public class MissingCallActivtiy extends Activity{

	private class PageAction{
		public final static int PAGE_UP = 0;
		public final static int PAGE_DOWN = 1;
		public final static int PAGE_NOTING = 1;
	}
	private int PageCount = 0;
	private ArrayList<MissingCallAdapter> mMissingCallAdapter = new ArrayList<MissingCallAdapter>();
	private View ButtonToLeft = null;
	private View ButtonToRight = null;
	private int Theme = ThemeType.CLASSIC_THEME;
	
	private Typeface tfGulim = null;
	private Typeface tfClockopia = null;
//	private Typeface tfAndroidClock = null;
	
	public class ThemeType{
		public static final int CLASSIC_THEME = 0;
		public static final int WINDOWS_THEME = 1;
		public static final int ANDROID_THEME = 2;
		public static final int IPHONE_THEME = 3;
	}
	
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		Theme = getSharedPreferences(getPackageName(),MODE_PRIVATE).getInt("theme",ThemeType.CLASSIC_THEME);;
		if(Theme == ThemeType.CLASSIC_THEME)
		{
			setContentView(R.layout.missing_call_dialog);
		}
		else if(Theme == ThemeType.WINDOWS_THEME)
		{
			setContentView(R.layout.missing_call_dialog_win_theme);	
		}
		else if(Theme == ThemeType.ANDROID_THEME)
		{
			tfGulim 		= Typeface.createFromAsset(getAssets(),"fonts/gulim.ttc");
			tfClockopia 	= Typeface.createFromAsset(getAssets(),"fonts/Clockopia.ttf");
//			tfAndroidClock 	= Typeface.createFromAsset(getAssets(),"fonts/AndroidClock.ttf");
			setContentView(R.layout.missing_call_dialog_android_theme);
		}
		else if(Theme == ThemeType.IPHONE_THEME)
		{
			setContentView(R.layout.missing_call_dialog);
		}

		long mCallTimer = getIntent().getLongExtra("time", -1);
		String incomingNumber = getIntent().getStringExtra("incomingNumber");
//		mMissingCallAdapter = new ArrayList<MissingCallAdapter>();
		mMissingCallAdapter.add(new MissingCallAdapter(mCallTimer, incomingNumber));
		setMissingInfo(mMissingCallAdapter.get(PageCount), PageAction.PAGE_NOTING);
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
/*
 * If this Activity is called twice, onNewIntent work(non-Javadoc)
 * @see android.app.Activity#onNewIntent(android.content.Intent)
 */
	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		super.onNewIntent(intent);
		
		long mCallTimer = intent.getLongExtra("time", -1);
		String incomingNumber = intent.getStringExtra("incomingNumber");
		
		mMissingCallAdapter.add(new MissingCallAdapter(mCallTimer, incomingNumber));
		ButtonToLeft = (View)findViewById(R.id.btn_to_left);
		ButtonToRight = (View)findViewById(R.id.btn_to_right);
		ButtonToRight.setVisibility(View.VISIBLE);
		ButtonToRight.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PageCount++;
				ButtonToLeft.setVisibility(View.VISIBLE);
				if(PageCount==mMissingCallAdapter.size()-1)
				{
					if(Theme == ThemeType.CLASSIC_THEME)
					{
						v.setVisibility(View.GONE);
					}
					else if(Theme == ThemeType.WINDOWS_THEME)
					{
						v.setVisibility(View.INVISIBLE);
					}
					else if(Theme == ThemeType.ANDROID_THEME)
					{
						findViewById(R.id.page_divider).setVisibility(View.GONE);
						v.setVisibility(View.GONE);
					}
					else if(Theme == ThemeType.IPHONE_THEME)
					{
						v.setVisibility(View.GONE);
					}
				}
				else
				{
					if(Theme == ThemeType.ANDROID_THEME)
					{
						findViewById(R.id.page_divider).setVisibility(View.VISIBLE);
					}
				}
				setMissingInfo(mMissingCallAdapter.get(PageCount),PageAction.PAGE_DOWN);
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
					if(Theme == ThemeType.CLASSIC_THEME)
					{
						v.setVisibility(View.GONE);
					}
					else if(Theme == ThemeType.WINDOWS_THEME)
					{
						v.setVisibility(View.INVISIBLE);
					}
					else if(Theme == ThemeType.ANDROID_THEME)
					{
						findViewById(R.id.page_divider).setVisibility(View.GONE);
						v.setVisibility(View.GONE);
					}
					else if(Theme == ThemeType.IPHONE_THEME)
					{
						v.setVisibility(View.GONE);
					}
				}
				else
				{
					if(Theme == ThemeType.ANDROID_THEME)
					{
						findViewById(R.id.page_divider).setVisibility(View.VISIBLE);
					}
				}
				setMissingInfo(mMissingCallAdapter.get(PageCount),PageAction.PAGE_UP);
			}
		});
	}
	/**
	 * Set layout info
	 * @param mMissingCallAdapter
	 */
	public void setMissingInfo(MissingCallAdapter mMissingCallAdapter, int pageAction)
	{
		if(Theme == ThemeType.CLASSIC_THEME)
		{
			long mCallTimer = mMissingCallAdapter.getTime();
			final String incomingNumber = mMissingCallAdapter.getIncomingNumber();
			TextView mcdText_1			= (TextView)findViewById(R.id.mcd_name_1);	
			TextView mcdText_2			= (TextView)findViewById(R.id.mcd_name_2);	
			TextView timeText			= (TextView)findViewById(R.id.mcd_time);
			ImageView reCallBreath		= (ImageView)findViewById(R.id.recall_breath);
			
			long Second = mCallTimer/1000;
//			Time mTime = new Time();
//			mTime.set(mCallTimer);
//			mTime.
			String ContactsName = getContactsInfo(incomingNumber);
			
	
			if (ContactsName == null) {
				mcdText_1.setText(incomingNumber);
				mcdText_2.setVisibility(View.GONE);
			} else {
				mcdText_1.setText(ContactsName);
				mcdText_2.setText(incomingNumber);
				mcdText_2.setVisibility(View.VISIBLE);
			}

			timeText.setText(getString(R.string.ringing_Time) + Long.toString(Second)
					+ getString(R.string.second));
			timeText.setTextColor(getTimeColor(Second));

			Animation alpha = AnimationUtils.loadAnimation(this, R.anim.alpha);
			alpha.setDuration(getBreathTime(Second));
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
		else if(Theme == ThemeType.WINDOWS_THEME)
		{
			startAnimationThemeWin();
			long mCallTimer = mMissingCallAdapter.getTime();
			final String incomingNumber = mMissingCallAdapter.getIncomingNumber();
			TextView mcdText_1			= (TextView)findViewById(R.id.mcd_name_1);	
			TextView mcdText_2			= (TextView)findViewById(R.id.mcd_name_2);	
			TextView timeText			= (TextView)findViewById(R.id.mcd_time);
			ImageView reCallIcon		= (ImageView)findViewById(R.id.btn_recall_icon);
			View reCallButton			= (View)findViewById(R.id.block_view_3);
			View contactDefaultIcon		= (View)findViewById(R.id.default_contact_icon);
			View BlockView_1 			= (View)findViewById(R.id.block_view_1);

			long Second = mCallTimer/1000;
//			Time mTime = new Time();
//			mTime.set(mCallTimer);
			
			String ContactsName = getContactsInfo(incomingNumber);
			Bitmap ContactsPhoto = getContactsPhoto(incomingNumber);

			if (ContactsName == null) {
				mcdText_1.setText(getString(R.string.unknow_contact_name));
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
			reCallIcon.setImageResource(getReCallIcon(Second));
			timeText.setText(Long.toString(Second) + getString(R.string.second));
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
		else if(Theme == ThemeType.ANDROID_THEME)
		{
			startAnimationThemeAndroid(pageAction);
			long mCallTimer = mMissingCallAdapter.getTime();
			long Second = mCallTimer/1000;
			final String incomingNumber = mMissingCallAdapter.getIncomingNumber();
			TextView mcdText_1			= (TextView)findViewById(R.id.mcd_name_1);	
			TextView mcdText_2			= (TextView)findViewById(R.id.mcd_name_2);	
			TextView timeText			= (TextView)findViewById(R.id.mcd_time);
			TextView titleText			= (TextView)findViewById(R.id.title_name);
			
			View reCallButton			= (View)findViewById(R.id.recall_btn);
			ImageView ContactImageView	= (ImageView)findViewById(R.id.contact_image);
			ImageView reCallIcon		= (ImageView)findViewById(R.id.btn_recall_icon);
			
			String ContactsName = getContactsInfo(incomingNumber);
			Bitmap ContactsPhoto = getContactsPhoto(incomingNumber);

			mcdText_2.setTypeface(tfGulim);
			timeText.setTypeface(tfClockopia);
			titleText.setTypeface(tfGulim);
			if (ContactsName == null) {
				mcdText_1.setText(R.string.unknow_contact_name);
				mcdText_2.setText(incomingNumber);
			} else {
				mcdText_1.setText(ContactsName);
				mcdText_2.setText(incomingNumber);
			}
			if (ContactsPhoto != null)
			{
				ContactImageView.setImageBitmap(ContactsPhoto);
			}
			else
			{
				ContactImageView.setImageResource(R.drawable.contact_icon_win_theme);
			}
			reCallIcon.setImageResource(getReCallIcon(Second));
			timeText.setText(Long.toString(Second)	+ getString(R.string.second));
			timeText.setTextColor(getTimeColor(Second));

			reCallButton.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
	                String phone = incomingNumber;  
					Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+phone));
					startActivity(callIntent);
				}
			});
		}
		else if(Theme == ThemeType.IPHONE_THEME)
		{

		}
	}
	/**
	 * Windows theme Layout animation
	 */
	private void startAnimationThemeWin()
	{
		if(Theme != ThemeType.WINDOWS_THEME)
		{
			return;
		}
		View BaseBlockView_1 			= (View)findViewById(R.id.base_block_1);
		View BaseBlockView_2 			= (View)findViewById(R.id.base_block_2);
		View BaseBlockView_3 			= (View)findViewById(R.id.base_block_3);
		View BaseBlockView_4 			= (View)findViewById(R.id.base_block_4);
		Animation animation_1 = AnimationUtils.loadAnimation(this, R.anim.translate_1);
		BaseBlockView_1.startAnimation(animation_1);
		Animation animation_2 = AnimationUtils.loadAnimation(this, R.anim.translate_2);
		BaseBlockView_2.startAnimation(animation_2);
		Animation animation_3 = AnimationUtils.loadAnimation(this, R.anim.translate_3);
		BaseBlockView_3.startAnimation(animation_3);
		Animation animation_4 = AnimationUtils.loadAnimation(this, R.anim.translate_4);
		BaseBlockView_4.startAnimation(animation_4);

	}
	/**
	 * Android theme Layout animation
	 * In
	 */
	private void startAnimationThemeAndroid(int pageAction)
	{
		if(Theme != ThemeType.ANDROID_THEME)
		{
			return;
		}
		if(pageAction==PageAction.PAGE_DOWN)
		{
			TextView timeText			= (TextView)findViewById(R.id.mcd_time);
			Animation animation_4 = AnimationUtils.loadAnimation(this, R.anim.theme_android_in_left_top);
			timeText.startAnimation(animation_4);
		}
		else if(pageAction==PageAction.PAGE_UP)
		{
			TextView timeText			= (TextView)findViewById(R.id.mcd_time);
			Animation animation_4 = AnimationUtils.loadAnimation(this, R.anim.theme_android_in_left_bottom);
			timeText.startAnimation(animation_4);
		}


	}
	/**
	 * Set the textView font Bold,but can be instead by xml
	 * @param text
	 */
	public void setFakeBoldText(TextView text)
	{
		TextPaint tp = text.getPaint(); 
		tp.setFakeBoldText(true); 
	}
	/**
	 * Contacts name
	 * @param address
	 * @return
	 */
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
	/**
	 * Contacts Photo
	 * @param address
	 * @return
	 */
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
	/**
	 * 
	 * @param time second
	 * @return
	 */
	public int getTimeColor(long time)
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
	/**
	 * 
	 * @param time second
	 * @return
	 */
	public int getReCallIcon(long time)
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
	/**
	 * 
	 * @param time second
	 * @return
	 */
	public long getBreathTime(long time)
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
