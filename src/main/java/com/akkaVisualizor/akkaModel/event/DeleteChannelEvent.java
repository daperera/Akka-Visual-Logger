package com.akkaVisualizor.akkaModel.event;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;
import com.akkaVisualizor.akkaModel.Channel;

public class DeleteChannelEvent extends Event {
	
	private final Channel channel;
	
	public DeleteChannelEvent(Actor actor, Channel channel, ActorState previousState, ActorState currentState) {
		super(actor, previousState, currentState);
		this.channel = channel;
	}

	@Override
	public void redo(Context context) {
		actor.loadState(currentState);
		context.getAkkaModel().deleteChannel(channel);
	}

	@Override
	public void undo(Context context) {
		context.getAkkaModel().createChannel(channel);
		actor.loadState(previousState);
	}
}
