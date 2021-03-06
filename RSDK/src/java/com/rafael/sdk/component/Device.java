package com.rafael.sdk.component;


import com.rafael.sdk.activity.AsyncActivityHandlerLogic;
import com.rafael.sdk.activity.NormalActivityHandler;
import com.rafael.sdk.activity.RealTimeActivityHandler;
import com.rafael.sdk.activity.SyncActivityHandlerLogic;
import com.rafael.sdk.util.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class Device.
 */
public abstract class Device extends Component {
	
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
			upActivityHandler = new NormalActivityHandler(new AsyncActivityHandlerLogic(subscriberUp, upAsyncThreadFactory, true), upAsyncThreadPriority);
			break;
		case 0:
			upActivityHandler = new NormalActivityHandler(new SyncActivityHandlerLogic(subscriberUp, true), upAsyncThreadPriority);
			break;
		default:
			upActivityHandler =  new NormalActivityHandler(new AsyncActivityHandlerLogic(subscriberUp, upAsyncThreadFactory, upAsyncThreadCount, true), upAsyncThreadPriority);
			break;
		}

		switch (downAsyncThreadCount) {
		case -1:
			downActivityHandler = new NormalActivityHandler(new AsyncActivityHandlerLogic(subscriberDown, downAsyncThreadFactory, false), downAsyncThreadPriority);
			break;
		case 0:
			downActivityHandler = new NormalActivityHandler(new SyncActivityHandlerLogic(subscriberDown, false), downAsyncThreadPriority);
			break;
		default:
			downActivityHandler = new NormalActivityHandler(new AsyncActivityHandlerLogic(subscriberDown, downAsyncThreadFactory, downAsyncThreadCount, false), downAsyncThreadPriority);
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
}