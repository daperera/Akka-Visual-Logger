package com.akkaVisualizor.javaFX.pane;

import java.util.HashMap;
import java.util.Map;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.javaFX.view.ActorView;
import com.akkaVisualizor.javaFX.view.ChannelView;
import com.akkaVisualizor.visualModel.visual.VisualActor;
import com.akkaVisualizor.visualModel.visual.VisualChannel;

import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.scene.layout.AnchorPane;

public class SimulatorPane extends AnchorPane {
/*
	private double newActorX, newActorY;
*/
	private final Map<VisualActor, ActorView> visualActorToActorView;
	private final Map<VisualChannel, ChannelView> visualChannelToChannelView;
	
	public SimulatorPane(Context context) {
		// init object var
		visualActorToActorView = new HashMap<>();
		visualChannelToChannelView = new HashMap<>();
		
		
		// init style
		setStyle("-fx-background-color: black;");

		
		
		// create ContextMenu
		/*
		ContextMenu contextMenu = new ContextMenu();
		MenuItem createActor = new MenuItem("Create Actor");
		createActor.setOnAction(e -> createActor(e));
		contextMenu.getItems().add(createActor);
		setOnContextMenuRequested(e-> { 
			contextMenu.show(this, e.getScreenX(), e.getScreenY()); 
			newActorX=e.getX(); // get click coordinate in order to create new actor
			newActorY=e.getY(); // at the correct place
		});
		*/
		
		// auto update actor view from model
		context.getGlobalModel().getVisualActorList().get().addListener((ListChangeListener.Change<? extends VisualActor> change)  -> {
			while (change.next()) {
				if(change.wasAdded()) {
					for (VisualActor visualActor : change.getAddedSubList()) {
						// add actor view
						ActorView actorView = new ActorView(context, visualActor);
						visualActorToActorView.put(visualActor, actorView);
						Platform.runLater(() -> SimulatorPane.this.getChildren().add(actorView));
					}
				}
				else if(change.wasRemoved()) {
					for (VisualActor visualActor : change.getRemoved()) {
						// remove actor view
						ActorView actorView = visualActorToActorView.get(visualActor);
						visualActorToActorView.remove(visualActor, actorView);
						Platform.runLater(() -> SimulatorPane.this.getChildren().remove(actorView));
						
					}
				}
			}
		}); 
		
		// auto update channel view from model
		context.getGlobalModel().getVisualChannelList().get().addListener((ListChangeListener.Change<? extends VisualChannel> change)  -> {
			while (change.next()) {
				if(change.wasAdded()) {
					for (VisualChannel visualChannel : change.getAddedSubList()) {
						// add channel view
						ChannelView channelView = new ChannelView(context, visualChannel);
						visualChannelToChannelView.put(visualChannel, channelView);
						Platform.runLater(() -> SimulatorPane.this.getChildren().add(channelView));
					}
				}
				else if(change.wasRemoved()) {
					for (VisualChannel visualChannel : change.getRemoved()) {
						// remove channel view
						ChannelView channelView = visualChannelToChannelView.get(visualChannel);
						visualChannelToChannelView.remove(visualChannel, channelView);
						Platform.runLater(() -> SimulatorPane.this.getChildren().remove(channelView));
						
					}
				}
			}
		}); 
		
		// create mouse listener
		setOnMousePressed(e -> context.getGlobalMouseController().onMousePressed(this, e));
		
		// handle drag and drop
		setOnDragDropped(e -> context.getGlobalMouseController().onDragDropped(this, e));
		setOnDragOver(e -> context.getGlobalMouseController().onDragOver(this, e));
	}

}
