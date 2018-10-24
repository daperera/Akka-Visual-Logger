package com.akkaVisualizor.akkaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.akkaVisualizor.Context;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class AkkaModel {

	private Context context;
	private ActorSystem system;

	private Map<ActorRef, Actor> actorRefToActor;
	private Map<Object, Message> messageToMessage;
	private List<ActorType> actorTypeList;
	private List<MessageType> messageTypeList;

	public AkkaModel(Context context, ActorSystem system) {
		this.context = context;
		this.system = system;
		actorRefToActor = new HashMap<ActorRef, Actor>();
		messageToMessage = new HashMap<Object, Message>();
		actorTypeList = new ArrayList<ActorType>();
		messageTypeList = new ArrayList<MessageType>();
	}

	public Actor createActor(String name) throws Exception {
		if(name.equals(""))
			throw new RuntimeException("InvalidName");

		return null;
	}

	public Actor createActor(ActorType actorType, String name) {
		// create actor internally
		ActorRef actorRef = actorType.getInstance(name);
		Actor actor = new Actor(actorRef, name, actorType);
		actorRefToActor.put(actorRef, actor);

		return actor;
	}

	public void logActorCreated(ActorRef actorRef, String name) {
		// work out actor type
		ActorType actorType = null;
		for(ActorType type : actorTypeList) {
			if(actorRef.getClass().equals(type.getTypeClass())) {
				actorType = type;
			}
		}

		// create actor internally
		Actor actor = new Actor(actorRef, name, actorType);
		actorRefToActor.put(actorRef, actor);

		// notify model
		context.getModel().notifyActorCreated(actor);
	}	

	public Channel createChannel(Actor source, Actor target) {
		return new Channel(source, target);
	}

	public void deleteActor(Actor actor) {
		// delete actor in akka
		system.stop(actor.getActorRef());

		// delete in model
		context.getModel().notifyActorDeleted(actor);

		// delete actor internally
		actorRefToActor.remove(actor.getActorRef());
	}

	public void logActorDeleted(ActorRef actorRef) {
		// get corresponding actor
		Actor actor = actorRefToActor.get(actorRef);

		// delete in model
		context.getModel().notifyActorDeleted(actor);

		// delete actor internally
		actorRefToActor.remove(actor);
	}

	public Message sendMessage(MessageType messageType, Actor source, Actor target) {
		// construct message internally
		Message message = new Message(source, target, messageType.getInstance()); // construct message
		// end message in akka
		target.getActorRef().tell(message, source.getActorRef());
		return message;
	}

	public void logMessageSent(Object m, ActorRef source, ActorRef target) {
		// construct message internally
		Message message = new Message(actorRefToActor.get(source), actorRefToActor.get(target), m); // construct message

		// notify model
		context.getModel().notifyMessageCreated(message);
	}

	public void logMessageReceived(Object m, ActorRef source, ActorRef target) {
		// get corresponding message
		Message message = messageToMessage.get(m);

		message.setDelivered();
	}

	public void logActorType(Class<? extends ActorRef> typeClass, Supplier<Props> typeConstructor) {
		actorTypeList.add(new ActorType(typeClass, typeConstructor, system));
	}

	public void logMessageType(String name, Supplier<Object> messageConstructor) {
		messageTypeList.add(new MessageType(name, messageConstructor));
	}
}
