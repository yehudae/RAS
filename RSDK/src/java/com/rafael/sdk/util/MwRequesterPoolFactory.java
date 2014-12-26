package com.rafael.sdk.util;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MiddlewareManager;
import com.rafael.sdk.middleware.MwRequester;

public class MwRequesterPoolFactory implements PoolFactory<MwRequester> {

	private String connectionType;
	private String connectionString;
	private boolean connect;
	private Component component;
	
	public MwRequesterPoolFactory(String connectionType, String connectionString, boolean connect, Component component) {
		this.connectionType = connectionType;
		this.connectionString = connectionString;
		this.connect = connect;
		this.component = component;
	}
	
	@Override
	public MwRequester create() {
		MwRequester mwRequester = MiddlewareManager.instance().createRequester(connectionType, connectionString, component);
		if (connect) {
			mwRequester.connect(connectionType + connectionString);
		}
		return mwRequester;
	}
}
