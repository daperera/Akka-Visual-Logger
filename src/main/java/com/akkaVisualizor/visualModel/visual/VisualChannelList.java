package com.akkaVisualizor.visualModel.visual;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.akkaVisualizor.akkaModel.Channel;
import com.akkaVisualizor.utils.Context;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

public class VisualChannelList implements Iterable<VisualChannel> {
	private final ObservableList<VisualChannel> list;
	private final Map<Channel, VisualChannel> channelToVisualChannel;
	
	public VisualChannelList(Context context) {
		list = FXCollections.observableArrayList();
		channelToVisualChannel = new HashMap<>();
		context.getAkkaModel().getChannelList().get().addListener((ListChangeListener.Change<? extends Channel> change)  -> {
			while (change.next()) {
				if(change.wasAdded()) {
					for (Channel channel : change.getAddedSubList()) {
						// add visual channel
						VisualActor source = context.getGlobalModel().getVisualActorList().get(channel.getSource());
						VisualActor target = context.getGlobalModel().getVisualActorList().get(channel.getTarget());
						
						VisualChannel visualChannel = new VisualChannel(context, channel, source, target); 
						channelToVisualChannel.put(channel, visualChannel);
						Platform.runLater(() -> list.add(visualChannel));
					}
				} else if (change.wasRemoved()) {
					for (Channel channel : change.getRemoved()) {
						// delete visual channel
						VisualChannel visualChannel = channelToVisualChannel.get(channel);
						channelToVisualChannel.remove(channel);
						Platform.runLater(() -> list.remove(visualChannel));
						
						// deselect it
						context.getGlobalModel().deselectChannel(visualChannel);
					}
				}
			}
		}); 
	}
	
	public ObservableList<VisualChannel> get() {
		return list;
	}
	
	public VisualChannel get(Channel channel) {
		return channelToVisualChannel.get(channel);
	}

	@Override
	public Iterator<VisualChannel> iterator() {
		return list.iterator();
	}

}
