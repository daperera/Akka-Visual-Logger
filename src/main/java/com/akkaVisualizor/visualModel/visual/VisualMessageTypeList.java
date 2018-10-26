package com.akkaVisualizor.visualModel.visual;

import java.util.Iterator;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class VisualMessageTypeList implements Iterable<VisualMessageType> {
	private final ObservableList<VisualMessageType> list;
	private final BooleanProperty changeProperty;
	
	public VisualMessageTypeList() {
		list = FXCollections.observableArrayList();
		changeProperty = new SimpleBooleanProperty(false);
	}
	
	public void add(VisualMessageType visualMessageType) {
		list.add(visualMessageType);
	}

	public BooleanProperty getChangeProperty() {
		return changeProperty;
	}
	
	public void notifyChange() {
		changeProperty.set(!changeProperty.get());
	}

	@Override
	public Iterator<VisualMessageType> iterator() {
		return list.iterator();
	}

	public boolean contains(VisualMessageType t) {
		return list.contains(t);
	}
	
	public List<VisualMessageType> get() {
		return list;
	}
	
}
