package com.rafael.sdk.activity;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;

import com.rafael.sdk.SystemManager;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.util.Bundle;
import com.rafael.sdk.util.Pool;

public abstract class Activity implements Runnable {
	
	protected ConcurrentLinkedQueue<Bundle> bundles = null;
	protected ConcurrentHashMap<Object, Object> database = null;
	protected Pool<MwRequester> requesters;
	protected MwReplier replier = null;
	protected MwPublisher publisherUp = null;
	protected MwPublisher publisherDown = null;
	protected MwSubscriber subscriberDown = null;
	protected boolean isSync;
	protected int priority = 5;
	protected SystemManager systemManager = SystemManager.getInstance();


	public Activity() {
		bundles = new ConcurrentLinkedQueue<Bundle>();
	}
	
	public void init(Pool<MwRequester> requesters, MwReplier replier, MwPublisher publisherUp, MwPublisher publisherDown, MwSubscriber subscriberDown, ConcurrentHashMap<Object, Object> database, boolean isSync) {
		this.requesters = requesters;
		this.replier = replier;
		this.publisherUp = publisherUp;
		this.publisherDown = publisherDown;
		this.subscriberDown = subscriberDown;
		this.database = database;
		this.isSync = isSync;
	}
	
	protected void reply(Bundle bundle) {
		if (null != replier) {
			replier.send(bundle);
		}
	}

	public boolean putBundle(Bundle bundle) {
		return bundles.add(bundle);
	}
	
	public Bundle getBundle() {
		return bundles.poll();
	}
	
	public void putToDatabase(Object key, Object value) {
		database.put(key, value);
	}
	
	public Object getFromDatabase(Object key) {
		return database.get(key);
	}
	
	public void clearDatabase() {
		database.clear();
	}
	
	public void removeFromDatabase(Object key) {
		database.remove(key);
	}
	
	public boolean isDatabaseContainsValue(Object value) {
		return database.containsValue(value);
	}

	public boolean isDatabaseContainsKey(Object key) {
		return database.containsKey(key);
	}
	
	public boolean isDatabaseEmpty() {
		return database.isEmpty();
	}
	
	public void publishDown(Bundle bundle) {
		if(null != publisherDown) {
			publisherDown.send(bundle);
		}
	}
	
	public Bundle request(Bundle bundle) {
		MwRequester requester = requesters.allocate();
		
		if (null != requester) {
			String connectionString = requester.getConnectionType() +  bundle.getDestination();
			
			requester.connect(connectionString);
			Bundle returnedBundle = requester.send(bundle);
			requester.disconnect(connectionString);
			requesters.deallocate(requester);
			return returnedBundle;
		}
		
		return null;
	}
	
	public void subscribeDown(String topic) {
		if (null != subscriberDown) {
			subscriberDown.subscribe(topic);
		}
	}

	public void unsubscribeDown(String topic) {
		if (null != subscriberDown) {
			subscriberDown.unsubscribe(topic);
		}
	}
	
	public void publishUp(Bundle bundle) {
		if (null != publisherUp){
			publisherUp.send(bundle);
		}
	}
	
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	// Log a message, with no arguments.
	public void log(Level level, String msg) {
		systemManager.rasLog(level, getClass().getName(), Thread.currentThread().getStackTrace()[2].getMethodName(), (int) Thread.currentThread().getId(), msg);
	}
	 
	public abstract void doActivity(Bundle bundle);
}
