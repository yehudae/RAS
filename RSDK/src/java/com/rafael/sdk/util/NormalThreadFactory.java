package com.rafael.sdk.util;

import java.util.concurrent.ThreadFactory;

public class NormalThreadFactory implements ThreadFactory {
	
	private int priority;
	
	public NormalThreadFactory(int priority) {
		this.priority = priority;
	}

	@Override
	public Thread newThread(Runnable r) {
		Thread normalThread = new Thread(r);
		normalThread.setPriority(priority);
		return normalThread;
	}
	
	public int getPriority() {
		return priority;
	}
}
