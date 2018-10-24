package com.akkaVisualizor.javaFX.view;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.visualModel.VisualChannel;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeLineCap;

public class ChannelView extends Line {
	//	private Context context;
	//	private VisualChannel channel;

	public ChannelView(Context context, VisualChannel visualChannel) {
		// init variables
		//				this.context = context;
		//				this.channel = channel;


		startXProperty().bind(visualChannel.getSource().getXProperty());
		startYProperty().bind(visualChannel.getSource().getYProperty());
		endXProperty().bind(visualChannel.getTarget().getXProperty());
		endYProperty().bind(visualChannel.getTarget().getYProperty());

		// init style
		setStrokeWidth(2);
		setStroke(Color.GRAY.deriveColor(0, 1, 1, 0.5));
		setStrokeLineCap(StrokeLineCap.BUTT);
		getStrokeDashArray().setAll(10.0, 5.0);
		//setMouseTransparent(true);
	}

}
