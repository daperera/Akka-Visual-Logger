package com.akkaVisualizor.javaFX.pane;

import com.akkaVisualizor.Context;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;

public class MenuPane extends SplitPane {
	
	private static double minHeight = 300;
	private static double minWidth = 200;
	
	public MenuPane(Context context) {
		setMinSize(minWidth, minHeight);
		setOrientation(Orientation.HORIZONTAL);
		
		getItems().add(new DuplicablePane(context));
	}

	public void ready() {
		// TODO Auto-generated method stub
		
	}
}
