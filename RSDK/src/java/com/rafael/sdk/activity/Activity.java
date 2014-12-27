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

// TODO: Auto-generated Javadoc
/**
 * The Class Activity.
 */
public abstract class Activity implements Runnable {
	
	/** The bundles. */
	protected ConcurrentLinkedQueue<Bundle> bundles = null;
	
	/** The database. */
	protected ConcurrentHashMap<Object, Object> database = null;
	
	/** The requesters. */
	protected Pool<MwRequester> requesters;
	
	/** The replier. */
	protected MwReplier replier = null;
	
	/** The publisher up. */
	protected MwPublisher publisherUp = null;
	
	/** The publisher down. */
	protected MwPublisher publisherDown = null;
	
	/** The subscriber down. */
	protected MwSubscriber subscriberDown = null;
	
	/** The is sync. */
	protected boolean isSync;
	
	/** The priority. */
	protected int priority = 5;
	
	/** The system manager. */
	protected SystemManager systemManager = SystemManager.getInstance();


	/**
	 * Instantiates a new activity.
	 */
	public Activity() {
		bundles = new ConcurrentLinkedQueue<Bundle>();
	}
	
	/**
	 * Inits the.
	 *
	 * @param requesters the requesters
	 * @param replier the replier
	 * @param publisherUp the publisher up
	 * @param publisherDown the publisher down
	 * @param subscriberDown the subscriber down
	 * @param database the database
	 * @param isSync the is sync
	 */
	public void init(Pool<MwRequester> requesters, MwReplier replier, MwPublisher publisherUp, MwPublisher publisherDown, MwSubscriber subscriberDown, ConcurrentHashMap<Object, Object> database, boolean isSync) {
		this.requesters = requesters;
		this.replier = replier;
		this.publisherUp = publisherUp;
		this.publisherDown = publisherDown;
		this.subscriberDown = subscriberDown;
		this.database = database;
		this.isSync = isSync;
	}
	
	/**
	 * Reply.
	 *
	 * @param bundle the bundle
	 */
	protected void reply(Bundle bundle) {
		if (null != replier) {
			replier.send(bundle);
		}
	}

	/**
	 * Put bundle.
	 *
	 * @param bundle the bundle
	 * @return true, if successful
	 */
	public boolean putBundle(Bundle bundle) {
		return bundles.add(bundle);
	}
	
	/**
	 * Gets the bundle.
	 *
	 * @return the bundle
	 */
	public Bundle getBundle() {
		return bundles.poll();
	}
	
	/**
	 * Put to database.
	 *
	 * @param key the key
	 * @param value the value
	 */
	public void putToDatabase(Object key, Object value) {
		database.put(key, value);
	}
	
	/**
	 * Gets the from database.
	 *
	 * @param key the key
	 * @return the from database
	 */
	public Object getFromDatabase(Object key) {
		return database.get(key);
	}
	
	/**
	 * Clear database.
	 */
	public void clearDatabase() {
		database.clear();
	}
	
	/**
	 * Removes the from database.
	 *
	 * @param key the key
	 */
	public void removeFromDatabase(Object key) {
		database.remove(key);
	}
	
	/**
	 * Checks if is database contains value.
	 *
	 * @param value the value
	 * @return true, if is database contains value
	 */
	public boolean isDatabaseContainsValue(Object value) {
		return database.containsValue(value);
	}

	/**
	 * Checks if is database contains key.
	 *
	 * @param key the key
	 * @return true, if is database contains key
	 */
	public boolean isDatabaseContainsKey(Object key) {
		return database.containsKey(key);
	}
	
	/**
	 * Checks if is database empty.
	 *
	 * @return true, if is database empty
	 */
	public boolean isDatabaseEmpty() {
		return database.isEmpty();
	}
	
	/**
	 * Publish down.
	 *
	 * @param bundle the bundle
	 */
	public void publishDown(Bundle bundle) {
		if(null != publisherDown) {
			publisherDown.send(bundle);
		}
	}
	
	/**
	 * Request.
	 *
	 * @param bundle the bundle
	 * @return the bundle
	 */
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
	
	/**
	 * Subscribe down.
	 *
	 * @param topic the topic
	 */
	public void subscribeDown(String topic) {
		if (null != subscriberDown) {
			subscriberDown.subscribe(topic);
		}
	}

	/**
	 * Unsubscribe down.
	 *
	 * @param topic the topic
	 */
	public void unsubscribeDown(String topic) {
		if (null != subscriberDown) {
			subscriberDown.unsubscribe(topic);
		}
	}
	
	/**
	 * Publish up.
	 *
	 * @param bundle the bundle
	 */
	public void publishUp(Bundle bundle) {
		if (null != publisherUp){
			publisherUp.send(bundle);
		}
	}
	
	/**
	 * Sets the priority.
	 *
	 * @param priority the new priority
	 */
	public void setPriority(int priority) {
		this.priority = priority;
	}
	
	// Log a message, with no arguments.
	/**
	 * Log.
	 *
	 * @param level the level
	 * @param msg the msg
	 */
	public void log(Level level, String msg) {
		systemManager.rasLog(level, getClass().getName(), Thread.currentThread().getStackTrace()[2].getMethodName(), (int) Thread.currentThread().getId(), msg);
	}
	 
	/**
	 * Do activity.
	 *
	 * @param bundle the bundle
	 */
	public abstract void doActivity(Bundle bundle);
}
