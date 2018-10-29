package com.akkaVisualizor.visualModel.visual;


import com.akkaVisualizor.akkaModel.Message;

import javafx.animation.PauseTransition;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.util.Duration;

public class VisualMessage {

	private static final double v = 0.01;
	private static final long animationSpacing = 10;

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
		PauseTransition wait = new PauseTransition(Duration.millis(animationSpacing));
		wait.setOnFinished((e) -> {
			// move from source to target
			double newT = t.get() + animationSpacing*v;;
			if(newT>1)
				newT=1;
			t.set(newT);

			// wait for message delivery
			if(message.isDelivered() && newT==1) {
				isDelivered.set(true);
			} else {
				wait.playFromStart();
			}
		});
		wait.play();
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

}
