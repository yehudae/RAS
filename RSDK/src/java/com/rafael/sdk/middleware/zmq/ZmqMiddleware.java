package com.rafael.sdk.middleware.zmq;

import org.zeromq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;

public class ZmqMiddleware implements Middleware
{
	private ZMQ.Context context;
	
	// private constructor of the class
	public ZmqMiddleware(int threadCount) {	
		// create the context
	    context = ZMQ.context(threadCount);
	}

	// private constructor of the class
	public ZmqMiddleware() {
		this(1);
	}
	
	// Publisher
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component) {
	    return new MwZmqPublisher(context, connectionType, connectionString, component);
	}
	
	// Subscriber
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
	    return new MwZmqSubscriber(context, connectionType, connectionString, component);
	}

	// Requester
	public MwRequester createRequester(String connectionType, String connectionString, Component component) {
		return new MwZmqRequester(context, connectionType, connectionString, component);
	}
	
	// Replier
	public MwReplier createReplier(String connectionType, String connectionString, Component component) {
		return new MwZmqReplier(context, connectionType, connectionString, component);
	}
}