package com.akkaVisualizor.akkaModel.event;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;
import com.akkaVisualizor.akkaModel.Message;

public class ReceiveEvent extends Event {

	private final Message message;
	
	public ReceiveEvent(Actor actor, Message message, ActorState prevState, ActorState currState) {
		super(actor, prevState, currState);
		this.message = message;
	}

	@Override
	public void redo(Context context) {
		context.getAkkaModel().receiveMessage(message);
		actor.loadState(currentState);
	}

	@Override
	public void undo(Context context) {
		actor.loadState(previousState);
	}

}
