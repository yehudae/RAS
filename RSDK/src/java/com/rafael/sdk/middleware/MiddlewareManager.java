package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MiddlewareManager.
 */
public class MiddlewareManager {
	
	/** The middleware manager. */
	private static MiddlewareManager middlewareManager = new MiddlewareManager();
	
	/**
	 * Instantiates a new middleware manager.
	 */
	private MiddlewareManager(){}
	
		
	/** The middleware. */
	private Middleware middleware = null;
	
	/**
	 * Instance.
	 *
	 * @return the middleware manager
	 */
	public static MiddlewareManager instance() {
		return middlewareManager;
	}
	
	/**
	 * Sets the middleware.
	 *
	 * @param middleware the new middleware
	 */
	public void setMiddleware(Middleware middleware) {
		if (this.middleware == null) {
			this.middleware = middleware;
		}
	}
	
	/**
	 * Gets the middleware.
	 *
	 * @return the middleware
	 */
	public Middleware getMiddleware() {
		return middleware;
	}
	
	// Publisher
	/**
	 * Creates the publisher.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 * @return the mw publisher
	 */
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component) {
		return middleware.createPublisher(connectionType, connectionString, component);
	}
	
	// Subscriber
	/**
	 * Creates the subscriber.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 * @return the mw subscriber
	 */
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
		return middleware.createSubscriber(connectionType, connectionString, component);
	}

	// Requester
	/**
	 * Creates the requester.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 * @return the mw requester
	 */
	public MwRequester createRequester(String connectionType, String connectionString, Component component) {
		return middleware.createRequester(connectionType, connectionString, component);
	}
	
	// Replier
	/**
	 * Creates the replier.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 * @return the mw replier
	 */
	public MwReplier createReplier(String connectionType, String connectionString, Component component) {
		return middleware.createReplier(connectionType, connectionString, component);
	}
}