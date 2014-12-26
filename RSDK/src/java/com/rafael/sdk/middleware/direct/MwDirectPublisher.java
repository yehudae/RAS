package com.rafael.sdk.middleware.direct;

import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.util.Bundle;

public class MwDirectPublisher extends MwPublisher {

	private Map<String, Vector<BlockingQueue<Bundle>>> queues = new HashMap<String, Vector<BlockingQueue<Bundle>>>();
	
	/**
	 * 
	 * @param queue
	 */
	public MwDirectPublisher(String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
	}
	
	public void putQueue(String topic, BlockingQueue<Bundle> queue) {
		Vector<BlockingQueue<Bundle>> queueList = queues.get(topic);
		if (null == queueList) {
			queueList = new Vector<BlockingQueue<Bundle>>();
			queues.put(topic, queueList);
		}
		queueList.add(queue);
	}
	
	public void removeQueue(String topic, BlockingQueue<Bundle> queue) {
		Vector<BlockingQueue<Bundle>> queueList = queues.get(topic);
		if (null != queueList) {
			queueList.remove(queue);
		}
	}

	public boolean send(String topic, byte[] bytes) {
		ByteBuffer byteBuffer = ByteBuffer.wrap(bytes);
		Bundle bundle = new Bundle(topic, byteBuffer);
		send(bundle);
		return true;
	}
	
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