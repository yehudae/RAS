package com.rafael.sdk.middleware.mix;

import org.jmq.Ctx;
import org.jmq.Msg;
import org.jmq.SocketBase;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.util.Bundle;

public class MwMixReplier extends MwReplier {

	SocketBase replier = null;
	
	/**
	 * Constructor
	 * @param newReplierId the replier Id
	 */
	public MwMixReplier (Ctx context, String connectionType, String connectionString, Component component) {
	    super(connectionType, connectionString, component);
		// create the replier
	    replier = context.create_socket(ZMQ.ZMQ_REP);

	    // connect 
	    replier.bind(connectionType + connectionString);
	}
	
	@Override
	public Bundle receive(boolean deserialize) {
		Msg msg = replier.recv(0);
		return Bundle.deserialize(msg.data(), msg.size());
	}

	@Override
	public Bundle send(Bundle bundle) {
		byte[] data = Bundle.serialize(bundle);		
		replier.send(new Msg(data, bundle.size()), 0);
		return bundle;
	}
}