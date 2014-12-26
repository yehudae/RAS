package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

public class MiddlewareManager {
	private static MiddlewareManager middlewareManager = new MiddlewareManager();
	
	private MiddlewareManager(){}
	
		
	private Middleware middleware = null;
	
	public static MiddlewareManager instance() {
		return middlewareManager;
	}
	
	public void setMiddleware(Middleware middleware) {
		if (this.middleware == null) {
			this.middleware = middleware;
		}
	}
	
	public Middleware getMiddleware() {
		return middleware;
	}
	
	// Publisher
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component) {
		return middleware.createPublisher(connectionType, connectionString, component);
	}
	
	// Subscriber
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
		return middleware.createSubscriber(connectionType, connectionString, component);
	}

	// Requester
	public MwRequester createRequester(String connectionType, String connectionString, Component component) {
		return middleware.createRequester(connectionType, connectionString, component);
	}
	
	// Replier
	public MwReplier createReplier(String connectionType, String connectionString, Component component) {
		return middleware.createReplier(connectionType, connectionString, component);
	}
}