package com.akkaVisualizor.akkaModel.event;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;

public class DeleteEvent extends Event {

	public DeleteEvent(Actor a, ActorState prevState) {
		super(a, prevState, null);
	}

	@Override
	public void redo(Context context) {
		context.getAkkaModel().deleteActor(actor);
	}

	@Override
	public void undo(Context context) {
		context.getAkkaModel().createActor(actor);
		actor.loadState(previousState);
	}

}
