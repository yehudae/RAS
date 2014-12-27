package com.rafael.sdk.util;

import java.util.concurrent.ThreadFactory;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating NormalThread objects.
 */
public class NormalThreadFactory implements ThreadFactory {
	
	/** The priority. */
	private int priority;
	
	/**
	 * Instantiates a new normal thread factory.
	 *
	 * @param priority the priority
	 */
	public NormalThreadFactory(int priority) {
		this.priority = priority;
	}

	/* (non-Javadoc)
	 * @see java.util.concurrent.ThreadFactory#newThread(java.lang.Runnable)
	 */
	@Override
	public Thread newThread(Runnable r) {
		Thread normalThread = new Thread(r);
		normalThread.setPriority(priority);
		return normalThread;
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
