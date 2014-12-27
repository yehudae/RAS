package com.rafael.framework;


// TODO: Auto-generated Javadoc
/**
 * The Interface FrameworkManagerMBean.
 */
public interface FrameworkManagerMBean 
{
	
	/**
	 * Load device.
	 *
	 * @param deviceClassName the device class name
	 * @param numOfThreads the num of threads
	 */
	abstract public void loadDevice(String deviceClassName, int numOfThreads);
	
	/**
	 * Start device.
	 *
	 * @param deviceClassName the device class name
	 */
	abstract public void startDevice(String deviceClassName);
	
	/**
	 * Stop device.
	 *
	 * @param deviceClassName the device class name
	 */
	abstract public void stopDevice(String deviceClassName);
}