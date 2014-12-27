package com.rafael.sdk.middleware;

// TODO: Auto-generated Javadoc
/**
 * The Interface MwConnector.
 */
public interface MwConnector {
	
	/**
	 * Connect.
	 *
	 * @param connection the connection
	 */
	public void connect(String connection);
	
	/**
	 * Disconnect.
	 *
	 * @param connection the connection
	 */
	public void disconnect(String connection);
}
