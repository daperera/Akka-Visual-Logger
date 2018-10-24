package com.akkaVisualizor.akkaModel;

import java.util.function.Supplier;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorType {

	private final Class<? extends ActorRef> typeClass;
	private final Supplier<Props> typeConstructor;
	private final ActorSystem system;
	
	public ActorType(Class<? extends ActorRef> typeClass, Supplier<Props> typeConstructor, ActorSystem system) {
		this.typeClass = typeClass;
		this.typeConstructor = typeConstructor;
		this.system = system;
	}

	public Class<? extends ActorRef> getTypeClass() {
		return typeClass;
	}
	
	public ActorRef getInstance(String name) {
		return system.actorOf(typeConstructor.get(), name);
	}
}
