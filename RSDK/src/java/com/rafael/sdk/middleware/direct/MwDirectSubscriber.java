package com.rafael.sdk.middleware.direct;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwDirectSubscriber.
 */
public class MwDirectSubscriber extends MwSubscriber {

	/** The queue. */
	private BlockingQueue<Bundle> queue = new LinkedBlockingQueue<Bundle>();
	
	/** The publishers. */
	private Map<String, MwDirectPublisher> publishers;
	
	/** The connected publishers. */
	private Vector<MwDirectPublisher> connectedPublishers = new Vector<MwDirectPublisher>();
	
	/** The topics. */
	private Map<String, Integer> topics = new HashMap<String, Integer>();
	
	/**
	 * Instantiates a new mw direct subscriber.
	 *
	 * @param publishers the publishers
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwDirectSubscriber (Map<String, MwDirectPublisher> publishers, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		this.publishers = publishers;
	}
		
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwConnector#connect(java.lang.String)
	 */
	public void connect(String connection) {
		MwDirectPublisher publisher = publishers.get(connection);
		if (null != publisher) {
			connectedPublishers.add(publisher);
		}
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwConnector#disconnect(java.lang.String)
	 */
	public void disconnect(String connection) {
		MwDirectPublisher publisher = publishers.get(connection);
		if (null != publisher) {
			connectedPublishers.remove(publisher);
			
			for (Map.Entry<String, Integer> entry : topics.entrySet()) {
				String topic = entry.getKey();
				Integer count = entry.getValue();
				publisher.removeQueue(topic, queue);
				if (count > 1) {
					--count;
				}
				else {
					topics.remove(topic);
				}
			}
		}
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSubscriber#subscribe(java.lang.String)
	 */
	public void subscribe(String topic) {
		int numOfconnectedPublisers = connectedPublishers.size();
		
		if (numOfconnectedPublisers > 0) {
			Integer count = topics.get(topic);
			if (null == count) {
				count = new Integer(1);
				topics.put(topic, count);
			}
			else {
				++count;
			}
		
			for (int i=0; i<numOfconnectedPublisers; i++) {
				MwDirectPublisher publisher = connectedPublishers.get(i);
				publisher.putQueue(topic, queue);
			}
		}
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSubscriber#unsubscribe(java.lang.String)
	 */
	public void unsubscribe(String topic) {
		Integer count = topics.get(topic);
		if (null != count) {
			if (count > 1) {
				--count;
			}
			else {
				topics.remove(topic);
			}

			for (int i=0; i<connectedPublishers.size(); i++) {
				MwDirectPublisher publisher = connectedPublishers.get(i);
				publisher.removeQueue(topic, queue);
			}
		}
	}
	
	/**
	 * Receive.
	 *
	 * @param topic the topic
	 * @return the byte[]
	 */
	public byte[] receive(byte[] topic) {
		Bundle bundle = receive(false);
		byte[] bundleTopic = bundle.getTopic().getBytes();
		System.arraycopy(bundleTopic, 0, topic, 0, bundleTopic.length);
		return bundle.getData();
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwReceiver#receive(boolean)
	 */
	public Bundle receive(boolean deserialize) {
		Bundle bundle = null;
		
		try {
			bundle = queue.take();
		}
		catch (Exception e) {
		}
		return bundle;
	}
}