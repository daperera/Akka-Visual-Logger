package com.akkaVisualizor.visualModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import com.akkaVisualizor.akkaModel.ActorType;
import com.akkaVisualizor.akkaModel.Message;
import com.akkaVisualizor.akkaModel.MessageType;
import com.akkaVisualizor.utils.Context;
import com.akkaVisualizor.visualModel.visual.VisualActor;
import com.akkaVisualizor.visualModel.visual.VisualActorList;
import com.akkaVisualizor.visualModel.visual.VisualActorType;
import com.akkaVisualizor.visualModel.visual.VisualActorTypeList;
import com.akkaVisualizor.visualModel.visual.VisualChannel;
import com.akkaVisualizor.visualModel.visual.VisualChannelList;
import com.akkaVisualizor.visualModel.visual.VisualMessage;
import com.akkaVisualizor.visualModel.visual.VisualMessageType;
import com.akkaVisualizor.visualModel.visual.VisualMessageTypeList;

import javafx.util.Pair;

public class GlobalModel {
	private final Context context;
	//	private final Map<Actor, VisualActor> actorToVisualActor;
	private final VisualActorList visualActorList;
	private final VisualChannelList channelList;
	private final List<VisualActor> selectedActorList;
	private final List<VisualChannel> selectedChannelList;
	private final VisualMessageTypeList visualMessageTypeList;
	private final VisualActorTypeList visualActorTypeList;
	private VisualMessageType selectedVisualMessageType;
	private final Map<String, Object> temp;
	private boolean replayMode; // replay mode entered flag

	public GlobalModel(Context context) {
		this.context = context;
		//		actorToVisualActor = new HashMap<>();
		visualActorList = new VisualActorList(context);
		channelList = new VisualChannelList(context);
		selectedActorList = new ArrayList<>();
		selectedChannelList = new ArrayList<>();
		visualActorTypeList = new VisualActorTypeList();
		visualMessageTypeList = new VisualMessageTypeList();
		temp = new HashMap<>(); 
		replayMode = false;
	}

	/* ********************************************** *
	 * *      NODE SELECTION GESTION FUNCTIONS      * *
	 * ********************************************** */

	public void selectActor(VisualActor actor) {
		deselectAllActorUtil();
		selectActorUtil(actor);
	}

	public void deselectActor(VisualActor actor) {
		deselectActorUtil(actor);
	}

	public void addToSelectedActorList(VisualActor actor) {
		if(selectedActorList.contains(actor)) {
			deselectActorUtil(actor);
		} else {
			selectActorUtil(actor);
		}
	}

	public void selectChannel(VisualChannel channel) {
		deselectAllChannelUtil();
		selectChannelUtil(channel);
	}

	public void deselectChannel(VisualChannel actor) {
		deselectChannelUtil(actor);
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
		if(!replayMode) {
			// delete selected actors
			List<VisualChannel> channelCpy = copy(selectedChannelList);
			for(VisualChannel c : channelCpy) {
				context.getAkkaModel().deleteChannel(c.getChannel());
			}

			// delete selected channels
			List<VisualActor> actorCpy = copy(selectedActorList);
			for(VisualActor a : actorCpy) {
				context.getAkkaModel().deleteActor(a.getActor());
			}
		}
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
		if(!replayMode) {
			// store coordinate for later retrieval
			temp.put(name, new Pair<Double, Double>(x, y));

			// create actor in akka
			context.getAkkaModel().createActor(t.getActorType(), name);
		}
	}

	public void createMessageTo(VisualActor target) {
		if(!replayMode) {
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
	}

	public void notifyMessageCreated(Message message) {
		// fetch source and target of message
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
				context.getAkkaModel().createChannel(source.getActor(), target.getActor());

			}
		}
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

	public VisualChannelList getVisualChannelList() {
		return channelList;
	}

	public VisualActorList getVisualActorList() {
		return visualActorList;
	}

	// ask for the actor name and create it
	public void requestCreateActorFromType(VisualActorType t, double x, double y) {
		if(!replayMode) {
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
	}


	public void resetHistory() {
		if(!replayMode) {
			System.out.println("replay mode entered");
			replayMode = true;
		}
		context.getAkkaModel().resetHistory();
	}
	
	public void undoHistory() {
		if(!replayMode) {
			System.out.println("replay mode entered");
			replayMode = true;
		}
		context.getAkkaModel().undoHistory();
	}
	
	public void redoHistory() {
		if(!replayMode) {
			System.out.println("replay mode entered");
			replayMode = true;
		}
		context.getAkkaModel().redoHistory();
	}

	public Map<String, Object> getTemp() {
		return temp;
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

	private <T> List<T> copy(List<T> list) {
		List<T> res = new ArrayList<>();
		res.addAll(list);
		return res;
	}

}
