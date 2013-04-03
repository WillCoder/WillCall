package com.will.willcall;
/**
 *  Missed Call imformation cache
 * @author Will
 *
 */
public class MissingCallAdapter {

	private long mCallTimer = -1;
	private String incomingNumber = null;
	MissingCallAdapter(long Time,String IncomingNumber)
	{
		mCallTimer = Time;
		incomingNumber = IncomingNumber;
	}
	public long getTime()
	{
		return mCallTimer;
	}
	public String getIncomingNumber()
	{
		return incomingNumber;
	}
}
