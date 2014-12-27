package com.rafael.sdk.middleware.mix;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.middleware.direct.DirectMiddleware;
import com.rafael.sdk.middleware.jmq.JmqMiddleware;

// TODO: Auto-generated Javadoc
/**
 * The Class MixMiddleware.
 */
public class MixMiddleware implements Middleware {
	
	/** The direct middleware. */
	private DirectMiddleware directMiddleware;
	
	/** The jmq middleware. */
	private JmqMiddleware jmqMiddleware;
	
	// private constructor of the class
	/**
	 * Instantiates a new mix middleware.
	 */
	public MixMiddleware() {
		directMiddleware = new DirectMiddleware();
		jmqMiddleware = new JmqMiddleware();
	}

	// Publisher
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createPublisher(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component) {
		if (connectionType.equals("direct://")) {
			return directMiddleware.createPublisher(connectionType, connectionString, component);
		}
		return jmqMiddleware.createPublisher(connectionType, connectionString, component);
	}
	
	// Subscriber
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createSubscriber(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
		if (connectionType.equals("direct://")) {
			return directMiddleware.createSubscriber(connectionType, connectionString, component);
		}
		return jmqMiddleware.createSubscriber(connectionType, connectionString, component);
	}

	// Requester
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createRequester(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwRequester createRequester(String connectionType, String connectionString, Component component)	{
		if (connectionType.equals("direct://")) {
			return directMiddleware.createRequester(connectionType, connectionString, component);
		}
		return jmqMiddleware.createRequester(connectionType, connectionString, component);
	}
	
	// Replier
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createReplier(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwReplier createReplier(String connectionType, String connectionString, Component component) {
		if (connectionType.equals("direct://")) {
			return directMiddleware.createReplier(connectionType, connectionString, component);
		}
		return jmqMiddleware.createReplier(connectionType, connectionString, component);
	}
}