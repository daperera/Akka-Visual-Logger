package com.akkaVisualizor.akkaModel;

import java.io.Serializable;
import java.util.function.Supplier;

import akka.actor.ActorSystem;
import akka.actor.Props;

public class ActorType implements Serializable {

	private static final long serialVersionUID = 201810261018L;
	private final String typeClass;
	private final SerializableSupplier<Props> typeConstructor;
	
	public ActorType(String typeClass, SerializableSupplier<Props> typeConstructor, ActorSystem system) {
		this.typeClass = typeClass;
		this.typeConstructor = typeConstructor;
	}

	public String getType() {
		return typeClass;
	}
	
	public Supplier<Props> getTypeConstructor() {
		return typeConstructor;
	}
	
	public interface SerializableSupplier<T> extends Supplier<T>, Serializable {}

}
