package com.AkkaVisualDebugger.actorEnumerationTest;

import com.akkaVisualizor.AkkaVisualLogger;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;
import akka.actor.Identify;

public class MainTest {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		
		AkkaVisualLogger log = AkkaVisualLogger.create(system);
		
		log.logActorType("default", DefaultActor::props);
		System.out.println("try creating actors (10 sec left before print)");
			
		sleep(10000);
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
