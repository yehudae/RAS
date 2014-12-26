package com.rafael.sdk.activity;

import com.rafael.sdk.util.Bundle;

public abstract class AsyncActivityWithResult extends Activity{

	private void sendResult(Bundle bundle){
		bundle.updateReplyTopic();
		publishUp(bundle);
	}

	@Override
	public void run() {
		Bundle bundle = getBundle(); 
		doActivity(bundle);
		sendResult(bundle);
	}
}