package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

public abstract class MwSubscriber extends MwObject implements MwReceiver, MwConnector {

	public MwSubscriber(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
	
	public abstract void subscribe(String topic);	
	public abstract void unsubscribe(String topic);
}
