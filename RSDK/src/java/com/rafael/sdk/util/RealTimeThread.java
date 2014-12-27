package com.rafael.sdk.util;

// TODO: Auto-generated Javadoc
/**
 * The Class RealTimeThread.
 */
public class RealTimeThread extends Thread {

	/**
	 * Instantiates a new real time thread.
	 *
	 * @param logic the logic
	 */
	public RealTimeThread(Runnable logic) {
		super(logic);
	}
}
