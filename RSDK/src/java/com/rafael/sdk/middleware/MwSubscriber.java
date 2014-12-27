package com.rafael.sdk.middleware;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * The Class MwSubscriber.
 */
public abstract class MwSubscriber extends MwObject implements MwReceiver, MwConnector {

	/**
	 * Instantiates a new mw subscriber.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwSubscriber(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
	
	/**
	 * Subscribe.
	 *
	 * @param topic the topic
	 */
	public abstract void subscribe(String topic);	
	
	/**
	 * Unsubscribe.
	 *
	 * @param topic the topic
	 */
	public abstract void unsubscribe(String topic);
}
