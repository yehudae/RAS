 package com.rafael.sdk.middleware.direct;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.Middleware;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;

// TODO: Auto-generated Javadoc
/**
 * The Class DirectMiddleware.
 */
public class DirectMiddleware implements Middleware {

	/** The components. */
	private Map<String, Component> components = new ConcurrentHashMap<String, Component>();
	
	/** The publishers. */
	private Map<String, MwDirectPublisher> publishers = new ConcurrentHashMap<String, MwDirectPublisher>();
	
	/** The requesters. */
	private Map<String, MwDirectRequester> requesters = new HashMap<String, MwDirectRequester>();
	
	// Publisher
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createPublisher(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component)	{
		String connection = connectionType + connectionString;
		MwDirectPublisher publisher = new MwDirectPublisher(connectionType, connectionString, component);
		publishers.put(connection, publisher);
	    return publisher;
	}
	
	// Subscriber
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createSubscriber(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
	    return new MwDirectSubscriber(publishers, connectionType, connectionString, component); 
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createRequester(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwRequester createRequester(String connectionType, String connectionString, Component component)	{
		String connection = connectionType + connectionString;
		MwDirectRequester requester = requesters.get(connection);
		if (null == requester) {
			requester = new MwDirectRequester(components, connectionType, connectionString, component);
			requesters.put(connection, requester);
		}
		return requester;
	}
	
	// Replier
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.Middleware#createReplier(java.lang.String, java.lang.String, com.rafael.sdk.component.Component)
	 */
	public MwReplier createReplier(String connectionType, String connectionString, Component component){
		String name = connectionType + connectionString;
		components.put(name, component);
		return new MwDirectReplier(connectionType, connectionString, component);
	}
}