package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MwReplier.
 */
public abstract class MwReplier extends MwObject implements MwReceiver, MwSender {

	/**
	 * Instantiates a new mw replier.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwReplier(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
}
