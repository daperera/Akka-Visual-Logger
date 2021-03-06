package com.akkaVisualizor.visualModel;


import java.util.HashMap;
import java.util.Map;

import com.akkaVisualizor.javaFX.pane.SimulatorPane;
import com.akkaVisualizor.javaFX.view.ActorTypeListView;
import com.akkaVisualizor.javaFX.view.ActorView;
import com.akkaVisualizor.javaFX.view.ChannelView;
import com.akkaVisualizor.javaFX.view.MessageTypeListView;
import com.akkaVisualizor.utils.Context;
import com.akkaVisualizor.visualModel.visual.VisualActor;
import com.akkaVisualizor.visualModel.visual.VisualActorType;
import com.akkaVisualizor.visualModel.visual.VisualChannel;
import com.akkaVisualizor.visualModel.visual.VisualMessageType;

import javafx.application.Platform;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;

public class GlobalController {
	private final Context context;
	private Map<String, Boolean> currentlyActiveKeys;

	public GlobalController(Context context) {
		this.context = context;
		currentlyActiveKeys = new HashMap<>();
	}

	public void onDragDetected(ActorTypeListView view, MouseEvent e) {
		// create clipboard
		ClipboardContent content = new ClipboardContent();
		content.putString(view.getItem().getActorType().getType());
		content.put(VisualActorType.format, view.getItem());

		// create dragboard
		Dragboard dragboard = context.getApp().getScene().startDragAndDrop(TransferMode.MOVE);
		dragboard.setDragView(ActorTypeListView.dragboardImage(view.getItem().getActorType().getType()));
		dragboard.setContent(content);

		e.consume();
	}
	
	public void onDragOver(ActorTypeListView view, DragEvent e) {
        e.acceptTransferModes(TransferMode.ANY);
        e.consume();
	}
	
	public void onDragOver(SimulatorPane pane, DragEvent e) {
		e.acceptTransferModes(TransferMode.MOVE);
        e.consume();
	}

	public void onDragDropped(SimulatorPane simulatorPane, DragEvent e) {
		// complete the drag and drop
		e.acceptTransferModes(TransferMode.ANY);
		e.setDropCompleted(true);
		e.consume();
		
		// delay before executing next instruction later
		VisualActorType t = (VisualActorType) e.getDragboard().getContent(VisualActorType.format);
		double x = e.getX(), y = e.getY();
		Platform.runLater(() -> {
			context.getGlobalModel().requestCreateActorFromType(t, x, y);
		});
	}

	public void onMousePressed(ActorView actorView, MouseEvent e) {
		VisualActor actor = actorView.getModel();

		// handle selection
		if(e.isShiftDown()) {
			context.getGlobalModel().addToSelectedActorList(actor);
		} else if(e.isControlDown()) {
			context.getGlobalModel().createChannelTo(actor);
		} else if(e.isAltDown()) {
			context.getGlobalModel().createMessageTo(actor);;
		} else {
			context.getGlobalModel().selectActor(actor);
		}

		// handle drag and move
		//		actor.setDragDelta(e.getSceneX(), e.getSceneY());
		actor.setDragDelta(e.getSceneX(), e.getSceneY());


		e.consume();
	}

	public void onMouseDragged(ActorView actorView, MouseEvent e) {
		VisualActor actor = actorView.getModel();

		// handle drag and move
		double x = actor.getXProperty().get(), y= actor.getYProperty().get();
		actor.drag(x+e.getSceneX()-actor.getDragDeltaX(), y+e.getSceneY()-actor.getDragDeltaY());
		actor.setDragDelta(e.getSceneX(), e.getSceneY());
		e.consume();
	}

	public void onMousePressed(SimulatorPane simulatorPane, MouseEvent e) {
		context.getGlobalModel().simulationPaneClicked();
		e.consume();
	}

	public void onMousePressed(ChannelView channelView, MouseEvent e) {
		VisualChannel channel = channelView.getModel();

		// handle selection
		if(e.isShiftDown()) {
			context.getGlobalModel().addToSelectedChannelList(channel);
		} else {
			context.getGlobalModel().selectChannel(channel);
		}
		e.consume();
	}
	
	public void onMousePressed(MessageTypeListView messageTypeListView, MouseEvent e) {
		VisualMessageType messageType = messageTypeListView.getItem();
		context.getGlobalModel().selectMessageType(messageType);
	}

	public void onKeyPressed(KeyEvent e) {
		String codeString = e.getCode().toString();
		if (!currentlyActiveKeys.containsKey(codeString)) {
			currentlyActiveKeys.put(codeString, true);
			keyPressed(e.getCode());
		}
		e.consume();
	}

	public void onKeyReleased(KeyEvent e) {
		currentlyActiveKeys.remove(e.getCode().toString());
		keyReleased(e.getCode());
		e.consume();
	}

	private void keyPressed(KeyCode code) {
		if(code.equals(KeyCode.DELETE)) {
			context.getGlobalModel().nodeDeletion();
		} else if(code.equals(KeyCode.BACK_SPACE)) {
			context.getGlobalModel().resetHistory();
		} else if(code.equals(KeyCode.Q)) {
			context.getGlobalModel().undoHistory();
		} else if(code.equals(KeyCode.D)) {
			context.getGlobalModel().redoHistory();
		}
	}

	private void keyReleased(KeyCode code) {
		// do something when key is released
	}

	public void menuHelpClicked(MenuItem menuHelp) {
		context.getApp().displayHelp();
	}

}
