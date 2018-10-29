package com.akkaVisualizor.akkaModel.event;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;
import com.akkaVisualizor.akkaModel.Message;

public class SendEvent extends Event {

	private final Message message;

	public SendEvent(Actor actor, Message message, ActorState previousState, ActorState nextState) {
		super(actor, previousState, nextState);
		this.message = message;
	}
	
	@Override
	public void redo(Context context) {
		context.getAkkaModel().sendMessage(message, message.getSource(), message.getTarget());
		actor.loadState(currentState);
	}

	@Override
	public void undo(Context context) {
		actor.loadState(previousState);
	}

}
