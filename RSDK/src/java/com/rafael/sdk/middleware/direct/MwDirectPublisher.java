package com.rafael.sdk.middleware.direct;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class MwDirectPublisher.
 */
public class MwDirectPublisher extends MwPublisher {

	/** The queues. */
	private Map<String, Vector<BlockingQueue<Bundle>>> queues = new HashMap<String, Vector<BlockingQueue<Bundle>>>();
	
	/**
	 * Instantiates a new mw direct publisher.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param component the component
	 */
	public MwDirectPublisher(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
	
	/**
	 * Put queue.
	 *
	 * @param topic the topic
	 * @param queue the queue
	 */
	public void putQueue(String topic, BlockingQueue<Bundle> queue) {
		Vector<BlockingQueue<Bundle>> queueList = queues.get(topic);
		if (null == queueList) {
			queueList = new Vector<BlockingQueue<Bundle>>();
			queues.put(topic, queueList);
		}
		queueList.add(queue);
	}
	
	/**
	 * Removes the queue.
	 *
	 * @param topic the topic
	 * @param queue the queue
	 */
	public void removeQueue(String topic, BlockingQueue<Bundle> queue) {
		Vector<BlockingQueue<Bundle>> queueList = queues.get(topic);
		if (null != queueList) {
			queueList.remove(queue);
		}
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwPublisher#send(java.lang.String, byte[])
	 */
	public boolean send(String topic, byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		Bundle bundle = new Bundle(topic, byteBuffer);
		send(bundle);
		return true;
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.middleware.MwSender#send(com.rafael.sdk.util.Bundle)
	 */
	public Bundle send(Bundle bundle) {
		try {
			Vector<BlockingQueue<Bundle>> queueList = queues.get(bundle.getTopic());
			if (null != queueList) {
				for (int i=0; i<queueList.size(); i++) {
					queueList.get(i).put(bundle);
				}
			}
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		}
		return bundle;
	}
}