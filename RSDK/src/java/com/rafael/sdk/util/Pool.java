package com.rafael.sdk.util;

import java.util.concurrent.ConcurrentLinkedQueue;

// TODO: Auto-generated Javadoc
/**
 * The Class Pool.
 *
 * @param <T> the generic type
 */
public class Pool <T> {

	/** The elements. */
	private ConcurrentLinkedQueue<T> elements;
	
	/** The increase dynamically. */
	private boolean increaseDynamically;
	
	/** The pool factory. */
	private PoolFactory<T> poolFactory;
	
	/**
	 * Instantiates a new pool.
	 *
	 * @param poolFactory the pool factory
	 * @param capacity the capacity
	 * @param increaseDynamically the increase dynamically
	 */
	public Pool(PoolFactory<T> poolFactory, int capacity, boolean increaseDynamically) {
		this.poolFactory = poolFactory;
		this.increaseDynamically = increaseDynamically;
		elements = new ConcurrentLinkedQueue<T>();
		for (int i=0; i<capacity; i++) {
			elements.add(poolFactory.create());
		}
	}
	
	/**
	 * Allocate.
	 *
	 * @return the t
	 */
	public T allocate() {
		T element = elements.poll();
		if ((null == element) && increaseDynamically) {
			element = poolFactory.create();
		}
		
		return element;
	}
	
	/**
	 * Deallocate.
	 *
	 * @param element the element
	 */
	public void deallocate(T element) {
		elements.add(element);
	}
}
