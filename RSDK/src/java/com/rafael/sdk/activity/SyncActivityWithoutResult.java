package com.rafael.sdk.activity;

import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class SyncActivityWithoutResult.
 */
public abstract class SyncActivityWithoutResult extends Activity {

	/**
	 * Send result.
	 *
	 * @param bundle the bundle
	 */
	private void sendResult(Bundle bundle){
		reply(bundle);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Bundle bundle = getBundle(); 
		doActivity(bundle);
		bundle.clear();
		sendResult(bundle);	
	}
}
