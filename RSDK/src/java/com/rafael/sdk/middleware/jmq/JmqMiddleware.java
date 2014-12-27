package com.rafael.sdk.middleware.jmq;

import org.jmq.Ctx;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;

// TODO: Auto-generated Javadoc
/**
 * The Class JmqMiddleware.
 */
public class JmqMiddleware implements Middleware
{
    
    /** The context. */
    private Ctx context;
	
	// private constructor of the class
	/**
	 * Instantiates a new jmq middleware.
	 *
	 * @param threadCount the thread count
	 */
	public JmqMiddleware(int threadCount) {	
		// create the context
	    context = ZMQ.zmq_init(threadCount);
	}

	// private constructor of the class
	/**
	 * Instantiates a new jmq middleware.
	 */
	public JmqMiddleware() {
		this(1);
	}
	
	// Publisher
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createPublisher(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component)	{
		return new MwJmqPublisher(context, connectionType, connectionString, component);
	}
	
	// Subscriber
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createSubscriber(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
	    return new MwJmqSubscriber(context, connectionType, connectionString, component); 
	}

	// Requester
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createRequester(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwRequester createRequester(String connectionType, String connectionString, Component component)	{
		return new MwJmqRequester(context, connectionType, connectionString, component);
	}
	
	// Replier
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createReplier(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwReplier createReplier(String connectionType, String connectionString, Component component){
		return new MwJmqReplier(context, connectionType, connectionString, component);
	}
}