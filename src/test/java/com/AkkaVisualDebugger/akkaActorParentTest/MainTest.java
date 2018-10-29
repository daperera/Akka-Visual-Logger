package com.AkkaVisualDebugger.akkaActorParentTest;

import akka.actor.ActorRef;
import akka.actor.ActorSelection;
import akka.actor.ActorSystem;

public class MainTest {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();

		// create an actor form main
		ActorRef actor = system.actorOf(DefaultActor.props(), "createdByMain");
		sleep(10);
		
		// create an actor from another actor
		actor.tell(new CreateChild(), null);
		sleep(10);
		
		/* make them print their parent */
		
		// print first order path actors
		ActorSelection actorList = system.actorSelection("/user/*");
		actorList.tell(new PrintParent(), null); // force actors to identify themselves
		sleep(10);
		
		// print second order path actors
		actorList = system.actorSelection("/user/*/*");
		actorList.tell(new PrintParent(), null); // force actors to identify themselves
		sleep(10);
		
		// should return an error (no third order path actors)
		actorList = system.actorSelection("/user/*/*/*");
		actorList.tell(new PrintParent(), null); // force actors to identify themselves
		sleep(10);
		
		system.terminate();
	}

	private static void sleep(long ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
