package com.AkkaVisualDebugger.messageTest;

import com.akkaVisualizor.AkkaVisualLogger;

import akka.actor.ActorSystem;

public class MainTest {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		
		AkkaVisualLogger log = AkkaVisualLogger.create(system);
		
		System.out.println("create Sender and Receiver actor, and send query or reply message from one to another");
		log.logActorType("Sender", Sender::props);
		log.logActorType("Receiver", Receiver::props);
		log.logMessageType("query", () -> new Query("query content"));
		log.logMessageType("reply", () -> new Reply("reply content"));
		
	}
	
}
