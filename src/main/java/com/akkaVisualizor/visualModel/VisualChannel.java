package com.akkaVisualizor.visualModel;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.Channel;

public class VisualChannel {
	
	private final Context context;
	private final Channel channel;
	private final VisualActor source;
	private final VisualActor target;
	public VisualChannel(Context context, Channel channel, VisualActor source, VisualActor target) {
		this.context = context;
		this.channel = channel;
		this.source = source;
		this.target = target;
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

}
