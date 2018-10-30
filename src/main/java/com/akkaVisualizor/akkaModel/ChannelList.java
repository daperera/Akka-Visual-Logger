package com.akkaVisualizor.akkaModel;

import java.util.Iterator;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ChannelList {
	
	private final ObservableList<Channel> list;
	
	public ChannelList() {
		list = FXCollections.observableArrayList();
	}
	
	public void add(Channel channel) {
		if(!list.contains(channel)) {
//			System.out.println("channel added");
			list.add(channel);
		}
	}
	
	public void remove(Channel channel) {
//		System.out.println("channel deleted");
		list.remove(channel);
	}
	
	public void remove(Actor actor) {
//		System.out.println("channel deleted");
		Platform.runLater(() -> {
			Iterator<Channel> it = list.iterator();
			while(it.hasNext()) {
				Channel c = it.next();
				if(c.getSource().equals(actor) || c.getTarget().equals(actor)) {
					it.remove();
				}
			}
		});
	}

	public ObservableList<Channel> get() {
		return list;
	}
}

