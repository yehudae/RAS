package com.rafael.sdk.activity;

import com.rafael.sdk.middleware.MwReceiver;

// TODO: Auto-generated Javadoc
/**
 * The Class SyncActivityHandlerLogic.
 */
public class SyncActivityHandlerLogic extends ActivityHandlerLogic {
	
	/**
	 * Instantiates a new sync activity handler logic.
	 *
	 * @param receiver the receiver
	 * @param deserialize the deserialize
	 */
	public SyncActivityHandlerLogic(MwReceiver receiver, boolean deserialize) {
		super(receiver, deserialize);
	}
	
	/* (non-Javadoc)
	 * @see com.rafael.sdk.activity.ActivityHandlerLogic#doRun(com.rafael.sdk.activity.Activity)
	 */
	public void doRun(Activity activity) {
		activity.run();
	}
}
