package com.akkaVisualizor.akkaModel;

import java.util.function.Supplier;

public class MessageType {

	private final String name;
	private final Supplier<Object> messageConstructor;
	
	public MessageType(String name, Supplier<Object> messageConstructor) {
		this.name = name;
		this.messageConstructor = messageConstructor;
	}

	public String getName() {
		return name;
	}

	public Object getInstance() {
		return messageConstructor.get();
	}

}
