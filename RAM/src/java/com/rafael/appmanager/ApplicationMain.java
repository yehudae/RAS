package com.rafael.appmanager;

import com.rafael.appmanager.ApplicationManager;

public class ApplicationMain {
	
	public static void main(String[] args) {
		// create an application manager
		ApplicationManager applicationManager = new ApplicationManager("application.xml");
		applicationManager.init();
	}
}
