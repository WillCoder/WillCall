package com.will.willcall;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

/**
 * ViewPager适配器
 */
public class NewPagerAdapter extends PagerAdapter {

	public Context context = null;

	public NewPagerAdapter(Context context, List<View> mListViews) {

	}

	public NewPagerAdapter(Context context, ArrayList<String> listData) {

	}

	@Override
	public void destroyItem(View arg0, int arg1, Object arg2) {
		System.out.println("destroyItem:" + arg1);

		((ViewPager) arg0).removeView((View) arg2);
	}

	@Override
	public void finishUpdate(View arg0) {
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object instantiateItem(View arg0, int arg1) {

		// LayoutInflater inflater = LayoutInflater.from(context);
		// View view = inflater.inflate(R.layout.knowledge, null);
		// System.out.println("instantiateItem:"+arg1);
		// view = getView(view,listData.get(arg1));
		// ((ViewPager) arg0).addView(view, 0);
		return arg0;
	}

	@Override
	public boolean isViewFromObject(View arg0, Object arg1) {
		return arg0 == (arg1);
	}

	@Override
	public void restoreState(Parcelable arg0, ClassLoader arg1) {
	}

	@Override
	public Parcelable saveState() {
		return null;
	}

	@Override
	public void startUpdate(View arg0) {
	}

	@Override
	public void setPrimaryItem(ViewGroup container, int position, Object object) {
		// TODO Auto-generated method stub
		super.setPrimaryItem(container, position, object);
	}
}