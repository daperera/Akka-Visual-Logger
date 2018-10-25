package com.akkaVisualizor.visualModel;

import com.akkaVisualizor.akkaModel.ActorType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class VisualActorType {
	private final ActorType actorType;
	private final BooleanProperty changedProperty;
	public VisualActorType(ActorType actorType) {
		this.actorType = actorType;
		this.changedProperty = new SimpleBooleanProperty(false);
	}
	public ActorType getActorType() {
		return actorType;
	}
	public BooleanProperty getChangedProperty() {
		return changedProperty;
	}
}
