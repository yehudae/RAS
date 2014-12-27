package com.rafael.sdk.util;

import java.util.concurrent.ThreadFactory;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating RealTimeThread objects.
 */
public class RealTimeThreadFactory implements ThreadFactory {
	
	/** The priority. */
	private int priority;
	
	/**
	 * Instantiates a new real time thread factory.
	 *
	 * @param priority the priority
	 */
	public RealTimeThreadFactory(int priority) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(Runnable r) {
		RealTimeThread realTimeThread = new RealTimeThread(r);
		realTimeThread.setPriority(priority);
		return realTimeThread;
	}

	/**
	 * Gets the priority.
	 *
	 * @return the priority
	 */
	public int getPriority() {
		return priority;
	}
}
