package com.akkaVisualizor.javaFX.view;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.visualModel.visual.VisualActor;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class ActorView extends StackPane {

	private final VisualActor actor;
	private final Circle circle;

	public ActorView(Context context, VisualActor actor, String name, double x, double y) {
		// init variables
		this.actor = actor;

		Configuration conf = context.getConfiguration();

		setManaged(false);
		layoutXProperty().bind(actor.getXProperty());
		layoutYProperty().bind(actor.getYProperty());

		// create javafxNode hierarchy
		circle = new Circle();
		Text text = new Text(name);
		text.setBoundsType(TextBoundsType.VISUAL); 
		getChildren().addAll(circle, text);

		
		// init style
		circle.setRadius(conf.getActorRadius());
		circle.setFill(conf.getActorFill());
		circle.setOpacity(conf.getActorOpacity());
		circle.setStroke(conf.getActorStroke());
		circle.setStrokeType(conf.getActorStrokeType());
		circle.setStrokeWidth(conf.getStrokeWidth());
		DropShadow borderGlow = new DropShadow();
		borderGlow.setColor(Color.BLUE);
		borderGlow.setOffsetX(0f);
		borderGlow.setOffsetY(0f);

		// init controller
		setOnMousePressed(e -> context.getGlobalMouseController().onMousePressed(this, e));
		setOnMouseDragged(e -> context.getGlobalMouseController().onMouseDragged(this, e));
		
		// add border glow effect when selected
		actor.getSelectedProperty().addListener((ChangeListener<Boolean>) (o, oldVal,  newVal) -> { 
			if(newVal) 
				circle.setEffect(borderGlow); 
			else
				circle.setEffect(null);
		});

		// delete itself when the actor is set deleted
		actor.getDeletedProperty().addListener((ChangeListener<Boolean>) (o, oldVal,  newVal) -> { 
			if(newVal) {
				// use Platform  in order to avoid throwing IllegalStateException by running from a non-JavaFX thread
				Platform.runLater(() -> ((Pane) ActorView.this.getParent()).getChildren().remove(ActorView.this));
			}
		});
	}

	public VisualActor getModel() {
		return actor;
	}

}
