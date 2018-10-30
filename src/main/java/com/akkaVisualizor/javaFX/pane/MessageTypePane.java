package com.akkaVisualizor.javaFX.pane;

import com.akkaVisualizor.javaFX.view.MessageTypeListView;
import com.akkaVisualizor.utils.Context;
import com.akkaVisualizor.visualModel.visual.VisualMessageType;

import javafx.scene.control.ListView;

public class MessageTypePane extends ListView<VisualMessageType> {

	public MessageTypePane(Context context) {
		setCellFactory(param -> new MessageTypeListView(context));
		setItems(context.getGlobalModel().getVisualMessageTypeList().get());
	}
	
}
