package com.akkaVisualizor.akkaModel;

import java.util.function.Supplier;

import com.akkaVisualizor.Context;

import akka.actor.ActorRef;
import akka.actor.Props;

public class AkkaModel {
	public AkkaModel(Context context) {
	}

	public Actor createActor(String name) throws Exception {
		if(name.equals(""))
			throw new RuntimeException("InvalidName");
		return new Actor();
	}

	public Channel createChannel(Object source, Object target) {
		return null;
	}

	public Message createMessage(Actor source, Actor target) {
		return new Message(source, target, null);
	}
	
	public void createActor(ActorRef actor, String name) {
		
	}	
	
	public void deleteActor(ActorRef actor) {
		
	}
	
	public void sendMessage(Object m, ActorRef source, ActorRef target) {
		
	}
	
	public void receiveMessage(Object m, ActorRef source, ActorRef target) {
		
	}
	
	public void logActorType(Class<ActorRef> typeClass, Supplier<Props> typeConstructor) {
		
	}
	
	public void logMessageType(String name, Supplier<Object> messageConstructor) {
		
	}
}
