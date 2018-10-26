package com.akkaVisualizor.javaFX.pane;

import java.util.ArrayList;
import java.util.List;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.javaFX.view.ActorView;
import com.akkaVisualizor.visualModel.visual.VisualActor;
import com.akkaVisualizor.visualModel.visual.VisualActorList;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.AnchorPane;

public class ActorPane extends AnchorPane{
	private List<ActorView> actorViewList;
	private Context context;

	public ActorPane(Context context) {
		this.context = context;
		actorViewList = new ArrayList<>();
		update();
		
		// update when the visualActorTypeList change
		context.getModel().getVisualActorList().getChangeProperty().addListener((ChangeListener<Boolean>) (o, oldVal,  newVal) -> { 
			update();
		});
	}

	public void update() {
		VisualActorList ActorTypeList = context.getModel().getVisualActorList();
		for(VisualActor type : ActorTypeList) {
			// detect if type was added to ActorTypeList since last update
			boolean contains = false;
			for(ActorView view : actorViewList) {
				if(view.getModel().equals(type)) {
					contains = true;
					break;
				}
			}
			if(!contains) {
				//actorViewList.add(new ActorView(type));
			}
		}
	}
}
