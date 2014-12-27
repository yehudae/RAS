package com.rafael.sdk.component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import com.rafael.sdk.activity.Activity;
import com.rafael.sdk.activity.ActivityHandler;
import com.rafael.sdk.middleware.MwPublisher;
import com.rafael.sdk.middleware.MwReplier;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.middleware.MwSubscriber;
import com.rafael.sdk.util.Bundle;
import com.rafael.sdk.util.MwRequesterPoolFactory;
import com.rafael.sdk.util.Pool;


// TODO: Auto-generated Javadoc
/**
 * The Class Component.
 */
public abstract class Component {
	
	/** The publisher up. */
	protected MwPublisher publisherUp = null;
	
	/** The publisher down. */
	protected MwPublisher publisherDown = null;
	
	/** The subscriber up. */
	protected MwSubscriber subscriberUp = null;
	
	/** The subscriber down. */
	protected MwSubscriber subscriberDown = null;
	
	/** The requesters. */
	protected Pool<MwRequester> requesters;
	
	/** The replier. */
	protected MwReplier replier = null;
	
	/** The up async thread count. */
	protected int upAsyncThreadCount = -1;
	
	/** The down async thread count. */
	protected int downAsyncThreadCount = -1;

	/** The sync activity handler. */
	protected ActivityHandler syncActivityHandler = null;
	
	/** The up activity handler. */
	protected ActivityHandler upActivityHandler = null;
	
	/** The down activity handler. */
	protected ActivityHandler downActivityHandler = null;
	
	/** The database. */
	protected ConcurrentHashMap<Object, Object> database = new ConcurrentHashMap<Object, Object>();

	/** The up async thread factory. */
	protected ThreadFactory upAsyncThreadFactory = Executors.defaultThreadFactory();
	
	/** The down async thread factory. */
	protected ThreadFactory downAsyncThreadFactory = Executors.defaultThreadFactory();
	
	/** The is sync handler real time. */
	protected boolean isSyncHandlerRealTime = false;
	
	/** The down async thread priority. */
	protected int downAsyncThreadPriority;
	
	/** The up async thread priority. */
	protected int upAsyncThreadPriority;
	
	/** The up sync thread priority. */
	protected int upSyncThreadPriority;

	/**
	 * On create.
	 */
	public abstract void onCreate();
	
	/**
	 * On start.
	 */
	public abstract void onStart();
	
	/**
	 * On pause.
	 */
	public abstract void onPause();
	
	/**
	 * On resume.
	 */
	public abstract void onResume();
	
	/**
	 * On restart.
	 */
	public abstract void onRestart();
	
	/**
	 * On stop.
	 */
	public abstract void onStop();
	
	/**
	 * On destroy.
	 */
	public abstract void onDestroy();	
	
	/**
	 * Inits the.
	 */
	public abstract void init();
	
	/**
	 * Inits the.
	 *
	 * @param downAsyncThreadPriority the down async thread priority
	 * @param upAsyncThreadPriority the up async thread priority
	 * @param upSyncThreadPriority the up sync thread priority
	 * @param isSyncHandlerRealTime the is sync handler real time
	 */
	public void init(int downAsyncThreadPriority, int upAsyncThreadPriority, int upSyncThreadPriority, boolean isSyncHandlerRealTime) {
		this.downAsyncThreadPriority = downAsyncThreadPriority;
		this.upAsyncThreadPriority = upAsyncThreadPriority;
		this.upSyncThreadPriority = upSyncThreadPriority;
		this.isSyncHandlerRealTime = isSyncHandlerRealTime;
		init();
	}
	
	/**
	 * Start.
	 */
	public void start(){
		if (null != syncActivityHandler) {
			syncActivityHandler.start();
		}
		
		if (null != downActivityHandler) {
			downActivityHandler.start();
		}
		
		if (null != upActivityHandler) {
			upActivityHandler.start();
		}
		
		onStart();
	}
		
	/**
	 * Sets the up publisher.
	 *
	 * @param publisherUp the new up publisher
	 */
	public void setUpPublisher(MwPublisher publisherUp) {
		if (null == this.publisherUp) {
			this.publisherUp = publisherUp;
		}
	}

	/**
	 * Sets the down publisher.
	 *
	 * @param publisherDown the new down publisher
	 */
	public void setDownPublisher(MwPublisher publisherDown) {
		if (null == this.publisherDown) {
			this.publisherDown = publisherDown;
		}
	}

	/**
	 * Sets the down subscriber.
	 *
	 * @param subscriberDown the new down subscriber
	 */
	public void setDownSubscriber(MwSubscriber subscriberDown) {
		if (null == this.subscriberDown) {
			this.subscriberDown = subscriberDown;
		}
	}

