package com.rafael.sdk.middleware.mix;

import org.jmq.Ctx;
import org.jmq.Msg;
import org.jmq.SocketBase;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.util.Bundle;

public class MwMixSubscriber extends MwSubscriber {

	private SocketBase subscriber = null;
	
	public MwMixSubscriber (Ctx context, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		// create the subscriber
	    subscriber = context.create_socket(ZMQ.ZMQ_SUB);
	}
		
	public synchronized void connect(String connection) {
	    // connect the subscriber to the connection
	    subscriber.connect(connection);
	}

	public synchronized void disconnect(String connection) {
	}

	public synchronized void subscribe(String topic) {
		subscriber.setsockopt(ZMQ.ZMQ_SUBSCRIBE, topic);
	}
	
	public synchronized void unsubscribe(String topic) {
		subscriber.setsockopt(ZMQ.ZMQ_UNSUBSCRIBE, topic);
	}

	public byte[] receive(byte[] topic) {
		Msg receivedTopic = subscriber.recv(0);
		receivedTopic.getBytes(0, topic, 0, receivedTopic.size());
		return subscriber.recv(0).data();
	}
	
	public Bundle receive(boolean deserialize) {
		Bundle bundle = null;
		String topic = new String(subscriber.recv(0).data());
		Msg msg = subscriber.recv(0);
		
		if (deserialize) {
			bundle = Bundle.deserialize(msg.data(), msg.size());
			bundle.setTopic(topic);
		}
		else {
			bundle = Bundle.createBundle(topic, msg.data(), msg.size());
		}
		
		return bundle;
	}
}