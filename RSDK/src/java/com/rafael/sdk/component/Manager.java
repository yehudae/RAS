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

public abstract class Manager extends Component implements ActivitiesCompletionListener {
	
	private HashMap<String, Integer> subscribedTopics = null;

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

	protected synchronized void publish(Bundle bundle){
		publisherDown.send(bundle);
	}

	protected synchronized void publishWithResult(ApplicationActivity activity, Bundle bundle)	{
		bundle.generateRequestId();
		String topic = bundle.getTopicWithRequestId();
		downActivityHandler.putActivity(topic, activity, false);
		subscribe(topic);
		publish(bundle);
	}

	protected synchronized Bundle register(ApplicationActivity activity, Bundle bundle) {
		registerWithoutResult(activity,bundle);
		return request(bundle);
	}
	
	protected synchronized Bundle registerWithoutResult(ApplicationActivity activity, Bundle bundle) {
		String topic = bundle.getTopic();
		downActivityHandler.putActivity(topic, activity, true);
		subscribe(topic);
		return bundle;
	}
		
	protected synchronized Bundle unregister(ApplicationActivity activity, Bundle bundle)	{
		unregisterWithoutResult(activity, bundle);
		return request(bundle);
	}
	
	protected synchronized Bundle unregisterWithoutResult(ApplicationActivity activity, Bundle bundle)	{
		String topic = bundle.getTopic();
		unsubscribe(topic);
		downActivityHandler.removeActivity(topic);
		return bundle;
	}

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
	
	public void onActivitiesCompletion() {
		Vector<ActivityDescriptor> removedActivities = downActivityHandler.getRemovedActivities();
		for (int i=0; i<removedActivities.size(); i++) {
			ActivityDescriptor activityDescriptor = removedActivities.get(i);
			unsubscribe(activityDescriptor.getTopic());
			downActivityHandler.removeActivity(activityDescriptor.getTopic());
		}
	}
	
	@Override
	public void onCreate() {
	}

	@Override
	public void onStart() {
	}

	@Override
	public void onResume() {
	}

	@Override
	public void onPause() {
	}

	@Override
	public void onStop() {
	}

	@Override
	public void onRestart() {
	}

	@Override
	public void onDestroy() {
	}
}