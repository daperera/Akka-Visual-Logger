package com.akkaVisualizor.javaFX.view;

import com.akkaVisualizor.utils.Context;
import com.akkaVisualizor.visualModel.visual.VisualActorType;

import javafx.scene.SnapshotParameters;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;

public class ActorTypeListView extends ListCell<VisualActorType> {

	private static final int size = 60;
	private static final double radius = size/2;
	private static final double opacity = 0.74;
	private static final Color fill = Color.valueOf("7d97ae");
	private static final Color stroke = Color.ALICEBLUE;
	private static final StrokeType strokeType = StrokeType.INSIDE;
	private static final double strokeWidth = 0.5;
	
	public ActorTypeListView(Context context) {

		// allow drag and drop
		setOnDragDetected(e -> context.getGlobalMouseController().onDragDetected(this, e));
		setOnDragOver(e -> context.getGlobalMouseController().onDragOver(this, e));
	}


	@Override
	protected void updateItem(VisualActorType item, boolean empty) {
		super.updateItem(item, empty);
		if (item != null) {
			setText(item.getActorType().getType());
		}
	}

	public static Image dragboardImage(String name) {
		
		// construct image
		StackPane pane = new StackPane();
		
		// create javafxNode hierarchy
		Circle circle = new Circle();
		Text text = new Text(name);
		text.setBoundsType(TextBoundsType.VISUAL); 
		pane.getChildren().addAll(circle, text);

		// init style
		circle.setRadius(radius);
		circle.setFill(fill);
		circle.setOpacity(opacity);
		circle.setStroke(stroke);
		circle.setStrokeType(strokeType);
		circle.setStrokeWidth(strokeWidth);
		
		// take assemble item and take snapshot
		WritableImage img = new WritableImage(size, size) ;
		SnapshotParameters sp = new SnapshotParameters();
		sp.setFill(Color.TRANSPARENT);
		pane.snapshot(sp, img);
		
		return img ;
	}
}
