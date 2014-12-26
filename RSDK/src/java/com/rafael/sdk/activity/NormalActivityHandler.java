package com.rafael.sdk.activity;

import java.util.Vector;

public class NormalActivityHandler extends Thread implements ActivityHandler {
	
	private ActivityHandlerLogic activityHandlerLogic;
	
	public NormalActivityHandler(ActivityHandlerLogic activityHandlerLogic, int priority) {
		super(activityHandlerLogic);
		setPriority(priority);
		this.activityHandlerLogic = activityHandlerLogic;
	}

	@Override
	public void putActivity(String topic, Activity activity, boolean runPeriodically) {
		activityHandlerLogic.putActivity(topic, activity, runPeriodically);
	}

	@Override
	public void removeActivity(String topic) {
		activityHandlerLogic.removeActivity(topic);
	}

	@Override
	public void addActivitiesCompletionListener(ActivitiesCompletionListener activitiesCompletionListener) {
		activityHandlerLogic.addActivitiesCompletionListener(activitiesCompletionListener);
	}

	@Override
	public Vector<ActivityDescriptor> getRemovedActivities() {
		return activityHandlerLogic.getRemovedActivities();
	}

	@Override
	public Activity getActivity(String topic) {
		return activityHandlerLogic.getActivity(topic);
	}
}
