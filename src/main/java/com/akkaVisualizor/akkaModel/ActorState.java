package com.akkaVisualizor.akkaModel;

import java.util.Map;

public class ActorState {
	private final Map<String, Object> fields;
	private Class<? extends akka.actor.Actor> actorClass;
	private final akka.actor.Actor actor;
	
	public ActorState(Map<String, Object> fields, Class<? extends akka.actor.Actor> actorClass, akka.actor.Actor actor) {
		this.fields = fields;
		this.actorClass = actorClass;
		this.actor = actor;
	}

	public Class<? extends akka.actor.Actor> getActorClass() {
		return actorClass;
	}

	public void setActorClass(Class<? extends akka.actor.Actor> actorClass) {
		this.actorClass = actorClass;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public akka.actor.Actor getActor() {
		return actor;
	}

}
