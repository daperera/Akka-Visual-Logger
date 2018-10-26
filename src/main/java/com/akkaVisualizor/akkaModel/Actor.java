package com.akkaVisualizor.akkaModel;

import akka.actor.ActorRef;

public class Actor {

	private final ActorRef actorRef;
	private final String name; 
	
	public Actor(ActorRef actorRef, String name, ActorType actorType) {
		this.actorRef = actorRef;
		this.name = name;
	}

	public ActorRef getActorRef() {
		return actorRef;
	}

	public String getName() {
		return name;
	}

}