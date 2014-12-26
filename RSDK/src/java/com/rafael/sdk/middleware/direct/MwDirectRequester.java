package com.rafael.sdk.middleware.direct;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.rafael.sdk.activity.Activity;
import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.util.Bundle;

public class MwDirectRequester extends MwRequester {
	
	private Map<String, Component> components;
	private Map<String, Component> connectedComponents = new ConcurrentHashMap<String, Component>();
	
	public MwDirectRequester (Map<String, Component> components, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		this.components = components;
		String name = connectionType + connectionString;
		Component replier = components.get(name);
		if (null != replier) {
			connect(name);
		}
	}
	
	public void connect(String connection) {
		Component component = components.get(connection);
		if (null != component) {
			connectedComponents.put(connection, component);
		}
	}
	
	public void disconnect(String connection) {
		connectedComponents.remove(connection);
	}
	
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
