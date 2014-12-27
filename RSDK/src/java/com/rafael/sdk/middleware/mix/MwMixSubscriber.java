package com.rafael.sdk.middleware.mix;

import org.jmq.Ctx;
import org.jmq.Msg;
import org.jmq.SocketBase;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwMixSubscriber.
 */
public class MwMixSubscriber extends MwSubscriber {

	/** The subscriber. */
	private SocketBase subscriber = null;
	
	/**
	 * Instantiates a new mw mix subscriber.
	 *
	 * @param context the context
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwMixSubscriber (Ctx context, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		// create the subscriber
	    subscriber = context.create_socket(ZMQ.ZMQ_SUB);
	}
		
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwConnector#connect(java.lang.String)
	 */
	public synchronized void connect(String connection) {
	    // connect the subscriber to the connection
	    subscriber.connect(connection);
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwConnector#disconnect(java.lang.String)
	 */
	public synchronized void disconnect(String connection) {
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSubscriber#subscribe(java.lang.String)
	 */
	public synchronized void subscribe(String topic) {
		subscriber.setsockopt(ZMQ.ZMQ_SUBSCRIBE, topic);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSubscriber#unsubscribe(java.lang.String)
	 */
	public synchronized void unsubscribe(String topic) {
		subscriber.setsockopt(ZMQ.ZMQ_UNSUBSCRIBE, topic);
	}

	/**
	 * Receive.
	 *
	 * @param topic the topic
	 * @return the byte[]
	 */
	public byte[] receive(byte[] topic) {
		Msg receivedTopic = subscriber.recv(0);
		receivedTopic.getBytes(0, topic, 0, receivedTopic.size());
		return subscriber.recv(0).data();
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwReceiver#receive(boolean)
	 */
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