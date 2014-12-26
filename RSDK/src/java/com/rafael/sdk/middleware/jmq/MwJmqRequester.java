package com.rafael.sdk.middleware.jmq;

import org.jmq.Ctx;
import org.jmq.Msg;
import org.jmq.SocketBase;
import org.jmq.ZMQ;

import com.rafael.sdk.component.Component;
import com.rafael.sdk.middleware.MwRequester;
import com.rafael.sdk.util.Bundle;

public class MwJmqRequester extends MwRequester {
	
	private SocketBase requester = null;
	
	public MwJmqRequester (Ctx context, String connectionType, String connectionString, Component component) {
		super(connectionType, connectionString, component);
		// create the requester
	    requester = context.create_socket(ZMQ.ZMQ_REQ);
	}

	public synchronized void connect(String connection) {		
	    requester.connect(connection);
	}
	
	public synchronized void disconnect(String connection) {
		requester.term_endpoint(connection);
	}
	
	public Bundle send (Bundle bundle) {
		byte[] data = Bundle.serialize(bundle);
		requester.send(new Msg(data, bundle.size()), 0);
		Msg msg = requester.recv(0);
		return Bundle.deserialize(msg.data(), msg.size());
	}
}
