package com.akkaVisualizor.akkaModel;

public class Event {
	
	public enum EventType {
		SEND, RECEIVE, CREATE, DELETE;
	}
	
	private final EventType type;
	private final Actor actor;
	private final ActorState previousState;
	private final ActorState nextState;
	
	
	
	public Event(EventType type, Actor actor, ActorState previousState, ActorState nextState) {
		this.type = type;
		this.actor = actor;
		this.previousState = previousState;
		this.nextState = nextState;
	}

	public EventType getType() {
		return type;
	}

	public Actor getActor() {
		return actor;
	}

	public ActorState getPreviousState() {
		return previousState;
	}

	public ActorState getNextState() {
		return nextState;
	}
	
	
	
}
