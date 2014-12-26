package com.rafael.sdk.util;

import java.util.concurrent.ThreadFactory;

public class RealTimeThreadFactory implements ThreadFactory {
	
	private int priority;
	
	public RealTimeThreadFactory(int priority) {
		this.priority = priority;
	}

	@Override
	public Thread newThread(Runnable r) {
		RealTimeThread realTimeThread = new RealTimeThread(r);
		realTimeThread.setPriority(priority);
		return realTimeThread;
	}

	public int getPriority() {
		return priority;
	}
}
