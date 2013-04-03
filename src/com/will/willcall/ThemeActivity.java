package com.will.willcall;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
/**
 * 
 * @author Will
 *	create at 2013/3
 */
public class ThemeActivity extends Activity implements OnItemSelectedListener {

	private int[] mThemeImageIds = { R.drawable.theme_classic, R.drawable.theme_windows};
	private int[] mThemeStringIds = { R.string.theme_classic, R.string.theme_windows};
//	private int ThemeSelect = 0;
	private int FocusGalleryPosition = 0;
	private TextView ThemeStrView = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.setting_theme_layout);

		
		FocusGalleryPosition = getSharedPreferences(getPackageName(),MODE_PRIVATE).getInt("theme",FocusGalleryPosition);
		
		Gallery mGallery			= (Gallery) findViewById(R.id.images_gallery);
		ImageButton GobackButton	= (ImageButton) findViewById(R.id.setting_theme_goback_btn);
		Button AcceptButton			= (Button) findViewById(R.id.setting_theme_accept_btn);
		ThemeStrView				= (TextView) findViewById(R.id.setting_theme_textview);
		
		GobackButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		AcceptButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
//				Toast.makeText(v.getContext(), "select"+FocusGalleryPosition, Toast.LENGTH_LONG).show();
				SharedPreferences.Editor editor = getSharedPreferences(getPackageName(),MODE_PRIVATE).edit();
				editor.putInt("theme", FocusGalleryPosition);
				editor.commit();
				Intent intent = new Intent();
				intent.putExtra("theme_name", getResources().getString(mThemeStringIds[FocusGalleryPosition]));
				setResult(RESULT_OK, intent);
				finish();
			}
		});
		ThemeStrView.setText(mThemeStringIds[FocusGalleryPosition]);
		
		mGallery.setAdapter(new ImageAdapter(this)); // 和ListView一样，Gallery需要一个adapter进行资源配置
		mGallery.setOnItemSelectedListener(this);
		mGallery.setSelection(FocusGalleryPosition);
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
		overridePendingTransition(R.anim.push_right_in, R.anim.push_right_out); 
	}

	private class ImageAdapter extends BaseAdapter {
		private Context mContext;

		private ImageAdapter(Context c) {
			mContext = c;
		}

		@Override
		public int getCount() {
			return mThemeImageIds.length;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView mImageView;
			if (convertView == null) { // if it's not recycled, initialize some
										// attributes.
				mImageView = new ImageView(mContext);
				// mImageView.setLayoutParams(new
				// Gallery.LayoutParams(LayoutParams.WRAP_CONTENT,
				// LayoutParams.WRAP_CONTENT)); // 设置Gallery图片(ImageView)大小
				mImageView
						.setBackgroundResource(R.drawable.setting_theme_image_bg); // 设置Gallery图片(ImageView)背景
				mImageView.setAdjustViewBounds(true); // 使Gallery图片自适应屏幕分辨率，以免图片bound超出屏幕范围
				mImageView.setImageResource(mThemeImageIds[position]); // 设置Gallery图片(ImageView)源资源
			} else {
				mImageView = (ImageView) convertView;
			}
			return mImageView;
		}
	}

	@Override
	public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
			long arg3) {
		// TODO Auto-generated method stub
		this.FocusGalleryPosition = arg2;
		ThemeStrView.setText(mThemeStringIds[FocusGalleryPosition]);
	}

	@Override
	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub
		
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