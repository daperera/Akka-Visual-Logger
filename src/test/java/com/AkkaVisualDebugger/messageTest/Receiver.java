package com.AkkaVisualDebugger.messageTest;

import com.akkaVisualizor.AkkaVisualDebugger;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class Receiver extends AbstractActor {

	private AkkaVisualDebugger log = AkkaVisualDebugger.getInstance();
	
	public static Props props() {
		return Props.create(Receiver.class, () -> new Receiver());
	}
	
	@Override
	public Receive createReceive() {
		return ReceiveBuilder.create()
				.match(Query.class, this::query)
				.matchAny(this::matchAny)
				.build();
	}

	private void query(Query m) {
		log.logMessageReceived(m, getSender(), getSelf());
		Reply r = new Reply(m.getContent());
		log.logMessageSent(r, getSelf(), getSender());
		getSender().tell(r, getSelf());
	}
	
	private void matchAny(Object m) {
		log.logMessageReceived(m, getSender(), getSelf());
		System.out.println("unknown message received : " + m);
	}
	
}