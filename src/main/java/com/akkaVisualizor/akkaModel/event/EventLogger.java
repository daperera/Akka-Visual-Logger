package com.akkaVisualizor.akkaModel.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorState;
import com.akkaVisualizor.akkaModel.Channel;
import com.akkaVisualizor.akkaModel.Message;

public class EventLogger {

	private static final int maxHistorySize = 1000;
	private boolean log;
	private final Context context;
	private final List<Event> history;
	private final Map<Actor, List<ActorState>> actorToStateList;
	private final Map<Actor, Integer> actorToStateIndex;
	private int historyIndex;
	private int currentIndex;


	public EventLogger(Context context) {
		this.context = context;
		history = new ArrayList<>(); 
		actorToStateList = new HashMap<>();
		actorToStateIndex = new HashMap<>();
		historyIndex = 0;
		currentIndex = 0;
		log = true;
	}

	public void reset() {
		currentIndex = 0;
		log = false;
	}
	
	public void forward() {
		if(currentIndex<historyIndex) {
			Event e = history.get(currentIndex);
			e.redo(context);
			currentIndex++;
		}
	}

	public void rewind() {
		if(currentIndex>0) {
			currentIndex--;
			Event e = history.get(currentIndex);
			e.undo(context);
		}
	}
	
	public void logActorCreated(Actor a) {
		ActorState currState = getCurrState(a);
		Event e = new CreateEvent(a, currState);
		logEvent(e, a, currState);
	}
	
	public void logActorDeleted(Actor a) {
		ActorState prevState = getPrevState(a);
		Event e = new DeleteEvent(a, prevState);
		logEvent(e, a, null);
	}
	
	public void logMessageSent(Message m) {
		Actor a = m.getSource();
		ActorState prevState = getPrevState(a);
		ActorState currState = getCurrState(a);
		Event e = new SendEvent(a, m, prevState, currState);
		logEvent(e, a, currState);
	}
	
	public void logMessageReceived(Message m) {
		Actor a = m.getTarget();
		ActorState prevState = getPrevState(a);
		ActorState currState = getCurrState(a);
		Event e = new ReceiveEvent(a, m, prevState, currState);
		logEvent(e, a, currState);
	}
	
	public void logInternalEvent(Actor a) {
		ActorState prevState = getPrevState(a);
		ActorState currState = getCurrState(a);
		Event e = new InternalEvent(a, prevState, currState);
		logEvent(e, a, currState);
	}
	
	public void logChannelCreated(Channel channel) {
		Actor a = channel.getSource();
		ActorState prevState = getPrevState(a);
		ActorState currState = getCurrState(a);
		Event e = new CreateChannelEvent(a, channel, prevState, currState);
		logEvent(e, a, currState);
	}

	public void logChannelDeleted(Channel channel) {
		Actor a = channel.getSource();
		ActorState prevState = getPrevState(a);
		ActorState currState = getCurrState(a);
		Event e = new DeleteChannelEvent(a, channel, prevState, currState);
		logEvent(e, a, currState);		
	}
	
	private ActorState getPrevState(Actor a) {
		return actorToStateList.get(a).get(actorToStateIndex.get(a));
	}
	
	private ActorState getCurrState(Actor a) {
		a.actualizeState();
		return a.getCurrentState();
	}
	
	private void logEvent(Event e, Actor a, ActorState currState) {
		if(log) {
			
			// actualize actorToStateList map
			List<ActorState> stateList = actorToStateList.get(a);
			if(stateList==null) { // if actor has not been registered yet
				stateList = new ArrayList<>();
			}
			stateList.add(currState);
			actorToStateList.put(a, stateList);
			
			// actualize actorToStateList map
			Integer stateIndex = actorToStateIndex.get(a);
			if(stateIndex == null) {
				stateIndex=-1;
			}
			actorToStateIndex.put(a, stateIndex+1);
			
			// actualize history
			synchronized(history) {
				history.add(e);
			}
			
			// actualize index in history
			historyIndex++;
			if(historyIndex>=maxHistorySize) {
				log = false;
			}
		}
	}

}
