package com.rafael.sdk.component;

import com.rafael.sdk.activity.ApplicationActivity;



public abstract class Application extends Component implements Runnable {

	public void run() {
		onStart();
	}
	
	public void init() {		
		onCreate();
	}
	
	public void initActivity(ApplicationActivity applicationActivity) {
		applicationActivity.init(requesters, replier, publisherUp, publisherDown, subscriberDown, database, true); 
	}	
}