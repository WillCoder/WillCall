package com.will.willcall;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewBold extends TextView{

	private boolean mBoldFlag = true;
	public TextViewBold(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		if(mBoldFlag)
		{
			this.setText();
		}
	}
	public TextViewBold(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		if(mBoldFlag)
		{
			this.setText();
		}
	}
	public TextViewBold(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		if(mBoldFlag)
		{
			this.setText();
		}
	}
	public void setBoldText(boolean BoldFlag)
	{
		this.mBoldFlag = BoldFlag;
	}
	private void setText()
	{
		this.getPaint().setFakeBoldText(true); 
	}

}
