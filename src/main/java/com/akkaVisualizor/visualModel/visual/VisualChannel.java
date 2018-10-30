package com.akkaVisualizor.visualModel.visual;

import com.akkaVisualizor.akkaModel.Channel;
import com.akkaVisualizor.utils.Context;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class VisualChannel {

	private final Context context;
	private final Channel channel;
	private final VisualActor source;
	private final VisualActor target;
	private BooleanProperty selectedProperty;

	public VisualChannel(Context context, Channel channel, VisualActor source, VisualActor target) {
		this.context = context;
		this.channel = channel;
		this.source = source;
		this.target = target;

		selectedProperty = new SimpleBooleanProperty(false);

	}

	public Context getContext() {
		return context;
	}

	public Channel getChannel() {
		return channel;
	}

	public VisualActor getSource() {
		return source;
	}

	public VisualActor getTarget() {
		return target;
	}
	
	public void setSelected() {
		selectedProperty.set(true);
	}

	public void setDeselected() {
		selectedProperty.set(false);
	}
	
	public BooleanProperty getSelectedProperty() {
		return selectedProperty;
	}

}
