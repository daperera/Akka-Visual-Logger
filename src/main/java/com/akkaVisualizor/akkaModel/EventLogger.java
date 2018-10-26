package com.akkaVisualizor.akkaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Event.EventType;

import akka.actor.ActorRef;

public class EventLogger {

	private final Context context;
	private final List<Event> history;
	private final Map<Actor, ActorState> actorToPreviousState;
	private int index;


	public EventLogger(Context context) {
		this.context = context;
		history = new ArrayList<>(); 
		actorToPreviousState = new HashMap<>();
		index = -1;
	}

	public void log(EventType t, Actor a) {
		ActorState previousState = actorToPreviousState.get(a);
		a.getCurrentState();
		ActorState nextState = a.getCurrentState();
		Event e = new Event(t, a, previousState, nextState);
		synchronized(history) {
			history.add(e);
		}
		index++;
	}

	public void reset() {
		index = 0;
	}

	public void forward() {
		Event e = history.get(index);
		Actor actor = e.getActor();
		actor.loadState(e.getNextState());
		ActorRef actorRef = actor.getActorRef();
		String name = actor.getName();
		switch(e.getType()) {
		case CREATE:
			context.getAkkaModel().logActorCreated(actor.getActorRef(), actor.getName());
			break;
		case DELETE:
			context.getAkkaModel().logActorDeleted(actorRef);
			break;
		case RECEIVE:
			context.getAkkaModel().logMessageReceived(, source, actor);
			break;
		case SEND:
			break;
		default:
			(new Exception("forwarding to unknown Event")).printStackTrace();
			break;
		}
		index++;
	}

	public void rewind() {
		Event e = history.get(index);
		switch(e.getType()) {
		case CREATE:
			break;
		case DELETE:
			break;
		case RECEIVE:
			break;
		case SEND:
			break;
		default:
			(new Exception("forwarding to unknown Event")).printStackTrace();
			break;
		}
		index--;
	}
}
