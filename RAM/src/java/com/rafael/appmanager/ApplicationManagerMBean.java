package com.rafael.appmanager;

// TODO: Auto-generated Javadoc
/**
 * The Interface ApplicationManagerMBean.
 */
public interface ApplicationManagerMBean 
{
	
	/**
	 * Start application.
	 *
	 * @param appClassName the app class name
	 */
	abstract public void startApplication(String appClassName);
	
	/**
	 * Stop application.
	 *
	 * @param appClassName the app class name
	 */
	abstract public void stopApplication(String appClassName);
}
