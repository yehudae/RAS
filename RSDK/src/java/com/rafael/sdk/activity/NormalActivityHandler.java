package com.rafael.sdk.activity;

import java.util.Vector;

// TODO: Auto-generated Javadoc
/**
 * The Class NormalActivityHandler.
 */
public class NormalActivityHandler extends Thread implements ActivityHandler {
	
	/** The activity handler logic. */
	private ActivityHandlerLogic activityHandlerLogic;
	
	/**
	 * Instantiates a new normal activity handler.
	 *
	 * @param activityHandlerLogic the activity handler logic
	 * @param priority the priority
	 */
	public NormalActivityHandler(ActivityHandlerLogic activityHandlerLogic, int priority) {
		super(activityHandlerLogic);
		setPriority(priority);
		this.activityHandlerLogic = activityHandlerLogic;
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.activity.ActivityHandler#putActivity(java.lang.String, com.rafael.sdk.activity.Activity, boolean)
	 */
	@Override
	public void putActivity(String topic, Activity activity, boolean runPeriodically) {
		activityHandlerLogic.putActivity(topic, activity, runPeriodically);
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.activity.ActivityHandler#removeActivity(java.lang.String)
	 */
	@Override
	public void removeActivity(String topic) {
		activityHandlerLogic.removeActivity(topic);
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.activity.ActivityHandler#addActivitiesCompletionListener(com.rafael.sdk.activity.ActivitiesCompletionListener)
	 */
	@Override
	public void addActivitiesCompletionListener(ActivitiesCompletionListener activitiesCompletionListener) {
		activityHandlerLogic.addActivitiesCompletionListener(activitiesCompletionListener);
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.activity.ActivityHandler#getRemovedActivities()
	 */
	@Override
	public Vector<ActivityDescriptor> getRemovedActivities() {
		return activityHandlerLogic.getRemovedActivities();
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.activity.ActivityHandler#getActivity(java.lang.String)
	 */
	@Override
	public Activity getActivity(String topic) {
		return activityHandlerLogic.getActivity(topic);
	}
}
