package com.akkaVisualizor.akkaModel;

import java.util.Map;

import akka.actor.ActorRef;

public class ActorState {
	private final Map<String, Object> fields;
	private Class<? extends ActorRef> actorClass;
	private final ActorRef actor;
	
	public ActorState(Map<String, Object> fields, Class<? extends ActorRef> actorClass, ActorRef actor) {
		this.fields = fields;
		this.actorClass = actorClass;
		this.actor = actor;
	}

	public Class<? extends ActorRef> getActorClass() {
		return actorClass;
	}

	public void setActorClass(Class<? extends ActorRef> actorClass) {
		this.actorClass = actorClass;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public ActorRef getActor() {
		return actor;
	}

}
