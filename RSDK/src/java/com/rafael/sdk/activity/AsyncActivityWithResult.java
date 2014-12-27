package com.rafael.sdk.activity;

import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class AsyncActivityWithResult.
 */
public abstract class AsyncActivityWithResult extends Activity{

	/**
	 * Send result.
	 *
	 * @param bundle the bundle
	 */
	private void sendResult(Bundle bundle){
		bundle.updateReplyTopic();
		publishUp(bundle);
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Bundle bundle = getBundle(); 
		doActivity(bundle);
		sendResult(bundle);
	}
}