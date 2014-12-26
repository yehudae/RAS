package com.rafael.sdk.middleware.zmq;

import org.zeromq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.util.Bundle;

public class MwZmqReplier extends MwReplier {

	ZMQ.Socket replier = null;
	
	/**
	 * Constructor
	 * @param newReplierId the replier Id
	 */
	public MwZmqReplier(ZMQ.Context context, String connectionType, String connectionString, Component component) {
	    super(connectionType, connectionString, component);
		// create the replier
	    replier = context.socket(ZMQ.REP);
	    // connect 
	    replier.bind(connectionType + connectionString);
	}
	
	@Override
	public Bundle receive(boolean deserialize) {
		byte[] data = new byte[Bundle.MAX_BUNDLE_SIZE];
		int size = replier.recv(data, 0, data.length, 0);
		return Bundle.deserialize(data, size);
	}

	@Override
	public Bundle send(Bundle bundle) {
		byte[] data = Bundle.serialize(bundle);		
		replier.send(data, 0, bundle.size(), 0);
		return bundle;
	}
}