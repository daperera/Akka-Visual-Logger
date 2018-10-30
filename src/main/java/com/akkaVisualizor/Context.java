package com.akkaVisualizor;

import com.akkaVisualizor.akkaModel.AkkaModel;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.javaFX.App;
import com.akkaVisualizor.visualModel.GlobalModel;
import com.akkaVisualizor.visualModel.GlobalController;

public class Context {
	private Configuration configuration;
	private AkkaModel akkaModel;
	private GlobalController globalMouseController; 
	private GlobalModel globalModel;
	private App app;
	
	public void set(Configuration configuration, AkkaModel akkaModel, GlobalController globalMouseController,
			GlobalModel model, App visualizor) {
		this.configuration = configuration;
		this.akkaModel = akkaModel;
		this.globalMouseController = globalMouseController;
		this.globalModel = model;
		this.app = visualizor;
	}
	public GlobalModel getGlobalModel() {
		return globalModel;
	}
	public void setGlobalModel(GlobalModel globalModel) {
		this.globalModel = globalModel;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	public void setAkkaModel(AkkaModel akkaModel) {
		this.akkaModel = akkaModel;
	}
	public void setGlobalMouseController(GlobalController globalMouseController) {
		this.globalMouseController = globalMouseController;
	}
	public void setApp(App app) {
		this.app = app;
	}
	public Configuration getConfiguration() {
		return configuration;
	}
	public AkkaModel getAkkaModel() {
		return akkaModel;
	}
	public App getApp() {
		return app;
	}
	public GlobalController getGlobalMouseController() {
		return globalMouseController;
	}
}
