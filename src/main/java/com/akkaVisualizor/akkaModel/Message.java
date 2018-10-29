package com.akkaVisualizor.akkaModel;

public class Message {

	private boolean delivered;
	private final Actor source, target;
	private final Object message;
	
	public Message(Actor source, Actor target, Object message) {
		delivered = false;
		this.source = source;
		this.target = target;
		this.message = message;
	}
	
	public Actor getTarget() {
		return target;
	}

	public Object getMessage() {
		return message;
	}

	public void setDelivered() {
		delivered = true;
	}
	public boolean isDelivered() {
		return delivered;
	}

	public Actor getSource() {
		return source;
	}

}
