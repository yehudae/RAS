package com.rafael.sdk.activity;

// TODO: Auto-generated Javadoc
/**
 * The Class AsyncActivityWithoutResult.
 */
public abstract class AsyncActivityWithoutResult extends Activity {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		doActivity(getBundle());
	}
}
