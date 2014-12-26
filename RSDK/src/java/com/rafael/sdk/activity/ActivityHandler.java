package com.rafael.sdk.activity;

import java.util.Vector;

public interface ActivityHandler {

	public void putActivity(String topic, Activity activity, boolean runPeriodically);
	public void removeActivity(String topic);
	public Activity getActivity(String topic);
	public void addActivitiesCompletionListener(ActivitiesCompletionListener activitiesCompletionListener);
	public Vector<ActivityDescriptor> getRemovedActivities();
	public void start();
}
