package com.will.willcall;

import java.util.Calendar;
/**
 * The call state cache
 * @author BXC2011010
 *
 */
public class CallTimer {

	public static final int STATE_TIMING = 0;
	public static final int STATE_IDLE = 1;
	
	
	private int TimerState = -1;
	private long timeOffset = -1;
	
	public CallTimer()
	{
		this.clear();
	}
	public void clear()
	{
		this.TimerState = STATE_IDLE;
		this.timeOffset = -1;
	}
	public void start()
	{
		if(TimerState!=STATE_IDLE)
		{
			return;
		}
		this.TimerState = STATE_TIMING;
		this.timeOffset = Calendar.getInstance().getTimeInMillis();
	}
	public long end()
	{
		if(TimerState!=STATE_TIMING)
		{
			return -1;
		}
		this.TimerState = STATE_IDLE;
		long ret = Calendar.getInstance().getTimeInMillis() - this.timeOffset;
		return ret;
	}
	public int getTimerState()
	{
		return this.TimerState;
	}
}
