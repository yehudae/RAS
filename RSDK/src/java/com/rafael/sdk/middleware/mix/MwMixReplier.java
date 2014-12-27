package com.rafael.sdk.middleware.mix;

import org.jmq.Ctx;
import org.jmq.Msg;
import org.jmq.SocketBase;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwMixReplier.
 */
public class MwMixReplier extends MwReplier {

	/** The replier. */
	SocketBase replier = null;
	
	/**
	 * Constructor.
	 *
	 * @param context the context
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwMixReplier (Ctx context, String connectionType, String connectionString, Component component) {
	    super(connectionType, connectionString, component);
		// create the replier
	    replier = context.create_socket(ZMQ.ZMQ_REP);

	    // connect 
	    replier.bind(connectionType + connectionString);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwReceiver#receive(boolean)
	 */
	@Override
	public Bundle receive(boolean deserialize) {
		Msg msg = replier.recv(0);
		return Bundle.deserialize(msg.data(), msg.size());
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSender#send(com.rafael.sdk.util.Bundle)
	 */
	@Override
	public Bundle send(Bundle bundle) {
		byte[] data = Bundle.serialize(bundle);		
		replier.send(new Msg(data, bundle.size()), 0);
		return bundle;
	}
}