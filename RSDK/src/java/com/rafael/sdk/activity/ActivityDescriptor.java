package com.rafael.sdk.activity;

public class ActivityDescriptor {
	
	private Activity activity = null;
	private boolean runPeriodically = true;
	private String topic;
	
	public ActivityDescriptor(String topic, Activity activity, boolean runPeriodically) {
		this.topic = topic;
		this.activity = activity;
		this.runPeriodically = runPeriodically;
	}
	
	public Activity getActivity() {
		return activity;
	}
	
	public boolean runPeriodically() {
		return runPeriodically;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activity == null) ? 0 : activity.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		ActivityDescriptor other = (ActivityDescriptor) obj;
		if (activity == null) {
			if (other.activity != null) {
				return false;
			}
		} 
		else if (!activity.equals(other.activity)) {
			return false;
		}
		return true;
	}

	public String getTopic() {
		return topic;
	}
}
