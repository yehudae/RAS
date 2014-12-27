package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * The Interface Middleware.
 */
public interface Middleware {
	// Publisher
	/**
	 * Creates the publisher.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 * @return the mw publisher
	 */
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component);	
	// Subscriber
	/**
	 * Creates the subscriber.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 * @return the mw subscriber
	 */
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component);

	// Requester
	/**
	 * Creates the requester.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 * @return the mw requester
	 */
	public MwRequester createRequester(String connectionType, String connectionString, Component component);
	
	// Replier
	/**
	 * Creates the replier.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 * @return the mw replier
	 */
	public MwReplier createReplier(String connectionType, String connectionString, Component component);
}