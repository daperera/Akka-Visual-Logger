package com.akkaVisualizor.visualModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;
import com.akkaVisualizor.akkaModel.Channel;

public class GlobalModel {
	private final Context context;
//	private final List<VisualActor> visualActorList;
	private final List<VisualChannel> channelList;
	private final Map<VisualActor, List<VisualActor>> existingChannel;
	private final List<VisualActor> selectedActorList;
	
	public GlobalModel(Context context) {
		this.context = context;
//		visualActorList = new ArrayList<VisualActor>();
		channelList = new ArrayList<VisualChannel>();
		existingChannel = new HashMap<VisualActor, List<VisualActor>>();
		selectedActorList = new ArrayList<VisualActor>();
	
	}
	
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

	public void createBidirectionalChannelTo(VisualActor target) {
		for(VisualActor source : selectedActorList) {
			if(!channelAlreadyExist(source, target)) {
				System.out.println("creating channel");
				
				// add to akka model
				Channel channel = context.getAkkaModel().createChannel(source.getActor(), target.getActor());
				
				// add internally
				VisualChannel visualChannel = new VisualChannel(context, channel, source, target);
				createBidirectionalChannel(visualChannel, source, target);
				
				// add channel to view
				context.getVisualizor().createBidirectionalChannel(visualChannel);
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
		boolean exist = false;
		if(existingChannel.get(source) != null) {
			exist |= existingChannel.get(source).contains(target);
		}
		if(existingChannel.get(target) != null) {
			exist |= existingChannel.get(target).contains(source);
		}
		return exist;
	}

	private void createBidirectionalChannel(VisualChannel channel, VisualActor source, VisualActor target) {
		// add visualChannel to visualChannelList
		channelList.add(channel);
		
		// update existingChannel variable
		List<VisualActor> targetList = existingChannel.get(source);
		if(targetList == null) { // handle case when no channel yet added to source
			targetList = new ArrayList<VisualActor>();
		}
		targetList.add(target);
		existingChannel.put(source, targetList);
	}

	public void createActor(String name, double x, double y) throws Exception {
		Actor actor = context.getAkkaModel().createActor(name);
		// if no exception thrown
		VisualActor visualActor = new VisualActor(context, actor, name, x, y);
		context.getVisualizor().createActor(visualActor, name, x, y);
	}
	
}
