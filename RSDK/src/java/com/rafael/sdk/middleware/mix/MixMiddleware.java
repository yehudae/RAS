package com.rafael.sdk.middleware.mix;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.middleware.direct.DirectMiddleware;
import com.rafael.sdk.middleware.jmq.JmqMiddleware;

public class MixMiddleware implements Middleware {
	private DirectMiddleware directMiddleware;
	private JmqMiddleware jmqMiddleware;
	
	// private constructor of the class
	public MixMiddleware() {
		directMiddleware = new DirectMiddleware();
		jmqMiddleware = new JmqMiddleware();
	}

	// Publisher
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component) {
		if (connectionType.equals("direct://")) {
			return directMiddleware.createPublisher(connectionType, connectionString, component);
		}
		return jmqMiddleware.createPublisher(connectionType, connectionString, component);
	}
	
	// Subscriber
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
		if (connectionType.equals("direct://")) {
			return directMiddleware.createSubscriber(connectionType, connectionString, component);
		}
		return jmqMiddleware.createSubscriber(connectionType, connectionString, component);
	}

	// Requester
	public MwRequester createRequester(String connectionType, String connectionString, Component component)	{
		if (connectionType.equals("direct://")) {
			return directMiddleware.createRequester(connectionType, connectionString, component);
		}
		return jmqMiddleware.createRequester(connectionType, connectionString, component);
	}
	
	// Replier
	public MwReplier createReplier(String connectionType, String connectionString, Component component) {
		if (connectionType.equals("direct://")) {
			return directMiddleware.createReplier(connectionType, connectionString, component);
		}
		return jmqMiddleware.createReplier(connectionType, connectionString, component);
	}
}