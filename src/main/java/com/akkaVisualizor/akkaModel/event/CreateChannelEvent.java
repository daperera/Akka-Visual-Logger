package com.akkaVisualizor.akkaModel.event;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;
import com.akkaVisualizor.akkaModel.Channel;

public class CreateChannelEvent extends Event {

	private final Channel channel;
	
	public CreateChannelEvent(Actor actor, Channel channel, ActorState previousState, ActorState currentState) {
		super(actor, previousState, currentState);
		this.channel = channel;
	}

	@Override
	public void redo(Context context) {
		context.getAkkaModel().createChannel(channel);
		actor.loadState(currentState);
	}

	@Override
	public void undo(Context context) {
		context.getAkkaModel().deleteChannel(channel);
		actor.loadState(previousState);
	}

}
