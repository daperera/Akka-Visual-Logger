package com.AkkaVisualDebugger.actorEnumerationTest;

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
				.build();
	}
	
}
