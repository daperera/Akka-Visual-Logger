package com.AkkaVisualDebugger.messageTest;

import com.akkaVisualizor.AkkaVisualDebugger;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class Sender extends AbstractActor {

	private AkkaVisualDebugger log = AkkaVisualDebugger.getInstance();
	
	public static Props props() {
		return Props.create(Sender.class, () -> new Sender());
	}
	
	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create()
				.match(Reply.class, this::reply)
				.matchAny(this::matchAny)
				.build();
	}
	
	private void reply(Reply m) {
		log.logMessageReceived(m, getSender(), getSelf());
		System.out.println("reply received");
	}
	
	private void matchAny(Object m) {
		log.logMessageReceived(m, getSender(), getSelf());
		System.out.println("unknown message received : " + m);
	}
	
}
