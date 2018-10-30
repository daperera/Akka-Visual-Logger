package com.akkaVisualizor.javaFX.pane;

import com.akkaVisualizor.Context;

import javafx.geometry.Orientation;
import javafx.scene.control.SplitPane;

public class MainPane extends SplitPane {

	private static double defaultHeight = 500;
	private static double defaultWidth = 800;
	
	private final MenuPane menuPane;
	private final SimulatorPane simulationPane;
	
	public MainPane(Context context) {
		menuPane = new MenuPane(context);
		simulationPane = new SimulatorPane(context); 
		getItems().addAll(menuPane, simulationPane);

		// set style
		setOrientation(Orientation.HORIZONTAL);
		setPrefSize(defaultWidth, defaultHeight);
		setDividerPositions(0.3);
		
	}

	public MenuPane getMenuPane() {
		return menuPane;
	}

	public SimulatorPane getSimulationPane() {
		return simulationPane;
	}

}
