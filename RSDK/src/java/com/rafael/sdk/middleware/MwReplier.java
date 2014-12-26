package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

public abstract class MwReplier extends MwObject implements MwReceiver, MwSender {

	public MwReplier(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
}
