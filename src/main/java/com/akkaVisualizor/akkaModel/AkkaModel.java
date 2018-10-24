package com.akkaVisualizor.akkaModel;

import com.akkaVisualizor.Context;

public class AkkaModel {
	public AkkaModel(Context context) {
	}

	public Actor createActor(String name) throws Exception {
		if(name.equals(""))
			throw new RuntimeException("InvalidName");
		return new Actor();
	}

	public Channel createChannel(Object source, Object target) {
		return null;
	}

	public Message createMessage(Actor source, Actor target) {
		return new Message(source, target, null);
	}
}
