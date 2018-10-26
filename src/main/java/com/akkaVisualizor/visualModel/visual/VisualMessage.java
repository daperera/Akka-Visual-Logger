package com.akkaVisualizor.visualModel.visual;

import com.akkaVisualizor.akkaModel.Message;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;

public class VisualMessage {
	
	private static final double v = 0.005;
	
	private final Message message;
	private final DoubleProperty t;
	private final DoubleBinding x, y;
	private BooleanProperty isDelivered;
	
	public VisualMessage(Message message, VisualActor source, VisualActor target) {
		this.message = message;
		
		t = new SimpleDoubleProperty(0);
		DoubleBinding uX = target.getXProperty().subtract(source.getXProperty());
		DoubleBinding uY = target.getYProperty().subtract(source.getYProperty());
		x = source.getXProperty().add(uX.multiply(t));
		y = source.getYProperty().add(uY.multiply(t));
		isDelivered = new SimpleBooleanProperty(false);
		
		animate();
	}
	
	private void animate() {
		new Thread( () -> {
			// move from source to target
			double curr = System.currentTimeMillis();
			double newT = 0;
			while(t.get()<1) {
				newT += (-curr + (curr=System.currentTimeMillis()))*v;
				if(newT>1)
					newT=1;
				t.set(newT);
				sleep();
			}
			
			// wait for message delivery
			while(!message.isDelivered()) {
				sleep();
			}
			isDelivered.set(true);
		}).start();
	}
	
	public DoubleBinding getXProperty() {
		return x;
	}
	public DoubleBinding getYProperty() {
		return y;
	}
	
	public BooleanProperty getDeliveredProperty() {
		return isDelivered;
	}
	
	private void sleep() {
		try {
			Thread.sleep(20);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
