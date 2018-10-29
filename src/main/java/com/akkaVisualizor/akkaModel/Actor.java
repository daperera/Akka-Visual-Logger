package com.akkaVisualizor.akkaModel;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Observable;

import akka.actor.ActorRef;

public class Actor extends Observable {

	private final akka.actor.Actor akkaActor;
	private final String name; 
	private ActorState currentState;

	public Actor(akka.actor.Actor akkaActor, String name, ActorType actorType) {
		this.akkaActor = akkaActor;
		this.name = name;
		actualizeState();
	}

	public ActorRef getActorRef() {
		return akkaActor.self();
	}

	public String getName() {
		return name;
	}

	public void actualizeState() {
		Class<? extends akka.actor.Actor> actorClass = akkaActor.getClass();
		Map<String, Object> fields = new HashMap<>();
		// take a 'snapshot' of the fields of the actor, and of their values
		System.out.println("detected class " + actorClass.getName()); 
		for(Field f : actorClass.getDeclaredFields()) {
			System.out.println("field read : " + f.getName());
			try {
				fields.put(f.getName(), f.get(akkaActor));
				System.out.println("field successfully extracted");
			} catch (IllegalArgumentException | IllegalAccessException e) {
				//e.printStackTrace();
			}
		}		
		currentState = new ActorState(fields, actorClass, akkaActor); 
		
		// notify observers
		setChanged();
		notifyObservers(currentState);
	}
	
	public ActorState getCurrentState() {
		return currentState;
	}

	public void loadState(ActorState state) {
		Map<String, Object> fields = state.getFields();
		Class<? extends akka.actor.Actor> actorClass = state.getActorClass();
		// set actor fields to the stored value
		for(String fieldName : fields.keySet()) {
			try {
				Field f = actorClass.getDeclaredField(fieldName);
				Object storedValue = fields.get(fieldName); 
				f.set(this, storedValue);
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}
		currentState = state;
	}

}