package com.akkaVisualizor.akkaModel.event;

import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;
import com.akkaVisualizor.utils.Context;

public class InternalEvent extends Event {

	public InternalEvent(Actor actor, ActorState prevState, ActorState currState) {
		super(actor, prevState, currState);
	}

	@Override
	public void redo(Context context) {
		actor.loadState(currentState);
	}

	@Override
	public void undo(Context context) {
		actor.loadState(previousState);
	}

}
