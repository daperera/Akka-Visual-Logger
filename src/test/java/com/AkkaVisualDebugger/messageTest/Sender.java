package com.AkkaVisualDebugger.messageTest;

import com.akkaVisualizor.AkkaVisualLogger;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

public class Sender extends AbstractActor {

	private final AkkaVisualLogger log = AkkaVisualLogger.getInstance();
	private int privateField = 0;
	protected int protectedField = 0;
	public int publicField = 0;
	
	public static Props props() {
		return Props.create(Sender.class, () -> new Sender());
	}
	
	public Sender() {
		log.logActorCreated(this);
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
		
		privateField++;
		protectedField++;
		publicField++;
		
		log.logInternalEvent(getSelf());
	}
	
	private void matchAny(Object m) {
		log.logMessageReceived(m, getSender(), getSelf());
		System.out.println("unknown message received : " + m);
	}
	
	public int getPrivateField() {
		return privateField;
	}
	
}
