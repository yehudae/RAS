package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MwObject.
 */
public abstract class MwObject {
	
	/** The connection type. */
	private String connectionType;
	
	/** The connection string. */
	private String connectionString;
	
	/** The component. */
	private Component component;

	/**
	 * Instantiates a new mw object.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwObject(String connectionType, String connectionString, Component component) {
		this.connectionType = connectionType;
		this.connectionString = connectionString;
		this.component = component;
	}
	
	/**
	 * Gets the connection type.
	 *
	 * @return the connection type
	 */
	public String getConnectionType() {
		return connectionType;
	}

	/**
	 * Gets the connection string.
	 *
	 * @return the connection string
	 */
	public String getConnectionString() {
		return connectionString;
	}
	
	/**
	 * Gets the component.
	 *
	 * @return the component
	 */
	public Component getComponent() {
		return component;
	}
}
