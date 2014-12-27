package com.rafael.sdk.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.rafael.sdk.middleware.MwReceiver;

// TODO: Auto-generated Javadoc
/**
 * The Class AsyncActivityHandlerLogic.
 */
public class AsyncActivityHandlerLogic extends ActivityHandlerLogic {
	
	/** The executor. */
	private ExecutorService executor = null;
	
	/**
	 * Instantiates a new async activity handler logic.
	 *
	 * @param receiver the receiver
	 * @param threadFactory the thread factory
	 * @param deserialize the deserialize
	 */
	public AsyncActivityHandlerLogic(MwReceiver receiver, ThreadFactory threadFactory, boolean deserialize) {
		super(receiver, deserialize);
		executor = Executors.newCachedThreadPool(threadFactory);
	}
	
	/**
	 * Instantiates a new async activity handler logic.
	 *
	 * @param receiver the receiver
	 * @param threadFactory the thread factory
	 * @param threadCount the thread count
	 * @param deserialize the deserialize
	 */
	public AsyncActivityHandlerLogic(MwReceiver receiver, ThreadFactory threadFactory, int threadCount, boolean deserialize) {
		super(receiver, deserialize);
		executor = Executors.newFixedThreadPool(threadCount, threadFactory);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.activity.ActivityHandlerLogic#doRun(com.rafael.sdk.activity.Activity)
	 */
	public void doRun(Activity activity) {
		executor.execute(activity);
	}
}
