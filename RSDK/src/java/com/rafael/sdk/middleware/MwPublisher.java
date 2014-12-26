package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

public abstract class MwPublisher extends MwObject implements MwSender {

	public MwPublisher(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
	
	public abstract boolean send(String topic, byte[] bytes);
}
