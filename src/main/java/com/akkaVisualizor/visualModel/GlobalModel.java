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
import com.akkaVisualizor.visualModel.visual.VisualActor;
import com.akkaVisualizor.visualModel.visual.VisualActorList;
import com.akkaVisualizor.visualModel.visual.VisualActorType;
import com.akkaVisualizor.visualModel.visual.VisualActorTypeList;
import com.akkaVisualizor.visualModel.visual.VisualChannel;
import com.akkaVisualizor.visualModel.visual.VisualMessage;
import com.akkaVisualizor.visualModel.visual.VisualMessageType;
import com.akkaVisualizor.visualModel.visual.VisualMessageTypeList;

public class GlobalModel {
	private final Context context;
//	private final Map<Actor, VisualActor> actorToVisualActor;
	private final Map<Channel, VisualChannel> channelToVisualChannel;
	private final VisualActorList visualActorList;
	private final List<VisualChannel> channelList;
	private final List<VisualActor> selectedActorList;
	private final List<VisualChannel> selectedChannelList;
	private final VisualMessageTypeList visualMessageTypeList;
	private final VisualActorTypeList visualActorTypeList;
	private VisualMessageType selectedVisualMessageType;

	public GlobalModel(Context context) {
		this.context = context;
//		actorToVisualActor = new HashMap<>();
		channelToVisualChannel = new HashMap<>();
		visualActorList = new VisualActorList();
		channelList = new ArrayList<>();
		selectedActorList = new ArrayList<>();
		selectedChannelList = new ArrayList<>();
		visualActorTypeList = new VisualActorTypeList();
		visualMessageTypeList = new VisualMessageTypeList();
	}

	/* ********************************************** *
	 * *      NODE SELECTION GESTION FUNCTIONS      * *
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
		if(selectedChannelList.contains(channel)) {
			deselectChannelUtil(channel);
		} else {
			selectChannelUtil(channel);
		}
	}

	public void selectMessageType(VisualMessageType messageType) {
		selectedVisualMessageType = messageType;
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

	/**
	 * create an actor from an actor type, its name and its coordinate on screen 
	 * @param name : if null, then  
	 * @throws Exception if akka system can't create corresponding actor (duplicate name)
	 */

	private void createActor(VisualActorType t, String name, double x, double y) throws Exception {
		Actor actor = context.getAkkaModel().createActor(t.getActorType(), name);
		
		// if no exception thrown
		VisualActor visualActor = new VisualActor(context, actor, actor.getName(), x, y);
//		actorToVisualActor.put(actor, visualActor);
		visualActorList.add(visualActor);
		context.getApp().createActor(visualActor, actor.getName(), x, y);
	}

	public void notifyActorCreated(Actor actor) {
		// get Name
		String name = actor.getName();

		// init coordinate
		double x = ThreadLocalRandom.current().nextDouble(0, context.getApp().getScene().getWidth());
		double y = ThreadLocalRandom.current().nextDouble(0, context.getApp().getScene().getHeight());

		VisualActor visualActor = new VisualActor(context, actor, name, x, y);
//		actorToVisualActor.put(actor, visualActor);
		visualActorList.add(visualActor);
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
//		VisualActor visualActor = actorToVisualActor.get(actor);
		VisualActor visualActor = visualActorList.get(actor);
		
		// delete internally (automatically impacts on view)
		actorInternalDeletion(visualActor);
	}

