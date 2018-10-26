package com.akkaVisualizor.akkaModel;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import akka.actor.ActorRef;

public class Actor {

	private final ActorRef actorRef;
	private final String name; 
	private ActorState currentState;

	public Actor(ActorRef actorRef, String name, ActorType actorType) {
		this.actorRef = actorRef;
		this.name = name;
		currentState = null;
	}

	public ActorRef getActorRef() {
		return actorRef;
	}

	public String getName() {
		return name;
	}

	public ActorState actualizeState() {
		Class<? extends ActorRef> actorClass = actorRef.getClass();
		Map<String, Object> fields = new HashMap<>();
		// take a 'snapshot' of the fields of the actor, and of their values
		for(Field f : actorClass.getDeclaredFields()) {
			try {
				fields.put(f.getName(), f.get(actorRef));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
			}
		}		
		return new ActorState(fields, actorClass, actorRef);
	}
	
	public ActorState getCurrentState() {
		return currentState;
	}

	public void loadState(ActorState state) {
		Map<String, Object> fields = state.getFields();
		Class<? extends ActorRef> actorClass = state.getActorClass();
		// set actor fields to the stored value
		for(String fieldName : fields.keySet()) {
			try {
				Field f = actorClass.getDeclaredField(fieldName);
				Object storedValue = fields.get(fieldName); 
				f.set(this, storedValue);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		currentState = state;
	}

}