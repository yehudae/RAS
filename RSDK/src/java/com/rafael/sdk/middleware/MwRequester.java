package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

public abstract class MwRequester extends MwObject implements MwSender, MwConnector {

	public MwRequester(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
}
