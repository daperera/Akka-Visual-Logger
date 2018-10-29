package com.akkaVisualizor.akkaModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.ActorType.SerializableSupplier;
import com.akkaVisualizor.akkaModel.event.EventLogger;

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
	private EventLogger eventLogger;

	public AkkaModel(Context context, ActorSystem system) {
		this.context = context;
		this.system = system;
		actorRefToActor = new HashMap<ActorRef, Actor>();
		messageToMessage = new HashMap<Object, Message>();
		actorTypeList = new ArrayList<ActorType>();
		messageTypeList = new ArrayList<MessageType>();
		eventLogger = new EventLogger(context);
	}

	/**
	 * Use this method to create an actor from the model.
	 * Creation is logged, transmitted to model and to akka system.
	 */
	public void createActor(ActorType actorType, String name) throws Exception {
		// create actor in akka
		ActorRef actorRef = getInstance(actorType, name);
		
		
/*
		// create actor internally
		// remark : use akka decided name, and not name used when invoking this function (in case they are different)
		Actor actor = new Actor(actorRef, getName(actorRef), actorType);
		actorRefToActor.put(actorRef, actor);
		eventLogger.logActorCreated(actor);
		
		// return to model
		return actor;
	*/
	}

	/**
	 * Use this method to create an actor from akka system.
	 * No modification is propagated to akka system.
	 */
	public void logActorCreated(akka.actor.Actor AkkaActor, String name) {
		// unfold out actor type
		ActorType actorType = null;
		for(ActorType type : actorTypeList) {
			if(AkkaActor.getClass().equals(type.getClass())) {
				actorType = type;
			}
		}

		// create actor internally
		Actor actor = new Actor(AkkaActor, name, actorType);
		actorRefToActor.put(AkkaActor.self(), actor);
		eventLogger.logActorCreated(actor);


		// notify model
		context.getModel().notifyActorCreated(actor);
	}	

	/**
	 * Use this method to create an actor from the event logger.
	 * This event is not logged and no modification is propagated to akka system.
	 */
	public void createActor(Actor actor, String name) {
		// create actor internally
		actorRefToActor.put(actor.getActorRef(), actor);

		// notify model
		context.getModel().notifyActorCreated(actor);
		
		// do not propagate modification to akka and do not log this event 
	}

	/**
	 * Use this method to create channel from model.
	 * Event is logged and no propagation do model.
	 */
	public Channel createChannel(Actor source, Actor target) {
		// create channel internally
		Channel channel = new Channel(source, target);
		eventLogger.logChannelCreated(channel);

		return channel;
	}
	
	/**
	 * Use this method to create channel from event logger.
	 * Event is not logged, and there is propagation to model
	 */
	public void createChannel(Channel channel) {
		// notify model
		context.getModel().notifyChannelCreated(channel);
	}

	/**
	 * Use this method to delete channel from model.
	 * Event is logged and no propagation do model.
	 */
	public void deleteChannel(Channel channel) {
		// register deletion internally
		eventLogger.logChannelDeleted(channel);
	}
	
	/**
	 * Use this method to delete channel from event logger.
	 * Event is not logged, and there is propagation to model
	 */
	public void deleteChannelFromEventLogger(Channel channel) {
		// notify model
		context.getModel().notifyChannelDeleted(channel);
	}

	/**
	 * Use this method to delete actor from model.
	 * no propagation back to model.
	 */
	public void deleteActor(Actor actor) {
		// delete actor in akka
		system.stop(actor.getActorRef());

		// delete actor internally
		actorRefToActor.remove(actor.getActorRef());
		eventLogger.logActorDeleted(actor);
	}

	/**
	 * Use this method to delete actor from akka.
	 * No propagation to akka system.
	 */
	public void logActorDeleted(ActorRef actorRef) {
		// get corresponding actor
		Actor actor = actorRefToActor.get(actorRef);

		// delete in model
		context.getModel().notifyActorDeleted(actor);

		// delete actor internally
		actorRefToActor.remove(actorRef);
		eventLogger.logActorDeleted(actor);
	}

	/**
	 * Use this method to send message from model.
	 */
	public Message sendMessage(MessageType messageType, Actor source, Actor target) {
		// construct message internally
		Message message = internalMessageCreation(messageType.getInstance(), source, target);
		eventLogger.logMessageSent(message);

		// send message in akka
		target.getActorRef().tell(message.getMessage(), source.getActorRef());
		
		// notify model
		return message;
	}
	
	/**
	 * Use this method to send message from akka system.
	 * No modification in akka system.
	 */
	public void logMessageSent(Object m, ActorRef source, ActorRef target) {
		// construct message internally
		Message message = internalMessageCreation(m, actorRefToActor.get(source), actorRefToActor.get(target));
		eventLogger.logMessageSent(message);

		// notify model
		context.getModel().notifyMessageCreated(message);
	}
	
	/**
	 * Use this method to send message from event logger.
	 * This event is not logged and no modification is propagated to akka system.
	 */
	public void sendMessage(Message message, Actor source, Actor target) {
		// construct message internally
		internalMessageCreation(message, source, target);
		
		// notify model
		context.getModel().notifyMessageCreated(message);
		
		// do not propagate modification to akka and do not log this event
	}

	/**
	 * Use this method to send message from akka system.
	 * No modification in akka system.
	 */
	public void logMessageReceived(Object m, ActorRef source, ActorRef target) {
		// get corresponding message
		Message message = messageToMessage.get(m);

		if(message==null) {
			new Exception("Error : an orphan message has been received").printStackTrace();;
		} else {
			message.setDelivered();
			eventLogger.logMessageReceived(message);
		}

	}

	/**
	 * Use this method to send message from akka system.
	 * No modification in akka system.
	 */
	public void logActorType(String typeClass, SerializableSupplier<Props> typeConstructor) {
		// add actor internally
		ActorType type = new ActorType(typeClass, typeConstructor, system);
		actorTypeList.add(type);

		// notify model ?
		context.getModel().notifyActorTypeCreated(type);		
	}

	/**
	 * Use this method to send message from akka system.
	 * No modification in akka system.
	 */
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

	/*
	private String getName(ActorRef actor) {
		return actor.path().name();
	}
	*/

}
