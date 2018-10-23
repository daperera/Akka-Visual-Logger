package com.akkaVisualizor.visualModel;


import com.akkaVisualizor.Context;
import com.akkaVisualizor.javaFX.ActorView;

import javafx.scene.input.MouseEvent;

public class GlobalMouseController {
	private final Context context;

	public GlobalMouseController(Context context) {
		this.context = context;
	}

	public void onMousePressed(ActorView actorView, MouseEvent e) {
		VisualActor actor = actorView.getModel();

		// handle selection
		if(e.isShiftDown()) {
			context.getModel().addToSelectedActorList(actor);
		} else if(e.isControlDown()) {
			context.getModel().createBidirectionalChannelTo(actor);
		} else if(e.isAltDown()) {
			context.getModel().createUnidirectionalChannelTo(actor);
		} else {
			context.getModel().selectActor(actor);
		}

	}

	public void onMouseDragged(ActorView actorView, MouseEvent e) {
		VisualActor target = actorView.getModel();
		
		// handle drag and move
		target.drag(e.getSceneX(), e.getSceneY());
	}

}
