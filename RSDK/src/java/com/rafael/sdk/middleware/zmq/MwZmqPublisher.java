package com.rafael.sdk.middleware.zmq;

import org.zeromq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.util.Bundle;


// TODO: Auto-generated Javadoc
/**
 * The Class MwZmqPublisher.
 */
public class MwZmqPublisher extends MwPublisher {

	/** The publisher. */
	private ZMQ.Socket publisher = null;

	/**
	 * Instantiates a new mw zmq publisher.
	 *
	 * @param context the context
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwZmqPublisher (ZMQ.Context context, String connectionType, String connectionString, Component component) {
	    super(connectionType, connectionString, component);
		// create the publisher
	    publisher = context.socket (ZMQ.PUB);
	    
	    // bind the address
	    publisher.bind(connectionType + connectionString);
	}
	
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwPublisher#send(java.lang.String, byte[])
	 */
	public synchronized boolean send(String topic, byte[] bytes){
		publisher.send(topic,ZMQ.SNDMORE);
		return publisher.send(bytes, bytes.length);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSender#send(com.rafael.sdk.util.Bundle)
	 */
	public synchronized Bundle send(Bundle bundle) {
		// send the topic
		String topic = bundle.getTopic();
		publisher.send(topic,ZMQ.SNDMORE);
		// send the data
		byte[] data = Bundle.serialize(bundle);
		publisher.send(data, 0, bundle.size(), 0);
		return bundle;
	}
}