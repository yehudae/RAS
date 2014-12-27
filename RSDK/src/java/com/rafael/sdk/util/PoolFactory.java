package com.rafael.sdk.util;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Pool objects.
 *
 * @param <T> the generic type
 */
public interface PoolFactory<T> {
	
	/**
	 * Creates the.
	 *
	 * @return the t
	 */
	public T create();
}
