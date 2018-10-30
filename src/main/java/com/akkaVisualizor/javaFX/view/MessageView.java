package com.akkaVisualizor.javaFX.view;

import com.akkaVisualizor.utils.Context;
import com.akkaVisualizor.visualModel.visual.VisualMessage;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

public class MessageView extends StackPane {

	private static double actorRadius = 10;
	
	private static double actorOpacity = 0.74;
	private static Color actorFill = Color.RED;
	private static Color actorStroke = Color.ROSYBROWN;
	private static StrokeType actorStrokeType = StrokeType.INSIDE;
	private static double strokeWidth = 0.5;
	
	private final Circle circle;
	
	

	public MessageView(Context context, VisualMessage message) {
		// init variables
		
		// bind coordinate to those of the visual message
		setManaged(false);
		layoutXProperty().bind(message.getXProperty());
		layoutYProperty().bind(message.getYProperty());
		
		// create javafxNode hierarchy
		circle = new Circle();
		getChildren().add(circle);
		

		// init style
		setMouseTransparent(true);
		circle.setRadius(actorRadius);
		circle.setFill(actorFill);
		circle.setOpacity(actorOpacity);
		circle.setStroke(actorStroke);
		circle.setStrokeType(actorStrokeType);
		circle.setStrokeWidth(strokeWidth);
		
		// delete itself when the message is delivered
		message.getDeliveredProperty().addListener((ChangeListener<Boolean>) (o, oldVal,  newVal) -> { 
			if(newVal) {
				// use Platform  in order to avoid throwing IllegalStateException by running from a non-JavaFX thread
				Platform.runLater(() -> ((Pane) MessageView.this.getParent()).getChildren().remove(MessageView.this));
			}
		});
	}
}
