package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

public interface Middleware {
	// Publisher
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component);	
	// Subscriber
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component);

	// Requester
	public MwRequester createRequester(String connectionType, String connectionString, Component component);
	
	// Replier
	public MwReplier createReplier(String connectionType, String connectionString, Component component);
}