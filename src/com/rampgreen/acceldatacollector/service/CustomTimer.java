package com.rampgreen.acceldatacollector.service;

import java.util.Timer;
import java.util.TimerTask;

import com.rampgreen.acceldatacollector.util.AppLog;

/**
 * @author Manish Pathak 
 *
 */
public class CustomTimer extends TimerTask{

	private long period = 1000; //1 second
	private long delay = 0;
	private Timer myTimer;
	private CustomTimerCallBack customTimerCallBack;
	private long totalExcutionTime = 0;
	private long elapsedExcutionTime = 0;
	private long cancelDuration = -1;

	public CustomTimer(CustomTimerCallBack customTimerCallBack, long delay, long period) {
		this.delay = delay;
		this.period = period;
		this.customTimerCallBack = customTimerCallBack;
		myTimer = new Timer();

	}
	
	public CustomTimer(CustomTimerCallBack customTimerCallBack, long delay, int sampleRateInHertz) {
		this.delay = delay;
		this.period = period / (long)sampleRateInHertz;
		this.customTimerCallBack = customTimerCallBack;
		myTimer = new Timer();

	}

	public void start() {
		totalExcutionTime = 0;
		elapsedExcutionTime = 0;
		myTimer.schedule(this, delay, period);
	}

	/**
	 * @param cancelDuration in miliseconds
	 */
	public void setAutomaticCancel(long cancelDuration) {
		this.cancelDuration = cancelDuration;
	}

	/**
	 * stop timer task, If there is a currently running task it is not affected. No more tasks may be scheduled on this Timer. Subsequent calls do nothing.
	 * Removes all canceled tasks from the task queue. If there are no other references on the tasks, then after this call they are free to be garbage collected.
	 * 
	 * @return the number of canceled tasks that were removed from the task queue. 
	 */
	public int stop() {
		myTimer.cancel();
		return myTimer.purge();
	}

	@Override
	public void run() {
		TimerMethod();
	}

	protected void TimerMethod() {
		if( cancelDuration == -1) {
			customTimerCallBack.exceuteOnUIThread(true);
			return;
		}
		totalExcutionTime = totalExcutionTime + period;
		elapsedExcutionTime = cancelDuration - totalExcutionTime;
		AppLog.e("elapsedTime"+elapsedExcutionTime);
		
		
		// a condition if elapsedExecution or u can say period  is not complete number 
		// like 1000/3 = 333.33 but schedular takes integer(333) so 1 milisecond differ occurs after 3 counts
		//it will avoid this condition
		if(elapsedExcutionTime - period < 0) {
			customTimerCallBack.exceuteOnUIThread(true);
			stop();
			return;
		}
		
		if(elapsedExcutionTime == 0) {
			customTimerCallBack.exceuteOnUIThread(true);
		} else if(totalExcutionTime <= cancelDuration) {
			customTimerCallBack.exceuteOnUIThread(false);
		} else {
			stop();
		}
	}

	public interface CustomTimerCallBack {
		/**
		 * This method is called directly by the timer
		and runs in the same thread as the timer.

		We call the method that will work with the UI
		through the runOnUiThread method.
		 */
		public void exceuteOnUIThread(boolean isLastCall);
	}

}
