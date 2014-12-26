package com.rafael.sdk.activity;

public abstract class ApplicationActivity extends Activity {

	@Override
	public void run() {
		doActivity(getBundle());
	}
}
