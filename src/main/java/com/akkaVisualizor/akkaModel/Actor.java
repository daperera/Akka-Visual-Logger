package com.akkaVisualizor.akkaModel;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;

import com.akkaVisualizor.Context;

import akka.actor.ActorRef;

public class Actor extends Observable {

	private final akka.actor.Actor akkaActor;
	private final String name; 
	private final List<Channel> channelList;
	private ActorState currentState;
	private final Context context;

	public Actor(Context context, akka.actor.Actor akkaActor, String name, ActorType actorType) {
		this.context = context;
		this.akkaActor = akkaActor;
		this.name = name;
		this.channelList = new ArrayList<>();
		//actualizeState();
	}

	public ActorRef getActorRef() {
		return akkaActor.self();
	}

	public String getName() {
		return name;
	}
	
	public void addChannel(Channel channel) {
		if(!channelList.contains(channel)) {
			channelList.add(channel);
		}
		context.getAkkaModel().getChannelList().add(channel);
	}
	
	public void removeChannel(Channel channel) {
		channelList.remove(channel);
		context.getAkkaModel().getChannelList().remove(channel);
	}
	
	public List<Channel> getChannelList() {
		return channelList;
	}
	
	public void actualizeState() {
		Class<? extends akka.actor.Actor> actorClass = akkaActor.getClass();
		Map<String, Object> fields = new HashMap<>();
		// take a 'snapshot' of the fields of the actor, and of their values
		for(Field f : actorClass.getDeclaredFields()) {
			try {
				fields.put(f.getName(), f.get(akkaActor));
			} catch (IllegalArgumentException | IllegalAccessException e) {
				// do nothing
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
				// do nothing
			}
		}
		currentState = state;
	}
	
}