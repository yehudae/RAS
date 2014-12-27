package com.rafael.sdk.activity;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Interface ActivityHandler.
 */
public interface ActivityHandler {

	/**
	 * Put activity.
	 *
	 * @param topic the topic
	 * @param activity the activity
	 * @param runPeriodically the run periodically
	 */
	public void putActivity(String topic, Activity activity, boolean runPeriodically);
	
	/**
	 * Removes the activity.
	 *
	 * @param topic the topic
	 */
	public void removeActivity(String topic);
	
	/**
	 * Gets the activity.
	 *
	 * @param topic the topic
	 * @return the activity
	 */
	public Activity getActivity(String topic);
	
	/**
	 * Adds the activities completion listener.
	 *
	 * @param activitiesCompletionListener the activities completion listener
	 */
	public void addActivitiesCompletionListener(ActivitiesCompletionListener activitiesCompletionListener);
	
	/**
	 * Gets the removed activities.
	 *
	 * @return the removed activities
	 */
	public Vector<ActivityDescriptor> getRemovedActivities();
	
	/**
	 * Start.
	 */
	public void start();
}
