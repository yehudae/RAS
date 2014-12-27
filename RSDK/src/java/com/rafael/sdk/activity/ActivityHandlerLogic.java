package com.rafael.sdk.activity;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.rafael.sdk.middleware.MwReceiver;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class ActivityHandlerLogic.
 */
public abstract class ActivityHandlerLogic implements Runnable {
	
	/** The receiver. */
	private MwReceiver receiver = null;
	
	/** The activities. */
	private ConcurrentHashMap<String, ActivityDescriptor> activities = null;
	
	/** The activities completion listeners. */
	private Vector<ActivitiesCompletionListener> activitiesCompletionListeners = null;
	
	/** The removed activities. */
	private Vector<ActivityDescriptor> removedActivities = null;
	
	/** The deserialize. */
	private boolean deserialize = false;
	
	/**
	 * Do run.
	 *
	 * @param activity the activity
	 */
	public abstract void doRun(Activity activity);
	
	/**
	 * Instantiates a new activity handler logic.
	 *
	 * @param receiver the receiver
	 * @param deserialize the deserialize
	 */
	public ActivityHandlerLogic(MwReceiver receiver, boolean deserialize) {
		this.receiver = receiver;
		activities = new ConcurrentHashMap<String, ActivityDescriptor>();
		removedActivities = new Vector<ActivityDescriptor>();
		activitiesCompletionListeners = new Vector<ActivitiesCompletionListener>();
		this.deserialize = deserialize;
	}
	
	/**
	 * Put activity.
	 *
	 * @param topic the topic
	 * @param activity the activity
	 * @param runPeriodically the run periodically
	 */
	public synchronized void putActivity(String topic, Activity activity, boolean runPeriodically) {
		activities.put(topic, new ActivityDescriptor(topic, activity, runPeriodically));
	}
	
	/**
	 * Removes the activity.
	 *
	 * @param topic the topic
	 */
	public synchronized void removeActivity(String topic) {
		activities.remove(topic);
	}
	
	/**
	 * Gets the activity.
	 *
	 * @param topic the topic
	 * @return the activity
	 */
	public Activity getActivity(String topic) {
		Activity activity = null;
		ActivityDescriptor activityDescriptor = activities.get(topic);
		if (null != activityDescriptor) {
			activity = activityDescriptor.getActivity();
		}
		
		return activity;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		while (true) {
			Bundle bundle = receiver.receive(deserialize);
			if (null == bundle) {
				System.out.println("ActivityHandler.run() - receiver return null bundle");
				continue;
			}
			
			ActivityDescriptor activityDescriptor = activities.get(bundle.getTopic());
			if (null == activityDescriptor) {
				System.out.println("ActivityHandler.run() - no activity found to handle topic " + bundle.getTopic());
				continue;
			}

			Activity activity = activityDescriptor.getActivity();
			activity.putBundle(new Bundle(bundle));
			doRun(activity);
			if (!activityDescriptor.runPeriodically()) {
				removedActivities.add(activityDescriptor);
			}
						
			onActivitiesCompletion();
			removedActivities.clear();
		}
	}
	
	/**
	 * Adds the activities completion listener.
	 *
	 * @param listener the listener
	 */
	public void addActivitiesCompletionListener(ActivitiesCompletionListener listener) {
		activitiesCompletionListeners.add(listener);
	}
	
	/**
	 * Removes the activities completion listener.
	 *
	 * @param listener the listener
	 */
	public void removeActivitiesCompletionListener(ActivitiesCompletionListener listener) {
		activitiesCompletionListeners.remove(listener);
	}
	
	/**
	 * On activities completion.
	 */
	public void onActivitiesCompletion() {
		for (int i=0; i<activitiesCompletionListeners.size(); i++) {
			activitiesCompletionListeners.get(i).onActivitiesCompletion();
		}
	}
	
	/**
	 * Gets the removed activities.
	 *
	 * @return the removed activities
	 */
	public Vector<ActivityDescriptor> getRemovedActivities() {
		return removedActivities;
	}
}
