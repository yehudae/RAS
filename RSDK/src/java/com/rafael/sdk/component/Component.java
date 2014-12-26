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


public abstract class Component {
	
	protected MwPublisher publisherUp = null;
	protected MwPublisher publisherDown = null;
	protected MwSubscriber subscriberUp = null;
	protected MwSubscriber subscriberDown = null;
	protected Pool<MwRequester> requesters;
	protected MwReplier replier = null;
	
	protected int upAsyncThreadCount = -1;
	protected int downAsyncThreadCount = -1;

	protected ActivityHandler syncActivityHandler = null;
	protected ActivityHandler upActivityHandler = null;
	protected ActivityHandler downActivityHandler = null;
	
	protected ConcurrentHashMap<Object, Object> database = new ConcurrentHashMap<Object, Object>();

	protected ThreadFactory upAsyncThreadFactory = Executors.defaultThreadFactory();
	protected ThreadFactory downAsyncThreadFactory = Executors.defaultThreadFactory();
	
	protected boolean isSyncHandlerRealTime = false;
	protected int downAsyncThreadPriority;
	protected int upAsyncThreadPriority;
	protected int upSyncThreadPriority;

	public abstract void onCreate();
	public abstract void onStart();
	public abstract void onPause();
	public abstract void onResume();
	public abstract void onRestart();
	public abstract void onStop();
	public abstract void onDestroy();	
	public abstract void init();
	
	public void init(int downAsyncThreadPriority, int upAsyncThreadPriority, int upSyncThreadPriority, boolean isSyncHandlerRealTime) {
		this.downAsyncThreadPriority = downAsyncThreadPriority;
		this.upAsyncThreadPriority = upAsyncThreadPriority;
		this.upSyncThreadPriority = upSyncThreadPriority;
		this.isSyncHandlerRealTime = isSyncHandlerRealTime;
		init();
	}
	
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
		
	public void setUpPublisher(MwPublisher publisherUp) {
		if (null == this.publisherUp) {
			this.publisherUp = publisherUp;
		}
	}

	public void setDownPublisher(MwPublisher publisherDown) {
		if (null == this.publisherDown) {
			this.publisherDown = publisherDown;
		}
	}

	public void setDownSubscriber(MwSubscriber subscriberDown) {
		if (null == this.subscriberDown) {
			this.subscriberDown = subscriberDown;
		}
	}

	public void setUpSubscriber(MwSubscriber subscriberUp) {
		if (null == this.subscriberUp) {
			this.subscriberUp = subscriberUp;
		}
	}

	public void createRequesters(String connectionType, String connectionString, int capacity, boolean increaseDynamically, boolean connect) {
		requesters = new Pool<MwRequester>(new MwRequesterPoolFactory(connectionType, connectionString, connect, this), capacity, increaseDynamically);
	}

	public void setReplier(MwReplier replier) {
		if (null == this.replier) {
			this.replier = replier;
		}
	}
	
	public void setUpAsyncThreadCount(int upAsyncThreadCount) {
		this.upAsyncThreadCount = upAsyncThreadCount;
	}
	
	public void setDownAsyncThreadCount(int downAsyncThreadCount) {
		this.downAsyncThreadCount = downAsyncThreadCount;
	}
	
	public void setUpAsyncThreadPoolFactory(ThreadFactory upAsyncThreadFactory) {
		this.upAsyncThreadFactory = upAsyncThreadFactory;
	}
	
	public void setDownAsyncThreadPoolFactory(ThreadFactory downAsyncThreadFactory) {
		this.downAsyncThreadFactory = downAsyncThreadFactory;
	}
	
	public void setSyncHandlerRealtime(boolean isRealtime) {
		this.isSyncHandlerRealTime = isRealtime;
	}

	public void putSyncActivity(String topic, Activity activity, boolean runPeriodically) {
		activity.init(requesters, replier, publisherUp, publisherDown, subscriberDown, database, true); 
		syncActivityHandler.putActivity(topic, activity, runPeriodically);
	}
	
	public Activity getSyncActivity(String topic) {
		return syncActivityHandler.getActivity(topic);
	}

	public void putUpAsyncActivity(String topic, Activity activity, boolean runPeriodically) {
		subscriberUp.subscribe(topic);
		activity.init(requesters, replier, publisherUp, publisherDown, subscriberDown, database, false);
		upActivityHandler.putActivity(topic, activity, runPeriodically);
	}

	public Activity getUpAsyncActivity(String topic) {
		return upActivityHandler.getActivity(topic);
	}

	public void putDownAsyncActivity(String topic, Activity activity, boolean runPeriodically) {
		subscriberDown.subscribe(topic);
		activity.init(requesters, replier, publisherUp, publisherDown, subscriberDown, database, false);
		downActivityHandler.putActivity(topic, activity, runPeriodically);
	}
	
	public Activity getDownAsyncActivity(String topic) {		
		return downActivityHandler.getActivity(topic);
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
