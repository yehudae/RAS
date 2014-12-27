package com.rafael.sdk.middleware.direct;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwDirectReplier.
 */
public class MwDirectReplier extends MwReplier {
	
	/** The lock. */
	private final Object lock = new Object();

	/**
	 * Constructor.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwDirectReplier(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwReceiver#receive(boolean)
	 */
	@Override
	public Bundle receive(boolean deserialize) {
		synchronized (lock) {
	        try {
				lock.wait();
			} 
	        catch (Exception e) 	{
				e.printStackTrace();
			}
		}
		
		return null;
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSender#send(com.rafael.sdk.util.Bundle)
	 */
	@Override
	public Bundle send(Bundle bundle) {
		return bundle;
	}
}