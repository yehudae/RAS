package com.rafael.sdk.middleware.zmq;

import java.nio.ByteBuffer;

import org.zeromq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwZmqSubscriber.
 */
public class MwZmqSubscriber extends MwSubscriber {

	/** The subscriber. */
	private ZMQ.Socket subscriber = null;
	
	/**
	 * Instantiates a new mw zmq subscriber.
	 *
	 * @param context the context
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwZmqSubscriber(ZMQ.Context context, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		// create the subscriber
	    subscriber = context.socket (ZMQ.SUB);    
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
	    // disconnect the subscriber from the connection
	    subscriber.disconnect(connection);
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSubscriber#subscribe(java.lang.String)
	 */
	public synchronized void subscribe(String topic) {
		subscriber.subscribe(topic.getBytes());
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSubscriber#unsubscribe(java.lang.String)
	 */
	public synchronized void unsubscribe(String topic) {
		subscriber.unsubscribe(topic.getBytes());
	}

	/**
	 * Receive.
	 *
	 * @param topic the topic
	 * @return the byte[]
	 */
	public byte[] receive(byte[] topic) {
		byte[] receivedTopic = subscriber.recv();
		System.arraycopy(receivedTopic, 0, topic, 0, receivedTopic.length);
		return subscriber.recv();
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwReceiver#receive(boolean)
	 */
	public Bundle receive(boolean deserialize) {
		Bundle bundle = null;
		byte[] topic = subscriber.recv();
		byte[] data = new byte[Bundle.MAX_BUNDLE_SIZE];

		int size = 0;

		if (deserialize) {
			size = subscriber.recv(data, 0, data.length, 0);
		}
		else {
			int prefix = 12 + topic.length;
			size = subscriber.recv(data, prefix, data.length, 0);
			ByteBuffer byteBuffer = ByteBuffer.wrap(data);
			byteBuffer.putInt(topic.length);
			byteBuffer.put(topic);
			byteBuffer.putInt(0);
			byteBuffer.putInt(size);
			size += prefix;
		}

		bundle = Bundle.deserialize(data, size);
		bundle.setTopic(new String(topic));
		
		return bundle;
	}
}