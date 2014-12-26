package com.rafael.sdk.middleware.mix;

import org.jmq.Ctx;
import org.jmq.Msg;
import org.jmq.SocketBase;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.util.Bundle;

public class MwMixPublisher extends MwPublisher {

	private SocketBase publisher = null;

	/**
	 * 
	 * @param publisher
	 */
	public MwMixPublisher(Ctx context, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		// create the publisher
	    publisher = context.create_socket(ZMQ.ZMQ_PUB);	    
	    // bind the address
	    publisher.bind(connectionType + connectionString);
	}
	
	public synchronized boolean send(String topic, byte[] bytes) {
		publisher.send(new Msg(topic.getBytes()), ZMQ.ZMQ_SNDMORE);
		return publisher.send(new Msg(bytes), 0);
	}
	
	public synchronized Bundle send(Bundle bundle) {
		// send the topic
		String topic = bundle.getTopic();
		publisher.send(new Msg(topic.getBytes()), ZMQ.ZMQ_SNDMORE);
		// send the data
		byte[] data = Bundle.serialize(bundle);
		publisher.send(new Msg(data, bundle.size()), 0);
		return bundle;
	}
}