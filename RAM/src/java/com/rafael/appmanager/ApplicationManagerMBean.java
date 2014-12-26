package com.rafael.appmanager;

public interface ApplicationManagerMBean 
{
	abstract public void startApplication(String appClassName);
	abstract public void stopApplication(String appClassName);
}
