package com.akkaVisualizor.visualModel.visual;

import java.util.HashMap;
import java.util.Map;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Pair;

public class VisualActorList {
	private final ObservableList<VisualActor> list;
	private final Map<Actor, VisualActor> actorToVisualActor;
	
	public VisualActorList(Context context) {
		list = FXCollections.observableArrayList();
		actorToVisualActor = new HashMap<>();
		
		// update automatically list when model changes
		context.getAkkaModel().getActorList().get().addListener((ListChangeListener.Change<? extends Actor> change)  -> {
			while (change.next()) {
				if(change.wasAdded()) {
					for (Actor actor : change.getAddedSubList()) {
						// add visual actor
						String name = actor.getName();
						
						// load coordinate
						double x = 0, y=0;
						@SuppressWarnings("unchecked")
						Pair<Double, Double> coord = (Pair<Double, Double>) context.getGlobalModel().getTemp().get(name);
						if(coord != null) {
							x = coord.getKey();
							y = coord.getValue();
						}
						
						VisualActor visualActor = new VisualActor(context, actor, name, x, y); 
						actorToVisualActor.put(actor, visualActor);
						Platform.runLater(() -> list.add(visualActor));
					}
				} else if (change.wasRemoved()) {
					for (Actor actor : change.getRemoved()) {
						// delete visual actor
						VisualActor visualActor = actorToVisualActor.get(actor);
						actorToVisualActor.remove(actor);
						Platform.runLater(() -> list.remove(visualActor));
						
						// deselect it
						context.getGlobalModel().deselectActor(visualActor);
					}
					
				}
			}
		}); 
	}
	
	public ObservableList<VisualActor> get() {
		return list;
	}
	
	public VisualActor get(Actor actor) {
		return actorToVisualActor.get(actor);
	}

}
