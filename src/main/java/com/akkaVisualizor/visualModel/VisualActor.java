package com.akkaVisualizor.visualModel;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Actor;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class VisualActor {
	
	private BooleanProperty selected;
//	private String name;
//	private final Delta dragDelta = new Delta();
//	private double deltaX, deltaY;
	private DoubleProperty x, y;
	private Actor actor;
//	private Context context;
	
	public VisualActor(Context context, Actor actor, String name, double x, double y) {
//		this.context = context;
		this.actor = actor;
		this.x = new SimpleDoubleProperty(x);
		this.y = new SimpleDoubleProperty(y);
		
//		this.name = name;
		
		selected = new SimpleBooleanProperty(false);
	}

	public void setSelected() {
		selected.set(true);
	}

	public void setDeselected() {
		selected.set(false);
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
		return selected;
	}
	
//	private class Delta { double x, y; }
}
