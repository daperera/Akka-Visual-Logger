package com.AkkaVisualDebugger.akkaActorParentTest;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;


public class DefaultActor extends AbstractActor {

	public static Props props() {
		return Props.create(DefaultActor.class, () -> new DefaultActor());
	}
	
	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create()
				.match(CreateChild.class, this::createChild)
				.match(PrintParent.class, this::printParent)
				.build();
	}
	
	private void createChild(CreateChild m) {
		System.out.println("creating child");
		getContext().actorOf(DefaultActor.props(), "createdByDefaultActor");
	}
	
	private void printParent(PrintParent m) {
		System.out.println("actor ["+ getSelf().path() + "] created by : [" + getContext().getParent().path() + "]");
		
	}
	
	
}