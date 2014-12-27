package com.rafael.sdk.util;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MiddlewareManager;
import com.rafael.sdk.middleware.MwRequester;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating MwRequesterPool objects.
 */
public class MwRequesterPoolFactory implements PoolFactory<MwRequester> {

	/** The connection type. */
	private String connectionType;
	
	/** The connection string. */
	private String connectionString;
	
	/** The connect. */
	private boolean connect;
	
	/** The component. */
	private Component component;
	
	/**
	 * Instantiates a new mw requester pool factory.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param connect the connect
	 * @param component the component
	 */
	public MwRequesterPoolFactory(String connectionType, String connectionString, boolean connect, Component component) {
		this.connectionType = connectionType;
		this.connectionString = connectionString;
		this.connect = connect;
		this.component = component;
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.util.PoolFactory#create()
	 */
	@Override
	public MwRequester create() {
		MwRequester mwRequester = MiddlewareManager.instance().createRequester(connectionType, connectionString, component);
		if (connect) {
			mwRequester.connect(connectionType + connectionString);
		}
		return mwRequester;
	}
}
