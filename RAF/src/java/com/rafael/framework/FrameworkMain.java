package com.rafael.framework;

import com.rafael.framework.factory.DynamicActivityFactory;


// TODO: Auto-generated Javadoc
/**
 * The Class FrameworkMain.
 */
public class FrameworkMain {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args){
		// register the framework manager to the MBean
		FrameworkManager frameworkManager = new FrameworkManager("framework.xml");
		frameworkManager.init(new DynamicActivityFactory());
	}
}