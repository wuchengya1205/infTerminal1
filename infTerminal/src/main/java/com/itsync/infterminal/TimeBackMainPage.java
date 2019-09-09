package com.itsync.infterminal;

import com.itsync.displayobject.DisplayTemplate;

/**
 * @author 作者 E-mail:
 * @version 创建时间：2018年11月23日 下午12:58:27 类说明
 */
public class TimeBackMainPage extends Thread {

	private static volatile TimeBackMainPage mThread;
	private long mCurrentTime = System.currentTimeMillis();
	private volatile boolean mWork;
	private MainActivity mActivity;
	private boolean doWork = false;

	private TimeBackMainPage() {
		// TODO Auto-generated constructor stub
	}

	public static TimeBackMainPage getInstance() {

		if (mThread == null) {
			synchronized (TimeBackMainPage.class) {
				if (mThread == null) {
					mThread = new TimeBackMainPage();
				}
			}
		}
		return mThread;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub

		while (mWork) {
//			synchronized (mThread) {
				if (System.currentTimeMillis() - mCurrentTime >= 30000
						&& doWork) {
					if (!(DisplayTemplate._filteName.equals("主页面"))) {
						DisplayTemplate._filteName = "主页面";
						mActivity._displayPageChanged = true;
						doWork = false;
					}
				}
//			}
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void resetTime() {
//		synchronized (mThread) {
			mCurrentTime = System.currentTimeMillis();
			doWork = true;
//		}
	}

	public synchronized void startWork(MainActivity acitivty) {
		this.mWork = true;
		this.mActivity = acitivty;
		if (!this.isInterrupted()) {
			try {
				this.start();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void stopWork() {
		this.mWork = false;
		this.interrupt();
		mThread = null;
	}
}
