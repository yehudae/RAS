package com.rafael.sdk.component;

import java.util.HashMap;
import java.util.Vector;
import com.rafael.sdk.activity.ActivitiesCompletionListener;
import com.rafael.sdk.activity.ActivityDescriptor;
import com.rafael.sdk.activity.ApplicationActivity;
import com.rafael.sdk.activity.AsyncActivityHandlerLogic;
import com.rafael.sdk.activity.NormalActivityHandler;
import com.rafael.sdk.activity.SyncActivityHandlerLogic;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class Manager.
 */
public abstract class Manager extends Component implements ActivitiesCompletionListener {
	
	/** The subscribed topics. */
	private HashMap<String, Integer> subscribedTopics = null;

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#init()
	 */
	public void init() {
		subscribedTopics = new HashMap<String, Integer>();

		switch (downAsyncThreadCount) {
		case -1:
			downActivityHandler =  new NormalActivityHandler(new AsyncActivityHandlerLogic(subscriberDown, downAsyncThreadFactory, true), downAsyncThreadPriority);
			break;
		case 0:
			downActivityHandler =  new NormalActivityHandler(new SyncActivityHandlerLogic(subscriberDown, true), downAsyncThreadPriority);
			break;
		default:
			downActivityHandler =  new NormalActivityHandler(new AsyncActivityHandlerLogic(subscriberDown, downAsyncThreadFactory, downAsyncThreadCount, true), downAsyncThreadPriority);
			break;
		}

		downActivityHandler.addActivitiesCompletionListener(this);
	}

	/**
	 * Publish.
	 *
	 * @param bundle the bundle
	 */
	protected synchronized void publish(Bundle bundle){
		publisherDown.send(bundle);
	}

	/**
	 * Publish with result.
	 *
	 * @param activity the activity
	 * @param bundle the bundle
	 */
	protected synchronized void publishWithResult(ApplicationActivity activity, Bundle bundle)	{
		bundle.generateRequestId();
		String topic = bundle.getTopicWithRequestId();
		downActivityHandler.putActivity(topic, activity, false);
		subscribe(topic);
		publish(bundle);
	}

	/**
	 * Register.
	 *
	 * @param activity the activity
	 * @param bundle the bundle
	 * @return the bundle
	 */
	protected synchronized Bundle register(ApplicationActivity activity, Bundle bundle) {
		registerWithoutResult(activity,bundle);
		return request(bundle);
	}
	
	/**
	 * Register without result.
	 *
	 * @param activity the activity
	 * @param bundle the bundle
	 * @return the bundle
	 */
	protected synchronized Bundle registerWithoutResult(ApplicationActivity activity, Bundle bundle) {
		String topic = bundle.getTopic();
		downActivityHandler.putActivity(topic, activity, true);
		subscribe(topic);
		return bundle;
	}
		
	/**
	 * Unregister.
	 *
	 * @param activity the activity
	 * @param bundle the bundle
	 * @return the bundle
	 */
	protected synchronized Bundle unregister(ApplicationActivity activity, Bundle bundle)	{
		unregisterWithoutResult(activity, bundle);
		return request(bundle);
	}
	
	/**
	 * Unregister without result.
	 *
	 * @param activity the activity
	 * @param bundle the bundle
	 * @return the bundle
	 */
	protected synchronized Bundle unregisterWithoutResult(ApplicationActivity activity, Bundle bundle)	{
		String topic = bundle.getTopic();
		unsubscribe(topic);
		downActivityHandler.removeActivity(topic);
		return bundle;
	}

	/**
	 * Subscribe.
	 *
	 * @param topic the topic
	 * @return the int
	 */
	private int subscribe(String topic)	{
		Integer count = subscribedTopics.get(topic);

		if (count == null){
			count = new Integer(1);
			subscribedTopics.put(topic, count);
			subscriberDown.subscribe(topic);
		} 
		else{
			count++;
		}

		return count;
	}

	/**
	 * Unsubscribe.
	 *
	 * @param topic the topic
	 * @return the int
	 */
	private int unsubscribe(String topic) {
		Integer count = subscribedTopics.get(topic);

		if (count != null){
			--count;
			if (count == 0)	{
				subscribedTopics.remove(topic);
				subscriberDown.unsubscribe(topic);
			}
		} else	{
			count = new Integer(0);
		}

		return count;
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.activity.ActivitiesCompletionListener#onActivitiesCompletion()
	 */
	public void onActivitiesCompletion() {
		Vector<ActivityDescriptor> removedActivities = downActivityHandler.getRemovedActivities();
		for (int i=0; i<removedActivities.size(); i++) {
			ActivityDescriptor activityDescriptor = removedActivities.get(i);
			unsubscribe(activityDescriptor.getTopic());
			downActivityHandler.removeActivity(activityDescriptor.getTopic());
		}
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onCreate()
	 */
	@Override
	public void onCreate() {
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onStart()
	 */
	@Override
	public void onStart() {
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onResume()
	 */
	@Override
	public void onResume() {
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onPause()
	 */
	@Override
	public void onPause() {
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onStop()
	 */
	@Override
	public void onStop() {
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onRestart()
	 */
	@Override
	public void onRestart() {
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onDestroy()
	 */
	@Override
	public void onDestroy() {
	}
}