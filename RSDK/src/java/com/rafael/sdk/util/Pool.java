package com.rafael.sdk.util;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Pool <T> {

	private ConcurrentLinkedQueue<T> elements;
	private boolean increaseDynamically;
	private PoolFactory<T> poolFactory;
	
	public Pool(PoolFactory<T> poolFactory, int capacity, boolean increaseDynamically) {
		this.poolFactory = poolFactory;
		this.increaseDynamically = increaseDynamically;
		elements = new ConcurrentLinkedQueue<T>();
		for (int i=0; i<capacity; i++) {
			elements.add(poolFactory.create());
		}
	}
	
	public T allocate() {
		T element = elements.poll();
		if ((null == element) && increaseDynamically) {
			element = poolFactory.create();
		}
		
		return element;
	}
	
	public void deallocate(T element) {
		elements.add(element);
	}
}