	/**
	 * Sets the up subscriber.
	 *
	 * @param subscriberUp the new up subscriber
	 */
	public void setUpSubscriber(MwSubscriber subscriberUp) {
		if (null == this.subscriberUp) {
			this.subscriberUp = subscriberUp;
		}
	}

	/**
	 * Creates the requesters.
	 *
	 * @param connectionType the connection type
	 * @param connectionString the connection string
	 * @param capacity the capacity
	 * @param increaseDynamically the increase dynamically
	 * @param connect the connect
	 */
	public void createRequesters(String connectionType, String connectionString, int capacity, boolean increaseDynamically, boolean connect) {
		requesters = new Pool<MwRequester>(new MwRequesterPoolFactory(connectionType, connectionString, connect, this), capacity, increaseDynamically);
	}

	/**
	 * Sets the replier.
	 *
	 * @param replier the new replier
	 */
	public void setReplier(MwReplier replier) {
		if (null == this.replier) {
			this.replier = replier;
		}
	}
	
	/**
	 * Sets the up async thread count.
	 *
	 * @param upAsyncThreadCount the new up async thread count
	 */
	public void setUpAsyncThreadCount(int upAsyncThreadCount) {
		this.upAsyncThreadCount = upAsyncThreadCount;
	}
	
	/**
	 * Sets the down async thread count.
	 *
	 * @param downAsyncThreadCount the new down async thread count
	 */
	public void setDownAsyncThreadCount(int downAsyncThreadCount) {
		this.downAsyncThreadCount = downAsyncThreadCount;
	}
	
	/**
	 * Sets the up async thread pool factory.
	 *
	 * @param upAsyncThreadFactory the new up async thread pool factory
	 */
	public void setUpAsyncThreadPoolFactory(ThreadFactory upAsyncThreadFactory) {
		this.upAsyncThreadFactory = upAsyncThreadFactory;
	}
	
	/**
	 * Sets the down async thread pool factory.
	 *
	 * @param downAsyncThreadFactory the new down async thread pool factory
	 */
	public void setDownAsyncThreadPoolFactory(ThreadFactory downAsyncThreadFactory) {
		this.downAsyncThreadFactory = downAsyncThreadFactory;
	}
	
	/**
	 * Sets the sync handler realtime.
	 *
	 * @param isRealtime the new sync handler realtime
	 */
	public void setSyncHandlerRealtime(boolean isRealtime) {
		this.isSyncHandlerRealTime = isRealtime;
	}

	/**
	 * Put sync activity.
	 *
	 * @param topic the topic
	 * @param activity the activity
	 * @param runPeriodically the run periodically
	 */
	public void putSyncActivity(String topic, Activity activity, boolean runPeriodically) {
		activity.init(requesters, replier, publisherUp, publisherDown, subscriberDown, database, true); 
		syncActivityHandler.putActivity(topic, activity, runPeriodically);
	}
	
	/**
	 * Gets the sync activity.
	 *
	 * @param topic the topic
	 * @return the sync activity
	 */
	public Activity getSyncActivity(String topic) {
		return syncActivityHandler.getActivity(topic);
	}

	/**
	 * Put up async activity.
	 *
	 * @param topic the topic
	 * @param activity the activity
	 * @param runPeriodically the run periodically
	 */
	public void putUpAsyncActivity(String topic, Activity activity, boolean runPeriodically) {
		subscriberUp.subscribe(topic);
		activity.init(requesters, replier, publisherUp, publisherDown, subscriberDown, database, false);
		upActivityHandler.putActivity(topic, activity, runPeriodically);
	}

	/**
	 * Gets the up async activity.
	 *
	 * @param topic the topic
	 * @return the up async activity
	 */
	public Activity getUpAsyncActivity(String topic) {
		return upActivityHandler.getActivity(topic);
	}

	/**
	 * Put down async activity.
	 *
	 * @param topic the topic
	 * @param activity the activity
	 * @param runPeriodically the run periodically
	 */
	public void putDownAsyncActivity(String topic, Activity activity, boolean runPeriodically) {
		subscriberDown.subscribe(topic);
		activity.init(requesters, replier, publisherUp, publisherDown, subscriberDown, database, false);
		downActivityHandler.putActivity(topic, activity, runPeriodically);
	}
	
	/**
	 * Gets the down async activity.
	 *
	 * @param topic the topic
	 * @return the down async activity
	 */
	public Activity getDownAsyncActivity(String topic) {		
		return downActivityHandler.getActivity(topic);
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
	 * Request.
	 *
	 * @param bundle the bundle
	 * @return the bundle
	 */
	protected Bundle request(Bundle bundle){
		MwRequester requester = requesters.allocate();
		Bundle retBundle = null;
		
		if (null != requester) {
			retBundle = requester.send(bundle);
			requesters.deallocate(requester);
		}
		
		return retBundle;
	}
}
