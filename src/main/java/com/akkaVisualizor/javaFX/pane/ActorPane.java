package com.akkaVisualizor.javaFX.pane;

import java.util.HashMap;
import java.util.Map;

import com.akkaVisualizor.akkaModel.ActorState;
import com.akkaVisualizor.utils.Context;
import com.akkaVisualizor.visualModel.visual.VisualActor;

import javafx.collections.ListChangeListener;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class ActorPane extends TreeView<String> {

	private final Map<VisualActor, TreeItem<String>> visualActorToTreeItem; 
	
	public ActorPane(Context context) {
		visualActorToTreeItem = new HashMap<>();

		TreeItem<String> root = new TreeItem<>();
		setRoot(root);
		setShowRoot(false);
		
		context.getGlobalModel().getVisualActorList().get().addListener((ListChangeListener.Change<? extends VisualActor> change)  -> {
			while (change.next()) {
				if(change.wasAdded()) {
					
				}
				if (change.wasPermutated()) {
					for (int i = change.getFrom(); i < change.getTo(); ++i) {
						//permutate
					}
				} else if (change.wasUpdated()) {
					//update item
				} else {
					for (VisualActor visualActor : change.getRemoved()) {
//						System.out.println("element deleted");
						root.getChildren().remove(visualActorToTreeItem.get(visualActor));
					}
					for (VisualActor visualActor : change.getAddedSubList()) {
//						System.out.println("element added");
						root.getChildren().add(createTreeItem(visualActor));
					}
				}
			}
		}); 
	}

	private TreeItem<String> createTreeItem(VisualActor visualActor) {
		TreeItem<String> t = new TreeItem<>(visualActor.getActor().getName());
		
		visualActor.getActor().getCurrentState();
		
		// set listener to actor state changes
		visualActor.getActor().addObserver( (obs, arg) -> {
			actualizeTreeItem(t, ((ActorState) arg).getFields());
		});
		
		visualActorToTreeItem.put(visualActor, t); // store it for future retrieval
		return t;
	}
	
	private void actualizeTreeItem(TreeItem<String> t, Map<String, Object> fields) {
		t.getChildren().clear();
		for(Map.Entry<String, Object> e : fields.entrySet()) {
			t.getChildren().add(new TreeItem<>(e.getKey() + " : " + e.getValue().toString()));
		}
	}
	
}
