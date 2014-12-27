package com.rafael.sdk.activity;

// TODO: Auto-generated Javadoc
/**
 * The Class ActivityDescriptor.
 */
public class ActivityDescriptor {
	
	/** The activity. */
	private Activity activity = null;
	
	/** The run periodically. */
	private boolean runPeriodically = true;
	
	/** The topic. */
	private String topic;
	
	/**
	 * Instantiates a new activity descriptor.
	 *
	 * @param topic the topic
	 * @param activity the activity
	 * @param runPeriodically the run periodically
	 */
	public ActivityDescriptor(String topic, Activity activity, boolean runPeriodically) {
		this.topic = topic;
		this.activity = activity;
		this.runPeriodically = runPeriodically;
	}
	
	/**
	 * Gets the activity.
	 *
	 * @return the activity
	 */
	public Activity getActivity() {
		return activity;
	}
	
	/**
	 * Run periodically.
	 *
	 * @return true, if successful
	 */
	public boolean runPeriodically() {
		return runPeriodically;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((activity == null) ? 0 : activity.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
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

	/**
	 * Gets the topic.
	 *
	 * @return the topic
	 */
	public String getTopic() {
		return topic;
	}
}
