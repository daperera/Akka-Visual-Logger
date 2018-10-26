package com.akkaVisualizor.akkaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.ActorType.SerializableSupplier;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.InvalidActorNameException;
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

	public Actor createActor(ActorType actorType, String name) throws Exception {
		// create actor in akka
		ActorRef actorRef = getInstance(actorType, name);

		// remak : use akka decided name, and not name used when invoking this function (in case they are different)
		Actor actor = new Actor(actorRef, getName(actorRef), actorType);
		actorRefToActor.put(actorRef, actor);
		return actor;
	}

	public void logActorCreated(ActorRef actorRef, String name) {
		// work out actor type
		ActorType actorType = null;
		for(ActorType type : actorTypeList) {
			if(actorRef.getClass().equals(type.getClass())) {
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
		Message message = internalMessageCreation(messageType.getInstance(), source, target);
		
		// send message in akka
		target.getActorRef().tell(message.getMessage(), source.getActorRef());
		return message;
	}

	public void logMessageSent(Object m, ActorRef source, ActorRef target) {
		// construct message internally
		Message message = internalMessageCreation(m, actorRefToActor.get(source), actorRefToActor.get(target));
		
		// notify model
		context.getModel().notifyMessageCreated(message);
	}

	public void logMessageReceived(Object m, ActorRef source, ActorRef target) {
		try {
			// get corresponding message
			Message message = messageToMessage.get(m);
			
			if(message==null) {
				throw new Exception("Error : an orphan message has been received");
			}
			
			message.setDelivered();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void logActorType(String typeClass, SerializableSupplier<Props> typeConstructor) {
		// add actor internally
		ActorType type = new ActorType(typeClass, typeConstructor, system);
		actorTypeList.add(type);

		// notify model ?
		context.getModel().notifyActorTypeCreated(type);		
	}

	public void logMessageType(String name, Supplier<Object> messageConstructor) {
		// add actoryp internally
		MessageType type = new MessageType(name, messageConstructor); 
		messageTypeList.add(type);

		// notify model ?
		context.getModel().notifyMessageTypeCreated(type);
	}

	private Message internalMessageCreation(Object m, Actor source, Actor target) {
		Message message = new Message(source, target, m); // construct message
		messageToMessage.put(m, message);
		return message;
	}
	
	private ActorRef getInstance(ActorType type, String name) throws Exception {
		if(name == null) { // if name is null use default akka name
			return system.actorOf(type.getTypeConstructor().get());
		}
		else { // else try to use name defined by user
			try {
				return system.actorOf(type.getTypeConstructor().get(), name);
			} catch(InvalidActorNameException e) {
				throw new InvalidActorNameException("name already used ["+ name + "]");
			}
		}
	}

	private String getName(ActorRef actor) {
		return actor.path().name();
	}

}
