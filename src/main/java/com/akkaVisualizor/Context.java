package com.akkaVisualizor;

import com.akkaVisualizor.akkaModel.AkkaModel;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.javaFX.App;
import com.akkaVisualizor.visualModel.GlobalModel;
import com.akkaVisualizor.visualModel.GlobalMouseController;

public class Context {
	private Configuration configuration;
	private AkkaModel akkaModel;
	private GlobalMouseController globalMouseController; 
	private GlobalModel globalModel;
	private App visualizor;
	
	public void set(Configuration configuration, AkkaModel akkaModel, GlobalMouseController globalMouseController,
			GlobalModel model, App visualizor) {
		this.configuration = configuration;
		this.akkaModel = akkaModel;
		this.globalMouseController = globalMouseController;
		this.globalModel = model;
		this.visualizor = visualizor;
	}
	public GlobalModel getModel() {
		return globalModel;
	}
	public Configuration getConfiguration() {
		return configuration;
	}
	public AkkaModel getAkkaModel() {
		return akkaModel;
	}
	public App getVisualizor() {
		return visualizor;
	}
	public GlobalMouseController getGlobalMouseController() {
		return globalMouseController;
	}
}
