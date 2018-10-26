package com.akkaVisualizor.javaFX.pane;

import com.akkaVisualizor.Context;

import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;

public class DuplicablePane extends TabPane {

	public DuplicablePane(Context context) {
		
		Tab nodePane = new Tab("Actors", new ActorPane(context));
		Tab nodeTypePane = new Tab("Actor Types", new ActorTypePane(context));
		Tab messagePane = new Tab("Messages", new MessageTypePane(context));
		//Tab test = new Tab("test", new ListOrganizer());
		nodePane.setClosable(false);
		nodeTypePane.setClosable(false);
		messagePane.setClosable(false);
		
		getTabs().addAll(nodePane, nodeTypePane, messagePane);
	}

}