	public void createMessageTo(VisualActor target) {
		try {
			if(selectedVisualMessageType == null) {
				throw new Exception("Error : no message selected");
			}
			if(target == null) {
				throw new Exception("Error : no target selected");
			}
			for(VisualActor source : selectedActorList) {
				// add to akka model

				Message message = context.getAkkaModel()
						.sendMessage(selectedVisualMessageType.getMessageType(), 
								source.getActor(), 
								target.getActor());

				// add internally
				VisualMessage visualMessage = new VisualMessage(message, source, target);

				// add to view
				context.getApp().createMessage(visualMessage);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	public void notifyMessageCreated(Message message) {
		// fetch source and target of message
//		VisualActor source = actorToVisualActor.get(message.getSource());
//		VisualActor target = actorToVisualActor.get(message.getTarget());
		VisualActor source = visualActorList.get(message.getSource());
		VisualActor target = visualActorList.get(message.getTarget());
		if(message.getSource()==null) {
			System.out.println("source is null");
		}
		if(message.getTarget()==null) {
			System.out.println("target is null");
		}
		
		
		// add internally
		VisualMessage visualMessage = new VisualMessage(message, source, target);

		// add to view
		context.getApp().createMessage(visualMessage);
	}

	public void createChannelTo(VisualActor target) {
		for(VisualActor source : selectedActorList) {
			if(!channelAlreadyExist(source, target)) {
				// add to akka model
				Channel channel = context.getAkkaModel().createChannel(source.getActor(), target.getActor());

				// add internally
				VisualChannel visualChannel = new VisualChannel(context, channel, source, target);
				channelList.add(visualChannel);

				// add channel to view
				context.getApp().createChannel(visualChannel);
			}
		}
	}
	
	public void notifyChannelCreated(Channel channel) {
		// add internally
//		VisualActor source = actorToVisualActor.get(channel.getSource());
//		VisualActor target = actorToVisualActor.get(channel.getTarget());
		VisualActor source = visualActorList.get(channel.getSource());
		VisualActor target = visualActorList.get(channel.getTarget());
		
		VisualChannel visualChannel = new VisualChannel(context, channel, source, target);
		channelList.add(visualChannel);
		channelToVisualChannel.put(channel, visualChannel);

		// add channel to view
		context.getApp().createChannel(visualChannel);
	}
	
	public void notifyChannelDeleted(Channel channel) {
		// delete internally 
		VisualChannel visualChannel = channelToVisualChannel.get(channel);
		channelInternalDeletion(visualChannel); // automatically impact on view
		
	}

	public void notifyActorTypeCreated(ActorType actorType) {
		visualActorTypeList.add(new VisualActorType(actorType));
	}

	public void notifyMessageTypeCreated(MessageType messageType) {
		visualMessageTypeList.add(new VisualMessageType(messageType));
	}

	public VisualActorTypeList getVisualActorTypeList() {
		return visualActorTypeList;
	}

	public VisualMessageTypeList getVisualMessageTypeList() {
		return visualMessageTypeList;
	}

	public VisualActorList getVisualActorList() {
		return visualActorList;
	}

	// ask for the actor name and create it
	public void requestCreateActorFromType(VisualActorType t, double x, double y) {
		String name;
		boolean correctName = false, restarted = false;
		while(!correctName) {
			// get name from user
			name = context.getApp().askActorName(restarted);

			// try to create actor with this name
			try {
				createActor(t, name, x, y);
				correctName = true;
			} catch(Exception exception) {
				exception.printStackTrace();
				restarted = true;
			}
		}
	}

	/* ********************************* *
	 * *              UTILS            * *
	 * ********************************* */ 


	private void selectActorUtil(VisualActor actor) {
		selectedActorList.add(actor);
		actor.select();
	}

	private void deselectActorUtil(VisualActor actor) {
		selectedActorList.remove(actor);
		actor.deselect();
	}

	private void deselectAllActorUtil() {
		// using iterator to avoid concurrent modification
		ListIterator<VisualActor> iter = selectedActorList.listIterator();
		while(iter.hasNext()){
			VisualActor actor = iter.next();
			actor.deselect();
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
//		actor.delete();
//		actorToVisualActor.remove(actor.getActor());
		visualActorList.remove(actor.getActor());
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

//		actorToVisualActor.remove(actor.getActor());
		visualActorList.remove(actor.getActor());
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

	private void channelInternalDeletion(VisualChannel channel) {
		channel.delete(); // impact on view
		channelList.remove(channel);
		selectedChannelList.remove(channel);
		channelToVisualChannel.remove(channel.getChannel());
	}

	private void channelInternalDeletionFromChannelList(VisualChannel channel, ListIterator<VisualChannel> it) {
		channel.delete(); // impact on view
		selectedChannelList.remove(channel);
		channelToVisualChannel.remove(channel.getChannel());
		it.remove();
	}
	private void channelInternalDeletionFromSelectedChannelList(VisualChannel channel, ListIterator<VisualChannel> it) {
		channel.delete(); // impact on view
		channelList.remove(channel);
		channelToVisualChannel.remove(channel.getChannel());
		it.remove();
	}


}
