package com.rafael.sdk.middleware;

public interface MwConnector {
	public void connect(String connection);
	public void disconnect(String connection);
}
