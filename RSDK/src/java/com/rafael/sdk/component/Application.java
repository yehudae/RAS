package com.rafael.sdk.component;

import com.rafael.sdk.activity.ApplicationActivity;



// TODO: Auto-generated Javadoc
/**
 * The Class Application.
 */
public abstract class Application extends Component implements Runnable {

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		onStart();
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#init()
	 */
	public void init() {		
		onCreate();
	}
	
	/**
	 * Inits the activity.
	 *
	 * @param applicationActivity the application activity
	 */
	public void initActivity(ApplicationActivity applicationActivity) {
		applicationActivity.init(requesters, replier, publisherUp, publisherDown, subscriberDown, database, true); 
	}	
}