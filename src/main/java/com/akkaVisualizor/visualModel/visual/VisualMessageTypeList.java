package com.akkaVisualizor.visualModel.visual;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VisualMessageTypeList {
	private final ObservableList<VisualMessageType> list;
	
	public VisualMessageTypeList() {
		list = FXCollections.observableArrayList();
	}
	
	public void add(VisualMessageType visualMessageType) {
		Platform.runLater(() -> list.add(visualMessageType));
	}

	public boolean contains(VisualMessageType t) {
		return list.contains(t);
	}
	
	public ObservableList<VisualMessageType> get() {
		return list;
	}
	
}
