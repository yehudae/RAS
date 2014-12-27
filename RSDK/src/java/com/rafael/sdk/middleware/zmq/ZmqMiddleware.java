package com.rafael.sdk.middleware.zmq;

import org.zeromq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;

// TODO: Auto-generated Javadoc
/**
 * The Class ZmqMiddleware.
 */
public class ZmqMiddleware implements Middleware
{
	
	/** The context. */
	private ZMQ.Context context;
	
	// private constructor of the class
	/**
	 * Instantiates a new zmq middleware.
	 *
	 * @param threadCount the thread count
	 */
	public ZmqMiddleware(int threadCount) {	
		// create the context
	    context = ZMQ.context(threadCount);
	}

	// private constructor of the class
	/**
	 * Instantiates a new zmq middleware.
	 */
	public ZmqMiddleware() {
		this(1);
	}
	
	// Publisher
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createPublisher(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component) {
	    return new MwZmqPublisher(context, connectionType, connectionString, component);
	}
	
	// Subscriber
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createSubscriber(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
	    return new MwZmqSubscriber(context, connectionType, connectionString, component);
	}

	// Requester
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createRequester(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwRequester createRequester(String connectionType, String connectionString, Component component) {
		return new MwZmqRequester(context, connectionType, connectionString, component);
	}
	
	// Replier
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createReplier(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwReplier createReplier(String connectionType, String connectionString, Component component) {
		return new MwZmqReplier(context, connectionType, connectionString, component);
	}
}