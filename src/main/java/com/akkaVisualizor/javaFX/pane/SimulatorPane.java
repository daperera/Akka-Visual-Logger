package com.akkaVisualizor.javaFX.pane;

import java.util.Optional;

import com.akkaVisualizor.Context;

import javafx.event.ActionEvent;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.AnchorPane;

public class SimulatorPane extends AnchorPane {

	private final Context context;
	private double newActorX, newActorY;

	public SimulatorPane(Context context) {
		// init object var
		this.context = context;

		// init style
		setStyle("-fx-background-color: black;");

		// create ContextMenu
		ContextMenu contextMenu = new ContextMenu();
		MenuItem createActor = new MenuItem("Create Actor");
		createActor.setOnAction(e -> createActor(e));
		contextMenu.getItems().add(createActor);
		setOnContextMenuRequested(e-> { 
			contextMenu.show(this, e.getScreenX(), e.getScreenY()); 
			newActorX=e.getX(); // get click coordinate in order to create new actor
			newActorY=e.getY(); // at the correct place
		});
		
		// create mouse and keyBoardListener
		setOnMousePressed(e -> context.getGlobalMouseController().onMousePressed(this, e));
		setOnKeyPressed(e -> context.getGlobalMouseController().onKeyPressed(this, e));
	}

	private void createActor(ActionEvent e) {
		boolean restarted = false;
		boolean correctName = false;
		String name = "";

		// ask for actor name
		while(!correctName) {
			// create dialog
			TextInputDialog dialog = new TextInputDialog();
			if(restarted) {
				dialog.setHeaderText("Invalid name. Please enter a different name");
			} else {
				dialog.setHeaderText("Please enter the name of the new Actor");
			}
			dialog.setTitle("Actor Name");
			dialog.setContentText("Actor name:");

			// get response value
			Optional<String> result = dialog.showAndWait();
			if(result.isPresent()) {
				name = result.get();
			}

			// try to create actor with this name
			try {
				context.getModel().createActor(name, newActorX, newActorY);
				correctName = true;
			} catch(Exception exception) {
				exception.printStackTrace();
				restarted = true;
			}
		}

	}
}
