package com.akkaVisualizor.visualModel.visual;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class VisualActorList implements Iterable<VisualActor> {
	private final List<VisualActor> list;
	private final BooleanProperty changeProperty;
	
	public VisualActorList() {
		list = new ArrayList<>();
		changeProperty = new SimpleBooleanProperty(false);
	}
	
	public void add(VisualActor visualActor) {
		list.add(visualActor);
	}

	public BooleanProperty getChangeProperty() {
		return changeProperty;
	}
	
	public void notifyChange() {
		changeProperty.set(!changeProperty.get());
	}

	@Override
	public Iterator<VisualActor> iterator() {
		return list.iterator();
	}

	public boolean contains(VisualActor t) {
		return list.contains(t);
	}
	
}
