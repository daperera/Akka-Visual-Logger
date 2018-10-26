package com.AkkaVisualDebugger;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class PrintAllActor extends AbstractActor {

	public static Props props() {
		return Props.create(PrintAllActor.class, () -> new PrintAllActor());
	}
	
	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create()
				.matchAny(m -> System.out.println(m))
				.build();
	}
	
}