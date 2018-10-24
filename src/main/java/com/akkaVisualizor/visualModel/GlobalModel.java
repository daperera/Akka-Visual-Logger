package com.akkaVisualizor.visualModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.Channel;
import com.akkaVisualizor.akkaModel.Message;

public class GlobalModel {
	private final Context context;
	private final Map<Actor, VisualActor> actorToVisualActor;
	private final List<VisualChannel> channelList;
	private final List<VisualActor> selectedActorList;

	public GlobalModel(Context context) {
		this.context = context;
		actorToVisualActor = new HashMap<Actor, VisualActor>();
		channelList = new ArrayList<VisualChannel>();
		selectedActorList = new ArrayList<VisualActor>();

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
	
	public void simulationPaneClicked() {
		deselectAllActorUtil();
	}
	
	public void nodeDeletion() {
		try {
			throw new Exception("NOT YET IMPLEMENTED");
		} catch(Exception e) {
			e.printStackTrace();
		}
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

	public void createMessageTo(VisualActor target) {
		for(VisualActor source : selectedActorList) {
			// add to akka model
			Message message = context.getAkkaModel().createMessage(source.getActor(), target.getActor());

			// add internally
			VisualMessage visualMessage = new VisualMessage(message, source, target);

			// add to view
			context.getApp().createMessage(visualMessage);
		}
	}
	
	public void registerMessageCreated(Message message) {
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

	public void createUnidirectionalChannelTo(VisualActor target) {
		try {
			throw new Exception("NOT YET IMPLEMENTED");
		} catch(Exception e) {
			e.printStackTrace();
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

	private boolean channelAlreadyExist(VisualActor source, VisualActor target) {
		for(VisualChannel channel : channelList) {
			if((channel.getSource().equals(source) && channel.getTarget().equals(target))
					|| (channel.getTarget().equals(source) && channel.getSource().equals(target))) {
				return true;
			}
		}
		return false;
	}

	

	

	

	

}
