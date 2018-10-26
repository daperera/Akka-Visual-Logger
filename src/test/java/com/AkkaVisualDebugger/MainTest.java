package com.AkkaVisualDebugger;

import com.akkaVisualizor.AkkaVisualDebugger;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Identify;

public class MainTest {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		
		AkkaVisualDebugger log = AkkaVisualDebugger.create(system);
		
		System.out.println("logging new ActorType");
		
		log.logActorType("default1", DefaultActor::props);
		
		
		sleep(1000);
		System.out.println("logging new ActorType");
		log.logActorType("default2", DefaultActor::props);
		
		sleep(1000);
		System.out.println("logging new ActorType");
		log.logActorType("default3", DefaultActor::props);
		
		sleep(5000);
		ActorRef printer = system.actorOf(PrintAllActor.props(), "printer");
		identifyActor(system, printer, "/user/*");
		//system.terminate();
	}
	
	public static void identifyActor(ActorSystem system, ActorRef printer, String path) {
		ActorSelection actorList = system.actorSelection(path);
		actorList.tell(new Identify(1), printer); // force actors to identify themselves
		sleep(100);
	}


	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
