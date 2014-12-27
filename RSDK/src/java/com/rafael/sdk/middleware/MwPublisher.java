package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MwPublisher.
 */
public abstract class MwPublisher extends MwObject implements MwSender {

	/**
	 * Instantiates a new mw publisher.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwPublisher(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
	
	/**
	 * Send.
	 *
	 * @param topic the topic
	 * @param bytes the bytes
	 * @return true, if successful
	 */
	public abstract boolean send(String topic, byte[] bytes);
}
