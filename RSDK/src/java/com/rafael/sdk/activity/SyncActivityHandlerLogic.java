package com.rafael.sdk.activity;

import com.rafael.sdk.middleware.MwReceiver;

public class SyncActivityHandlerLogic extends ActivityHandlerLogic {
	
	public SyncActivityHandlerLogic(MwReceiver receiver, boolean deserialize) {
		super(receiver, deserialize);
	}
	
	public void doRun(Activity activity) {
		activity.run();
	}
}
