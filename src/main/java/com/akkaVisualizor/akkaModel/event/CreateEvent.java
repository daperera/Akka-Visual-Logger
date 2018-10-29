package com.akkaVisualizor.akkaModel.event;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;

public class CreateEvent extends Event{

	public CreateEvent(Actor a, ActorState currState) {
		super(a, null, currState);
	}

	@Override
	public void redo(Context context) {
		context.getAkkaModel().createActor(actor, actor.getName());
		actor.loadState(currentState);
	}

	@Override
	public void undo(Context context) {
		context.getAkkaModel().deleteActor(actor);
	}


}
