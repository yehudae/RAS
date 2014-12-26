package com.rafael.sdk.middleware;

import com.rafael.sdk.util.Bundle;

public interface MwReceiver {
	public Bundle receive(boolean deserialize);
}
