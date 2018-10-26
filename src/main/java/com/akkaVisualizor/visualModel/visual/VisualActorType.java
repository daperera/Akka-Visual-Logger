package com.akkaVisualizor.visualModel.visual;

import java.io.Serializable;

import com.akkaVisualizor.akkaModel.ActorType;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.scene.input.DataFormat;

public class VisualActorType implements Serializable {
	
	private static final long serialVersionUID = 201810260117L;
	private final ActorType actorType;
	
	
	private transient final BooleanProperty changedProperty;
	
	public static final DataFormat format = new DataFormat("com.akkaVisualizor.visualModel.visual.VisualActorType");
	
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
