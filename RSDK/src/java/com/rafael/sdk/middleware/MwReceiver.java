package com.rafael.sdk.middleware;

import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Interface MwReceiver.
 */
public interface MwReceiver {
	
	/**
	 * Receive.
	 *
	 * @param deserialize the deserialize
	 * @return the bundle
	 */
	public Bundle receive(boolean deserialize);
}
