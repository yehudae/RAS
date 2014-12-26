package com.rafael.sdk.middleware.zmq;

import org.zeromq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.util.Bundle;

public class MwZmqRequester extends MwRequester {
	
	private ZMQ.Socket requester = null;
	
	public MwZmqRequester (ZMQ.Context context,String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		// create the requester
	    requester = context.socket (ZMQ.REQ);
	}

	public synchronized void connect(String connection) {		
	    requester.connect(connection);
	}
	
	public synchronized void disconnect(String connection) {
		requester.disconnect(connection);
	}
	
	public synchronized Bundle send(Bundle bundle) {
		byte[] data = Bundle.serialize(bundle);
		requester.send(data, 0, bundle.size(), 0);
		data = new byte[Bundle.MAX_BUNDLE_SIZE];
		int size = requester.recv(data, 0, data.length, 0);
		return Bundle.deserialize(data, size);
	}
}
