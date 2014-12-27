package com.rafael.sdk.activity;

import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class ActivityWithResult.
 */
public abstract class ActivityWithResult extends Activity {

	/**
	 * Send result.
	 *
	 * @param bundle the bundle
	 */
	private void sendResult(Bundle bundle) {
		if (isSync) {
			reply(bundle);
		}
		else {
			bundle.updateReplyTopic();
			publishUp(bundle);
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	@Override
	public void run() {
		Thread.currentThread().setPriority(priority);
		Thread.currentThread().setName(getClass().getName());
		Bundle bundle = getBundle(); 
		doActivity(bundle);
		sendResult(bundle);
	}
}
