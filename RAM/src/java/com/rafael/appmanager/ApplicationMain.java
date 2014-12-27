package com.rafael.appmanager;

import com.rafael.appmanager.ApplicationManager;

// TODO: Auto-generated Javadoc
/**
 * The Class ApplicationMain.
 */
public class ApplicationMain {
	
	/**
	 * The main method.
	 *
	 * @param args the arguments
	 */
	public static void main(String[] args) {
		// create an application manager
		ApplicationManager applicationManager = new ApplicationManager("application.xml");
		applicationManager.init();
	}
}
