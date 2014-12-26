package com.rafael.sdk.activity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.rafael.sdk.middleware.MwReceiver;

public class AsyncActivityHandlerLogic extends ActivityHandlerLogic {
	
	private ExecutorService executor = null;
	
	public AsyncActivityHandlerLogic(MwReceiver receiver, ThreadFactory threadFactory, boolean deserialize) {
		super(receiver, deserialize);
		executor = Executors.newCachedThreadPool(threadFactory);
	}
	
	public AsyncActivityHandlerLogic(MwReceiver receiver, ThreadFactory threadFactory, int threadCount, boolean deserialize) {
		super(receiver, deserialize);
		executor = Executors.newFixedThreadPool(threadCount, threadFactory);
	}
	
	public void doRun(Activity activity) {
		executor.execute(activity);
	}
}
