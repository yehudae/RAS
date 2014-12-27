package com.rafael.framework.factory;

import com.rafael.sdk.component.Component;

// TODO: Auto-generated Javadoc
/**
 * A factory for creating Activity objects.
 */
public abstract class ActivityFactory {
	
	/**
	 * Creates a new Activity object.
	 *
	 * @param component the component
	 */
	public abstract void createActivities(Component component);
}
