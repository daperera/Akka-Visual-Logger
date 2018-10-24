package com.akkaVisualizor.javaFX.pane;

import com.akkaVisualizor.Context;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class DuplicablePane extends TabPane {

	public DuplicablePane(Context context) {
		
		Tab nodePane = new Tab("Actors", new NodePane());
		Tab nodeTypePane = new Tab("Actor Types", new NodeTypePane());
		Tab messagePane = new Tab("Mesages", new MessagePane());
		nodePane.setClosable(false);
		nodeTypePane.setClosable(false);
		messagePane.setClosable(false);
		
		getTabs().addAll(nodePane, nodeTypePane, messagePane);
	}

}
