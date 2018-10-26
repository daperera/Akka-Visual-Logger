package com.akkaVisualizor.visualModel.visual;

import java.util.Iterator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VisualActorTypeList implements Iterable<VisualActorType> {
	private final ObservableList<VisualActorType> list;

	public VisualActorTypeList() {
		list = FXCollections.observableArrayList();
	}

	public void add(VisualActorType visualActorType) {
		Platform.runLater(() -> list.add(visualActorType));
	}

	@Override
	public Iterator<VisualActorType> iterator() {
		return list.iterator();
	}

	public ObservableList<VisualActorType> get() {
		return list;
	}
	
}
