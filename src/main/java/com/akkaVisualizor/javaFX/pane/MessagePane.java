package com.akkaVisualizor.javaFX.pane;

import java.util.ArrayList;
import java.util.List;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.javaFX.view.MessageTypeView;
import com.akkaVisualizor.visualModel.visual.VisualMessageType;
import com.akkaVisualizor.visualModel.visual.VisualMessageTypeList;

import javafx.beans.value.ChangeListener;
import javafx.scene.layout.AnchorPane;

public class MessagePane extends AnchorPane {

	private List<MessageTypeView> messageTypeViewList;
	private Context context;

	public MessagePane(Context context) {
		this.context = context;
		messageTypeViewList = new ArrayList<>();
		update();
		
		// update when the visualMessageTypeList change
		context.getModel().getVisualMessageTypeList().getChangeProperty().addListener((ChangeListener<Boolean>) (o, oldVal,  newVal) -> { 
			update();
		});
	}

	public void update() {
		VisualMessageTypeList messageTypeList = context.getModel().getVisualMessageTypeList();
		for(VisualMessageType type : messageTypeList) {
			// detect if type was added to messageTypeList since last update
			boolean contains = false;
			for(MessageTypeView view : messageTypeViewList) {
				if(view.getModel().equals(type)) {
					contains = true;
					break;
				}
			}
			if(!contains) {
				messageTypeViewList.add(new MessageTypeView(type));
			}
		}
	}
}
