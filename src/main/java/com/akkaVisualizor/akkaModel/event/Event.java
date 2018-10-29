package com.akkaVisualizor.akkaModel.event;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;

public abstract class Event {
	
	protected final Actor actor;
	protected final ActorState previousState;
	protected final ActorState currentState;
	
	public Event(Actor actor, ActorState previousState, ActorState nextState) {
		this.actor = actor;
		this.previousState = previousState;
		this.currentState = nextState;
	}
	
	public abstract void redo(Context context);
	
	public abstract void undo(Context context);
	
}
