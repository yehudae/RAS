package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MwRequester.
 */
public abstract class MwRequester extends MwObject implements MwSender, MwConnector {

	/**
	 * Instantiates a new mw requester.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwRequester(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
}
