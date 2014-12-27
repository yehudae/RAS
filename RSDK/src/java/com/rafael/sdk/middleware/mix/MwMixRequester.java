package com.rafael.sdk.middleware.mix;

import org.jmq.Ctx;
import org.jmq.Msg;
import org.jmq.SocketBase;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwMixRequester.
 */
public class MwMixRequester extends MwRequester {
	
	/** The requester. */
	private SocketBase requester = null;
	
	/**
	 * Instantiates a new mw mix requester.
	 *
	 * @param context the context
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwMixRequester (Ctx context, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		// create the requester
	    requester = context.create_socket(ZMQ.ZMQ_REQ);
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
		requester.term_endpoint(connection);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSender#send(com.rafael.sdk.util.Bundle)
	 */
	public Bundle send (Bundle bundle) {
		byte[] data = Bundle.serialize(bundle);
		requester.send(new Msg(data, bundle.size()), 0);
		Msg msg = requester.recv(0);
		return Bundle.deserialize(msg.data(), msg.size());
	}
}
