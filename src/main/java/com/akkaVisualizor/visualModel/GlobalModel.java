package com.akkaVisualizor.visualModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.ActorType;
import com.akkaVisualizor.akkaModel.Channel;
import com.akkaVisualizor.akkaModel.Message;
import com.akkaVisualizor.akkaModel.MessageType;

public class GlobalModel {
	private final Context context;
	private final Map<Actor, VisualActor> actorToVisualActor;
	private final List<VisualChannel> channelList;
	private final List<VisualActor> selectedActorList;
	private final List<VisualChannel> selectedChannelList;

	public GlobalModel(Context context) {
		this.context = context;
		actorToVisualActor = new HashMap<Actor, VisualActor>();
		channelList = new ArrayList<VisualChannel>();
		selectedActorList = new ArrayList<VisualActor>();
		selectedChannelList = new ArrayList<VisualChannel>();
	}

	/* ********************************************** *
	 * *      ACTOR SELECTION GESTION FUNCTIONS     * *
	 * ********************************************** */

	public void selectActor(VisualActor actor) {
		deselectAllActorUtil();
		selectActorUtil(actor);
	}

	public void addToSelectedActorList(VisualActor actor) {
		if(selectedActorList.contains(actor)) {
			deselectActorUtil(actor);
		} else {
			selectActorUtil(actor);
		}
	}

	public void selectChannelView(VisualChannel channel) {
		deselectAllChannelUtil();
		selectChannelUtil(channel);
	}

	public void addToSelectedChannelList(VisualChannel channel) {
		if(selectedActorList.contains(channel)) {
			deselectChannelUtil(channel);
		} else {
			selectChannelUtil(channel);
		}
	}

	public void simulationPaneClicked() {
		deselectAllActorUtil();
		deselectAllChannelUtil();
	}

	public void nodeDeletion() {

		// delete selected actors
		ListIterator<VisualActor> iter1 = selectedActorList.listIterator();
		while(iter1.hasNext()){
			VisualActor actor = iter1.next();
			actorInternalDeletion(actor, iter1);
		};
		
		//delete selected channels
		ListIterator<VisualChannel> iter2 = selectedChannelList.listIterator();
		while(iter2.hasNext()){
			VisualChannel channel = iter2.next();
			channelInternalDeletionFromSelectedChannelList(channel, iter2);
		};
	}


	/* ********************************* *
	 * *      CREATION FUNCTIONS       * *
	 * ********************************* */ 

	public void createActor(String name, double x, double y) throws Exception {
		Actor actor = context.getAkkaModel().createActor(name);
		// if no exception thrown
		VisualActor visualActor = new VisualActor(context, actor, name, x, y);
		context.getApp().createActor(visualActor, name, x, y);
	}

