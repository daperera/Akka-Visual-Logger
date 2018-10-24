package com.akkaVisualizor.visualModel;


import java.util.HashMap;
import java.util.Map;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.javaFX.pane.SimulatorPane;
import com.akkaVisualizor.javaFX.view.ActorView;
import com.akkaVisualizor.javaFX.view.ChannelView;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;

public class GlobalMouseController {
	private final Context context;
	private Map<String, Boolean> currentlyActiveKeys;

	public GlobalMouseController(Context context) {
		this.context = context;
		currentlyActiveKeys = new HashMap<>();
	}

	public void onMousePressed(ActorView actorView, MouseEvent e) {
		VisualActor actor = actorView.getModel();

		// handle selection
		if(e.isShiftDown()) {
			context.getModel().addToSelectedActorList(actor);
		} else if(e.isControlDown()) {
			context.getModel().createBidirectionalChannelTo(actor);
		} else if(e.isAltDown()) {
			context.getModel().createMessageTo(actor);;
		} else {
			context.getModel().selectActor(actor);
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
		context.getModel().simulationPaneClicked();
		e.consume();
	}

	public void onKeyPressed(SimulatorPane simulatorPane, KeyEvent e) {
		if(e.getCode().equals(KeyCode.DELETE)) {
			context.getModel().nodeDeletion();
		}
		e.consume();
	}

	public void onMousePressed(ChannelView channelView, MouseEvent e) {
		VisualChannel channel = channelView.getModel();
		
		// handle selection
		if(e.isShiftDown()) {
			context.getModel().addToSelectedChannelList(channel);
		} else {
			context.getModel().selectChannelView(channel);
		}
		e.consume();
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
			context.getModel().nodeDeletion();
		}
	}
	
	private void keyReleased(KeyCode code) {
		// do something when key is released
	}
}
