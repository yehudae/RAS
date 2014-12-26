package com.rafael.sdk.activity;

import com.rafael.sdk.util.Bundle;

public abstract class SyncActivityWithoutResult extends Activity {

	private void sendResult(Bundle bundle){
		reply(bundle);
	}

	@Override
	public void run() {
		Bundle bundle = getBundle(); 
		doActivity(bundle);
		bundle.clear();
		sendResult(bundle);	
	}
}
