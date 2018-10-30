package com.akkaVisualizor.javaFX.pane;

import com.akkaVisualizor.utils.Context;

import javafx.geometry.Orientation;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.layout.VBox;

public class MainPane extends VBox {

	private static double defaultHeight = 500;
	private static double defaultWidth = 800;
	
	private final Context context;
	private final MenuBar menuBar;
	private final MenuPane menuPane;
	private final SimulatorPane simulationPane;
	
	
	public MainPane(Context context) {
		this.context = context;
		
		// menu bar
		menuBar = menuBar();
		
		// main panel
		SplitPane mainPane = new SplitPane();
		menuPane = new MenuPane(context);
		simulationPane = new SimulatorPane(context); 
		mainPane.getItems().addAll(menuPane, simulationPane);

		// set style of main pane
		mainPane.setOrientation(Orientation.HORIZONTAL);
		mainPane.setPrefSize(defaultWidth, defaultHeight);
		mainPane.setDividerPositions(0.3);

		getChildren().addAll(menuBar, mainPane);
	}
	
	public MenuPane getMenuPane() {
		return menuPane;
	}

	public SimulatorPane getSimulationPane() {
		return simulationPane;
	}
	
	private MenuBar menuBar() {
        MenuItem menuHelp = new MenuItem("Help");
        menuHelp.setOnAction(e -> context.getGlobalMouseController().menuHelpClicked(menuHelp));
        Menu menuFile = new Menu("File");
        menuFile.getItems().add(menuHelp);
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menuFile);
        return menuBar;
	}

}
