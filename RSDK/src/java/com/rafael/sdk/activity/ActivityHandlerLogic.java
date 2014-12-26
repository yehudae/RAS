package com.rafael.sdk.activity;

import java.util.Vector;
import java.util.concurrent.ConcurrentHashMap;

import com.rafael.sdk.middleware.MwReceiver;
import com.rafael.sdk.util.Bundle;

public abstract class ActivityHandlerLogic implements Runnable {
	
	private MwReceiver receiver = null;
	private ConcurrentHashMap<String, ActivityDescriptor> activities = null;
	private Vector<ActivitiesCompletionListener> activitiesCompletionListeners = null;
	private Vector<ActivityDescriptor> removedActivities = null;
	private boolean deserialize = false;
	
	public abstract void doRun(Activity activity);
	
	public ActivityHandlerLogic(MwReceiver receiver, boolean deserialize) {
		this.receiver = receiver;
		activities = new ConcurrentHashMap<String, ActivityDescriptor>();
		removedActivities = new Vector<ActivityDescriptor>();
		activitiesCompletionListeners = new Vector<ActivitiesCompletionListener>();
		this.deserialize = deserialize;
	}
	
	public synchronized void putActivity(String topic, Activity activity, boolean runPeriodically) {
		activities.put(topic, new ActivityDescriptor(topic, activity, runPeriodically));
	}
	
	public synchronized void removeActivity(String topic) {
		activities.remove(topic);
	}
	
	public Activity getActivity(String topic) {
		Activity activity = null;
		ActivityDescriptor activityDescriptor = activities.get(topic);
		if (null != activityDescriptor) {
			activity = activityDescriptor.getActivity();
		}
		
		return activity;
	}
	
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
	
	public void addActivitiesCompletionListener(ActivitiesCompletionListener listener) {
		activitiesCompletionListeners.add(listener);
	}
	
	public void removeActivitiesCompletionListener(ActivitiesCompletionListener listener) {
		activitiesCompletionListeners.remove(listener);
	}
	
	public void onActivitiesCompletion() {
		for (int i=0; i<activitiesCompletionListeners.size(); i++) {
			activitiesCompletionListeners.get(i).onActivitiesCompletion();
		}
	}
	
	public Vector<ActivityDescriptor> getRemovedActivities() {
		return removedActivities;
	}
}
