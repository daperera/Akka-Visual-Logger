package com.akkaVisualizor.akkaModel;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.akkaVisualizor.utils.Context;

import akka.actor.ActorRef;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ActorList {
	private final ObservableList<Actor> list;
	private final Map<ActorRef, Actor> actorRefToActor;
	private final Context context;
	
	public ActorList(Context context) {
		this.context = context;
		list = FXCollections.observableArrayList();
		actorRefToActor = new HashMap<>();
	}
	
	public void add(Actor actor) {
		actorRefToActor.put(actor.getActorRef(), actor);
		Platform.runLater(() -> list.add(actor));
	}
	
	public void delete(Actor actor) {
		// remove actor internally
		actorRefToActor.remove(actor.getActorRef());
		Platform.runLater(() -> list.remove(actor));
		
		// remove channels linked to this actor
		context.getAkkaModel().getChannelList().remove(actor);
		
	}
	
	public void delete(ActorRef actor) {
		delete(actorRefToActor.get(actor));
	}
	
	public void clear() {
		Iterator<Actor> it = list.iterator();
		while(it.hasNext()) {
			Actor actor = it.next();
			
			// remove actor internally
			actorRefToActor.remove(actor.getActorRef());
			it.remove();
			
			// remove channels linked to this actor
			context.getAkkaModel().getChannelList().remove(actor);
		}
	}
	
	public ObservableList<Actor> get() {
		return list;
	}
	
	public Actor get(ActorRef actorRef) {
		return actorRefToActor.get(actorRef);
	}

	

}
