package com.akkaVisualizor.visualModel.visual;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VisualActorTypeList {
	private final ObservableList<VisualActorType> list;

	public VisualActorTypeList() {
		list = FXCollections.observableArrayList();
	}

	public void add(VisualActorType visualActorType) {
		Platform.runLater(() -> list.add(visualActorType));
	}

	public ObservableList<VisualActorType> get() {
		return list;
	}
	
}
