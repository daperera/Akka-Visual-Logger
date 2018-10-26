package com.akkaVisualizor.javaFX.view;

import java.awt.MenuItem;

import com.akkaVisualizor.visualModel.visual.VisualMessageType;

public class MessageTypeView extends MenuItem {
	private static final long serialVersionUID = 201810260200L;
	private VisualMessageType visualMessageType; 

	public MessageTypeView(VisualMessageType visualMessageType) {
		this.visualMessageType = visualMessageType;
		setName(visualMessageType.getMessageType().getName());
	}
	
	public VisualMessageType getModel() {
		return visualMessageType;
	}
}
