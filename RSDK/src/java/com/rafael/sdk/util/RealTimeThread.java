package com.rafael.sdk.util;

public class RealTimeThread extends Thread {

	public RealTimeThread(Runnable logic) {
		super(logic);
	}
}
