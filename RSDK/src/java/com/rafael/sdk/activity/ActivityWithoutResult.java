package com.rafael.sdk.activity;

import com.rafael.sdk.util.Bundle;

public abstract class ActivityWithoutResult extends Activity {

	private void sendResult(Bundle bundle) {
		if (isSync) {
			bundle.clear();
			reply(bundle);
		}
	}

	@Override
	public void run() {
		Thread.currentThread().setPriority(priority);
		Thread.currentThread().setName(getClass().getName());
		Bundle bundle = getBundle(); 
		doActivity(bundle);
		sendResult(bundle);	
	}
}
