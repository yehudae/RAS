package com.rafael.sdk.middleware.zmq;

import org.zeromq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwZmqRequester.
 */
public class MwZmqRequester extends MwRequester {
	
	/** The requester. */
	private ZMQ.Socket requester = null;
	
	/**
	 * Instantiates a new mw zmq requester.
	 *
	 * @param context the context
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwZmqRequester (ZMQ.Context context,String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		// create the requester
	    requester = context.socket (ZMQ.REQ);
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwConnector#connect(java.lang.String)
	 */
	public synchronized void connect(String connection) {		
	    requester.connect(connection);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwConnector#disconnect(java.lang.String)
	 */
	public synchronized void disconnect(String connection) {
		requester.disconnect(connection);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSender#send(com.rafael.sdk.util.Bundle)
	 */
	public synchronized Bundle send(Bundle bundle) {
		byte[] data = Bundle.serialize(bundle);
		requester.send(data, 0, bundle.size(), 0);
		data = new byte[Bundle.MAX_BUNDLE_SIZE];
		int size = requester.recv(data, 0, data.length, 0);
		return Bundle.deserialize(data, size);
	}
}
