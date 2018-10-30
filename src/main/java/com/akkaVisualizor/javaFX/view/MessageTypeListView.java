package com.akkaVisualizor.javaFX.view;

import com.akkaVisualizor.utils.Context;
import com.akkaVisualizor.visualModel.visual.VisualMessageType;

import javafx.scene.control.ListCell;

public class MessageTypeListView extends ListCell<VisualMessageType> {

	public MessageTypeListView(Context context) {
		// do nothing
//		setOnDragDetected(e -> context.getGlobalMouseController().onDragDetected(this, e));
		setOnMousePressed(e -> context.getGlobalMouseController().onMousePressed(this, e));
	}

	@Override
	protected void updateItem(VisualMessageType item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			setText(item.getMessageType().getName());
		}
	}

}
