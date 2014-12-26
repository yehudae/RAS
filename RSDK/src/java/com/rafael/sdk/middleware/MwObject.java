package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

public abstract class MwObject {
	
	private String connectionType;
	private String connectionString;
	private Component component;

	public MwObject(String connectionType, String connectionString, Component component) {
		this.connectionType = connectionType;
		this.connectionString = connectionString;
		this.component = component;
	}
	
	public String getConnectionType() {
		return connectionType;
	}

	public String getConnectionString() {
		return connectionString;
	}
	
	public Component getComponent() {
		return component;
	}
}
