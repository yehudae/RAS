package com.rafael.sdk.activity;

public abstract class AsyncActivityWithoutResult extends Activity {

	@Override
	public void run() {
		doActivity(getBundle());
	}
}
