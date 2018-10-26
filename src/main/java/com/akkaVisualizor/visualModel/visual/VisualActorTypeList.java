package com.akkaVisualizor.visualModel.visual;

import java.util.Iterator;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VisualActorTypeList implements Iterable<VisualActorType> {
	private final ObservableList<VisualActorType> list;
	private final BooleanProperty changeProperty;

	public VisualActorTypeList() {
		list = FXCollections.observableArrayList();
		changeProperty = new SimpleBooleanProperty(false);
	}

	public void add(VisualActorType visualActorType) {
//		list.add(visualActorType);
		Platform.runLater(() -> list.add(visualActorType));
		Platform.runLater(() -> changeProperty.set(!changeProperty.get()));
	}

	public BooleanProperty getChangeProperty() {
		return changeProperty;
	}

	@Override
	public Iterator<VisualActorType> iterator() {
		return list.iterator();
	}

	public ObservableList<VisualActorType> get() {
		return list;
	}
	
}
