package com.will.willcall;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.google.ads.AdRequest;
import com.google.ads.AdView;
import com.google.analytics.tracking.android.EasyTracker;
/**
 * 
 * @author Will
 *	create at 2013/3
 */
public class MainActivity extends Activity {

	private int Switch = -1;
	public static final int STATE_OPEN = 1;
	public static final int STATE_CLOSE = 0;
	private int FocusGalleryPosition = 0;
	private Typeface tf = null;
	
	private static final class RequestCode {
		public static final int ThemeRequest = 0;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_main_layout);
		/**************************** Get screen shoot **********************/
//		Intent intent = new Intent(this, MissingCallActivtiy.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtra("time", Long.valueOf(3000));
//		intent.putExtra("incomingNumber", "1999999999");
//		startActivity(intent);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//		intent.putExtra("time", Long.valueOf(8000));
//		intent.putExtra("incomingNumber", "2999999999");
//		startActivity(intent);
//		finish();

		/********************************************************************/
		/************************ AdMod add *********************************/
		AdView adView = (AdView) this.findViewById(R.id.adView);
		AdRequest mAdRequest = new AdRequest();
		mAdRequest.setTesting(false);
		adView.loadAd(mAdRequest);
		/************************ AdMod add *********************************/
		FocusGalleryPosition = getSharedPreferences(getPackageName(),
				MODE_PRIVATE).getInt("theme", FocusGalleryPosition);
		Switch = getSharedPreferences(getPackageName(), MODE_PRIVATE).getInt(
				"state", STATE_CLOSE);
		Intent service = new Intent(Intent.ACTION_RUN);
		service.setClass(this, CallService.class);
		startService(service);

		tf = Typeface.createFromAsset(getAssets(),"fonts/gulim.ttc");
		
		TextView versionText 	= (TextView) findViewById(R.id.about_version);
		ToggleButton mButton 	= (ToggleButton) findViewById(R.id.button);
		View mThemeButton 		= (View) findViewById(R.id.theme_button);
		View mTestButton 		= (View) findViewById(R.id.test_button);
		TextView ThemeTextView 	= (TextView) findViewById(R.id.setting_main_theme_text);

		/**
		 * Set fontfamily
		 */
		versionText.setTypeface(tf);
		((TextView)findViewById(R.id.switch_textview)).setTypeface(tf);
		((TextView)findViewById(R.id.theme_textview)).setTypeface(tf);
		((TextView)findViewById(R.id.test_textview)).setTypeface(tf);

		ThemeTextView.setText(ThemeActivity.ThemeStringIds[FocusGalleryPosition]);
		versionText.setText(getAppVersionName(this));
		mThemeButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(), ThemeActivity.class);
				startActivityForResult(intent, RequestCode.ThemeRequest);
				overridePendingTransition(R.anim.push_left_in,
						R.anim.push_left_out);
			}
		});
		mTestButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(v.getContext(),
						MissingCallActivtiy.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("time", Long.valueOf(3000));
				intent.putExtra("incomingNumber", "1999999999");
				v.getContext().startActivity(intent);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("time", Long.valueOf(8000));
				intent.putExtra("incomingNumber", "2999999999");
				v.getContext().startActivity(intent);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("time", Long.valueOf(23000));
				intent.putExtra("incomingNumber", "3999999999");
				v.getContext().startActivity(intent);

				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				intent.putExtra("time", Long.valueOf(134000));
				intent.putExtra("incomingNumber", "4999999999");
				v.getContext().startActivity(intent);
			}
		});

		if (Switch == STATE_CLOSE) {
			mButton.setChecked(false);
		} else if (Switch == STATE_OPEN) {
			mButton.setChecked(true);
		}
		mButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				// TODO Auto-generated method stub
				setSwitchState(isChecked);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		switch (requestCode) {
		case RequestCode.ThemeRequest: {
			if (resultCode == RESULT_OK) {
				TextView ThemeTextView = (TextView) findViewById(R.id.setting_main_theme_text);
				ThemeTextView.setText(data.getStringExtra("theme_name"));
			}
		}
			break;
		default:
			break;
		}
	}

	public void setSwitchState(boolean isChecked) {
		if (isChecked) {
			this.Switch = STATE_OPEN;
		} else {
			this.Switch = STATE_CLOSE;
		}
		SharedPreferences.Editor editor = getSharedPreferences(
				getPackageName(), MODE_PRIVATE).edit();
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
			// Log.e(THIS_FILE, "Exception", e);
		}
		return versionName;
	}
}
