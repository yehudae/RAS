package com.rafael.sdk.middleware.direct;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.util.Bundle;

public class MwDirectReplier extends MwReplier {
	
	private final Object lock = new Object();

	/**
	 * Constructor
	 * @param newReplierId the replier Id
	 */
	public MwDirectReplier(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
	
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

	@Override
	public Bundle send(Bundle bundle) {
		return bundle;
	}
}