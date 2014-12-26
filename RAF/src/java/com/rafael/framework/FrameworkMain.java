package com.rafael.framework;

import com.rafael.framework.factory.DynamicActivityFactory;


public class FrameworkMain {
	
	public static void main(String[] args){
		// register the framework manager to the MBean
		FrameworkManager frameworkManager = new FrameworkManager("framework.xml");
		frameworkManager.init(new DynamicActivityFactory());
	}
}