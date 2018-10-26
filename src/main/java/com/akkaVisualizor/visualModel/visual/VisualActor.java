package com.akkaVisualizor.visualModel.visual;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class VisualActor {
	
	private BooleanProperty selectedProperty;
	private double dragDeltaX, dragDeltaY;
	private DoubleProperty x, y;
	private Actor actor;
	private BooleanProperty deletedProperty;
	
	
	public VisualActor(Context context, Actor actor, String name, double x, double y) {
		this.actor = actor;
		this.x = new SimpleDoubleProperty(x);
		this.y = new SimpleDoubleProperty(y);
		
		selectedProperty = new SimpleBooleanProperty(false);
		deletedProperty = new SimpleBooleanProperty(false);
	}

	public void setSelected() {
		selectedProperty.set(true);
	}

	public void setDeselected() {
		selectedProperty.set(false);
	}

	public Actor getActor() {
		return actor;
	}
	
	public void drag(double dragX, double dragY) {
		x.set(dragX);
		y.set(dragY);
	}
	
	public DoubleProperty getXProperty() {
		return x;
	}
	
	public DoubleProperty getYProperty() {
		return y;
	}
	
	public BooleanProperty getSelectedProperty() {
		return selectedProperty;
	}
	
	public void setDragDelta(double dragDeltaX, double dragDeltaY) {
		this.dragDeltaX = dragDeltaX;
		this.dragDeltaY = dragDeltaY;
	}

	public double getDragDeltaX() {
		return dragDeltaX;
	}
	
	public double getDragDeltaY() {
		return dragDeltaY;
	}

	public BooleanProperty getDeletedProperty() {
		return deletedProperty;
	}
	
	public void delete() {
		deletedProperty.set(true);
	}

//	private class Delta { double x, y; }
}
