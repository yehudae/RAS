package com.rafael.sdk.middleware.jmq;

import org.jmq.Ctx;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;

public class JmqMiddleware implements Middleware
{
    private Ctx context;
	
	// private constructor of the class
	public JmqMiddleware(int threadCount) {	
		// create the context
	    context = ZMQ.zmq_init(threadCount);
	}

	// private constructor of the class
	public JmqMiddleware() {
		this(1);
	}
	
	// Publisher
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component)	{
		return new MwJmqPublisher(context, connectionType, connectionString, component);
	}
	
	// Subscriber
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
	    return new MwJmqSubscriber(context, connectionType, connectionString, component); 
	}

	// Requester
	public MwRequester createRequester(String connectionType, String connectionString, Component component)	{
		return new MwJmqRequester(context, connectionType, connectionString, component);
	}
	
	// Replier
	public MwReplier createReplier(String connectionType, String connectionString, Component component){
		return new MwJmqReplier(context, connectionType, connectionString, component);
	}
}