	public void notifyActorCreated(Actor actor) {
		// get Name
		String name = actor.getName();

		// init coordinate
		double x = ThreadLocalRandom.current().nextDouble(0, context.getApp().getScene().getWidth());
		double y = ThreadLocalRandom.current().nextDouble(0, context.getApp().getScene().getHeight());

		VisualActor visualActor = new VisualActor(context, actor, name, x, y);
		try {
			context.getApp().createActor(visualActor, name, x, y);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void deleteActor(VisualActor visualActor) {
		// delete in akka model
		context.getAkkaModel().deleteActor(visualActor.getActor());

		// delete internally (automatically impacts on view)
		actorInternalDeletion(visualActor);
	}

	public void notifyActorDeleted(Actor actor) {
		// get corresponding visualActor
		VisualActor visualActor = actorToVisualActor.get(actor);

		// delete internally (automatically impacts on view)
		actorInternalDeletion(visualActor);
	}

	public void createMessageTo(VisualActor target) {
		for(VisualActor source : selectedActorList) {
			// add to akka model
			Message message = context.getAkkaModel().sendMessage(null, source.getActor(), target.getActor());

			// add internally
			VisualMessage visualMessage = new VisualMessage(message, source, target);

			// add to view
			context.getApp().createMessage(visualMessage);
		}
	}

	public void notifyMessageCreated(Message message) {
		// fetch source and target of message
		VisualActor source = actorToVisualActor.get(message.getSource());
		VisualActor target = actorToVisualActor.get(message.getTarget());

		// add internally
		VisualMessage visualMessage = new VisualMessage(message, source, target);

		// add to view
		context.getApp().createMessage(visualMessage);
	}

	public void createBidirectionalChannelTo(VisualActor target) {
		for(VisualActor source : selectedActorList) {
			if(!channelAlreadyExist(source, target)) {
				// add to akka model
				Channel channel = context.getAkkaModel().createChannel(source.getActor(), target.getActor());

				// add internally
				VisualChannel visualChannel = new VisualChannel(context, channel, source, target);
				channelList.add(visualChannel);

				// add channel to view
				context.getApp().createBidirectionalChannel(visualChannel);
			}
		}
	}

	/* ********************************* *
	 * *              UTILS            * *
	 * ********************************* */ 


	private void selectActorUtil(VisualActor actor) {
		selectedActorList.add(actor);
		actor.setSelected();
	}

	private void deselectActorUtil(VisualActor actor) {
		selectedActorList.remove(actor);
		actor.setDeselected();
	}

	private void deselectAllActorUtil() {
		// using iterator to avoid concurrent modification
		ListIterator<VisualActor> iter = selectedActorList.listIterator();
		while(iter.hasNext()){
			VisualActor actor = iter.next();
			actor.setDeselected();
			iter.remove();
		};
	}

	private void selectChannelUtil(VisualChannel channel) {
		selectedChannelList.add(channel);
		channel.setSelected();
	}

	private void deselectChannelUtil(VisualChannel channel) {
		selectedChannelList.remove(channel);
		channel.setDeselected();
	}

	private void deselectAllChannelUtil() {
		// using iterator to avoid concurrent modification
		ListIterator<VisualChannel> iter = selectedChannelList.listIterator();
		while(iter.hasNext()){
			VisualChannel channel = iter.next();
			channel.setDeselected();
			iter.remove();
		};
	}

	private boolean channelAlreadyExist(VisualActor source, VisualActor target) {
		for(VisualChannel channel : channelList) {
			if((channel.getSource().equals(source) && channel.getTarget().equals(target))
					|| (channel.getTarget().equals(source) && channel.getSource().equals(target))) {
				return true;
			}
		}
		return false;
	}

	private void actorInternalDeletion(VisualActor actor) {
		actor.delete();
		
		actorToVisualActor.remove(actor);
		selectedActorList.remove(actor);
		
		// delete channels connected to this actors
		ListIterator<VisualChannel> iter = channelList.listIterator();
		while(iter.hasNext()){
			VisualChannel channel = iter.next();
			if(channel.getSource().equals(actor) || channel.getTarget().equals(actor)) {
				channelInternalDeletionFromChannelList(channel, iter);
			}
		};
	}
	
	private void actorInternalDeletion(VisualActor actor, ListIterator<VisualActor> it) {
		actor.delete();	
		
		actorToVisualActor.remove(actor);
		it.remove();
		
		// delete channels connected to this actors
		ListIterator<VisualChannel> iter = channelList.listIterator();
		while(iter.hasNext()){
			VisualChannel channel = iter.next();
			if(channel.getSource().equals(actor) || channel.getTarget().equals(actor)) {
				channelInternalDeletionFromChannelList(channel, iter);
			}
		};
	}

	/*
	private void channelInternalDeletion(VisualChannel channel) {
		channel.delete();
		channelList.remove(channel);
		selectedChannelList.remove(channel);
	}
	*/
	
	private void channelInternalDeletionFromChannelList(VisualChannel channel, ListIterator<VisualChannel> it) {
		channel.delete();
		it.remove();
		selectedChannelList.remove(channel);
	}
	private void channelInternalDeletionFromSelectedChannelList(VisualChannel channel, ListIterator<VisualChannel> it) {
		channel.delete();
		channelList.remove(channel);
		it.remove();
	}

	public void notifyActorTypeCreated(ActorType type) {
		
	}

	public void notifyMessageTypeCreated(MessageType type) {
		
	}
}
