package com.rafael.sdk.middleware.direct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rafael.sdk.activity.Activity;
import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwDirectRequester.
 */
public class MwDirectRequester extends MwRequester {
	
	/** The components. */
	private Map<String, Component> components;
	
	/** The connected components. */
	private Map<String, Component> connectedComponents = new ConcurrentHashMap<String, Component>();
	
	/**
	 * Instantiates a new mw direct requester.
	 *
	 * @param components the components
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwDirectRequester (Map<String, Component> components, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		this.components = components;
		String name = connectionType + connectionString;
		Component replier = components.get(name);
		if (null != replier) {
			connect(name);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwConnector#connect(java.lang.String)
	 */
	public void connect(String connection) {
		Component component = components.get(connection);
		if (null != component) {
			connectedComponents.put(connection, component);
		}
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwConnector#disconnect(java.lang.String)
	 */
	public void disconnect(String connection) {
		connectedComponents.remove(connection);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSender#send(com.rafael.sdk.util.Bundle)
	 */
	public Bundle send (Bundle bundle) {
		String destination = null;
				
		try {
			destination = getConnectionType() + bundle.getDestination();
		}
		catch (Exception e) {
			for (Map.Entry<String, Component> entry : connectedComponents.entrySet()) {
				destination = entry.getKey();
			}
		}
		
		if (null != destination) {
			Component component = connectedComponents.get(destination);
			if (null != component) {
				Activity activity = component.getSyncActivity(bundle.getTopic());
				if (null!= activity) {
					activity.doActivity(bundle);
				}
			}
		}
		
		return bundle;
	}
}
