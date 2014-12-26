package com.rafael.framework;


public interface FrameworkManagerMBean 
{
	abstract public void loadDevice(String deviceClassName, int numOfThreads);
	abstract public void startDevice(String deviceClassName);
	abstract public void stopDevice(String deviceClassName);
}