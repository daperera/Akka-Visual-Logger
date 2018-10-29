package com.akkaVisualizor.visualModel.visual;

import java.util.HashMap;
import java.util.Map;

import com.akkaVisualizor.akkaModel.Actor;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VisualActorList {
	private final ObservableList<VisualActor> list;
	private final Map<Actor, VisualActor> actorToVisualActor;
	
	public VisualActorList() {
		list = FXCollections.observableArrayList();
		actorToVisualActor = new HashMap<>();
	}
	
	public void add(VisualActor visualActor) {
		Platform.runLater(() -> list.add(visualActor));
		actorToVisualActor.put(visualActor.getActor(), visualActor);
	}
	
	public void delete(VisualActor visualActor) {
		Platform.runLater(() -> list.remove(visualActor));
		actorToVisualActor.remove(visualActor.getActor());
		visualActor.delete();
	}
	
	public void remove(Actor actor) {
		delete(actorToVisualActor.get(actor));
	}
	
	public ObservableList<VisualActor> get() {
		return list;
	}
	
	public VisualActor get(Actor actor) {
		return actorToVisualActor.get(actor);
	}

}
