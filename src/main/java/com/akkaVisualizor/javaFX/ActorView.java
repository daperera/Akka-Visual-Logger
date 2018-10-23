package com.akkaVisualizor.javaFX;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.visualModel.VisualActor;

import javafx.beans.value.ChangeListener;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class ActorView extends StackPane {

//	private final Context context;
	private final VisualActor actor;
	private final DropShadow borderGlow;
//	final Delta dragDelta = new Delta();
	private final Circle circle;
	

	public ActorView(Context context, VisualActor actor, String name, double x, double y) {
		// init variables
//		this.context = context;
		this.actor = actor;

		Configuration conf = context.getConfiguration();
		
		//setLayoutX(x);
		//setLayoutY(y);
		
		
		layoutXProperty().bind(actor.getXProperty());
		layoutYProperty().bind(actor.getYProperty());
		
		// create javafxNode hierarchy
		circle = new Circle();
		//circle.setCenterX(x);
		//circle.setCenterY(y);
		Text text = new Text(name);
		text.setBoundsType(TextBoundsType.VISUAL); 
//		text.xProperty().bind(circle.centerXProperty());
//		text.yProperty().bind(circle.centerYProperty());
		getChildren().addAll(circle, text);
		

		// init style
		
		circle.setRadius(conf.getActorRadius());
		circle.setFill(conf.getActorFill());
		circle.setOpacity(conf.getActorOpacity());
		circle.setStroke(conf.getActorStroke());
		circle.setStrokeType(conf.getActorStrokeType());
		circle.setStrokeWidth(conf.getStrokeWidth());
		borderGlow = new DropShadow();
		borderGlow.setColor(Color.RED);
		borderGlow.setOffsetX(0f);
		borderGlow.setOffsetY(0f);

		// init controller
		setOnMousePressed(e -> context.getGlobalMouseController().onMousePressed(this, e));
		setOnMouseDragged(e -> context.getGlobalMouseController().onMouseDragged(this, e));
		//circle.setOnContextMenuRequested(e -> contextMenu.show(getParent(), e.getScreenX(), e.getScreenY()));
		actor.getSelectedProperty().addListener((ChangeListener<Boolean>) (o, oldVal,  newVal) -> { 
		if(newVal) 
			circle.setEffect(borderGlow); 
		else
			circle.setEffect(null);
		});
			
		
	}
	/*
	private void onMousePressed(MouseEvent e) {
		context.getGlobalMouseController().onMousePressed(this, e);
		// handle drag and move
		dragDelta.x = getLayoutX() - e.getSceneX();
		dragDelta.y = getLayoutY() - e.getSceneY();
	}
	
	private void onMouseDragged(MouseEvent e) {
		context.getGlobalMouseController().onMouseDragged(this, e);
		// handle drag and move
		double newX = e.getSceneX() + dragDelta.x;
		double newY = e.getSceneY() + dragDelta.y;
		setLayoutX(newX);
		setLayoutY(newY);
	}
	*/
	/*
	public DoubleBinding getCenterXProperty() {
		return layoutXProperty().add(circle.getRadius());
	}
	
	public DoubleBinding getCenterYProperty() {
		return layoutYProperty().add(circle.getRadius());
	}
	*/
	
/*
	public void setSelected() {
		circle.setEffect(borderGlow);
	}

	public void setDeselected() {
		circle.setEffect(null);
	}
*/
	public VisualActor getModel() {
		return actor;
	}
	
}
