package com.rafael.sdk.component;

import com.rafael.sdk.activity.AsyncActivityHandlerLogic;
import com.rafael.sdk.activity.NormalActivityHandler;
import com.rafael.sdk.activity.RealTimeActivityHandler;
import com.rafael.sdk.activity.SyncActivityHandlerLogic;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class Service.
 */
public abstract class Service extends Component{
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#init()
	 */
	public void init() {
		if (isSyncHandlerRealTime) {
			syncActivityHandler = new RealTimeActivityHandler(new SyncActivityHandlerLogic(replier, false), upSyncThreadPriority);
		}
		else {
			syncActivityHandler = new NormalActivityHandler(new SyncActivityHandlerLogic(replier, false), upSyncThreadPriority);
		}

		switch (upAsyncThreadCount) {
		case -1:
			upActivityHandler =  new NormalActivityHandler(new AsyncActivityHandlerLogic(subscriberUp, upAsyncThreadFactory, true), upAsyncThreadPriority);
			break;
		case 0:
			upActivityHandler =  new NormalActivityHandler(new SyncActivityHandlerLogic(subscriberUp, true), upAsyncThreadPriority);
			break;
		default:
			upActivityHandler =  new NormalActivityHandler(new AsyncActivityHandlerLogic(subscriberUp, upAsyncThreadFactory, upAsyncThreadCount, true), upAsyncThreadPriority);
			break;
		}

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
		
		onCreate();
	}

	/**
	 * Publish up.
	 *
	 * @param bundle the bundle
	 */
	public void publishUp(Bundle bundle) {
		publisherUp.send(bundle);
	}
	
	/**
	 * Publish down.
	 *
	 * @param bundle the bundle
	 */
	public void publishDown(Bundle bundle) {
		publisherDown.send(bundle);
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onCreate()
	 */
	@Override
	public void onCreate(){
		
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onStart()
	 */
	@Override
	public void onStart(){
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onResume()
	 */
	@Override
	public void onResume(){
		
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onPause()
	 */
	@Override
	public void onPause()	{
		
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onStop()
	 */
	@Override
	public void onStop()	{
		
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onRestart()
	 */
	@Override
	public void onRestart()	{
		
	}

	/* (non-Javadoc)
	 * @see com.rafael.sdk.component.Component#onDestroy()
	 */
	@Override
	public void onDestroy(){
	}
}