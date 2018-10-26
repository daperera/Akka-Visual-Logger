package com.akkaVisualizor.javaFX.pane;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.javaFX.view.ActorTypeView;
import com.akkaVisualizor.visualModel.visual.VisualActorType;

import javafx.scene.control.ListView;

public class ActorTypePane extends ListView<VisualActorType> {


	public ActorTypePane(Context context) {
		setCellFactory(param -> new ActorTypeView(context));
		setItems(context.getModel().getVisualActorTypeList().get());
	}

}
