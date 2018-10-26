package com.AkkaVisualDebugger.objectDynamicAdditionTest;

import com.akkaVisualizor.AkkaVisualDebugger;

import akka.actor.ActorSystem;

public class MainTest {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		
		AkkaVisualDebugger log = AkkaVisualDebugger.create(system);
		
		System.out.println("loging actor type");
		log.logActorType("default1", DefaultActor::props);
		
		sleep(3000);
		System.out.println("loging actor type");
		log.logActorType("default2", DefaultActor::props);
		
		sleep(3000);
		System.out.println("loging message type");
		log.logMessageType("default3", () -> new String(""));
		
	}
	
	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
