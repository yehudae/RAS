package com.rafael.sdk.activity;

// TODO: Auto-generated Javadoc
/**
 * The listener interface for receiving activitiesCompletion events.
 * The class that is interested in processing a activitiesCompletion
 * event implements this interface, and the object created
 * with that class is registered with a component using the
 * component's <code>addActivitiesCompletionListener<code> method. When
 * the activitiesCompletion event occurs, that object's appropriate
 * method is invoked.
 *
 * @see ActivitiesCompletionEvent
 */
public interface ActivitiesCompletionListener {
	
	/**
	 * On activities completion.
	 */
	public void onActivitiesCompletion();
}
