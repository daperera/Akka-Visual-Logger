package com.akkaVisualizor.akkaModel;

public class Channel {

	private final Actor source, target;

	public Channel(Actor source, Actor target) {
		this.source = source;
		this.target = target;
	}

	public Actor getSource() {
		return source;
	}

	public Actor getTarget() {
		return target;
	}

}
