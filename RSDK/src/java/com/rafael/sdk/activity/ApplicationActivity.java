package com.rafael.sdk.activity;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationActivity.
 */
public abstract class ApplicationActivity extends Activity {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		doActivity(getBundle());
	}
}
