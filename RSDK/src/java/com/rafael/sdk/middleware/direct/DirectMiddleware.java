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

public class DirectMiddleware implements Middleware {

	private Map<String, Component> components = new ConcurrentHashMap<String, Component>();
	private Map<String, MwDirectPublisher> publishers = new ConcurrentHashMap<String, MwDirectPublisher>();
	private Map<String, MwDirectRequester> requesters = new HashMap<String, MwDirectRequester>();
	
	// Publisher
	public MwPublisher createPublisher(String connectionType, String connectionString, Component component)	{
		String connection = connectionType + connectionString;
		MwDirectPublisher publisher = new MwDirectPublisher(connectionType, connectionString, component);
		publishers.put(connection, publisher);
	    return publisher;
	}
	
	// Subscriber
	public MwSubscriber createSubscriber(String connectionType, String connectionString, Component component) {
	    return new MwDirectSubscriber(publishers, connectionType, connectionString, component); 
	}

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
	public MwReplier createReplier(String connectionType, String connectionString, Component component){
		String name = connectionType + connectionString;
		components.put(name, component);
		return new MwDirectReplier(connectionType, connectionString, component);
	}
}