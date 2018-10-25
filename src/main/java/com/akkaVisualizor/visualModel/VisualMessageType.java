package com.akkaVisualizor.visualModel;

import com.akkaVisualizor.akkaModel.MessageType;

public class VisualMessageType {
	private final MessageType messageType;
	public VisualMessageType(MessageType messageType) {
		this.messageType = messageType;
	}
	public MessageType getMessageType() {
		return messageType;
	}
}