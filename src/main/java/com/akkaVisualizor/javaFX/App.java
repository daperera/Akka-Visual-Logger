package com.akkaVisualizor.javaFX;

import java.io.IOException;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.AkkaModel;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.visualModel.GlobalModel;
import com.akkaVisualizor.visualModel.GlobalMouseController;
import com.akkaVisualizor.visualModel.VisualActor;
import com.akkaVisualizor.visualModel.VisualChannel;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class App extends Application {

	private Configuration conf;
	private AkkaModel akkaModel;
	private Pane root;
	private Context context;
	
	public static void launch()
	{
		Application.launch();
	}

	@Override
	public void start(Stage stage) throws IOException
	{
		//
		context = new Context();
		
		// load config
		conf = Configuration.load();

		// load Akka simulator 
		akkaModel = new AkkaModel(context);
		
		// load mouse controller
		GlobalMouseController mouseController = new GlobalMouseController(context);
		
		// load global model
		GlobalModel model = new GlobalModel(context);
		
		// actualize context
		context.set(conf, akkaModel, mouseController, model, this);
		
		// javafx init
		root = new MainView(context);
		Scene scene = new Scene(root, conf.getDefaultHeight(), conf.getDefaultWidth());
		stage.setTitle(conf.getTitle());
		stage.setScene(scene);
		stage.show();
		
	}

	public void createActor(VisualActor visualActor, String name, double x, double y) throws Exception {
			ActorView actorView = new ActorView(context, visualActor, name, x, y);
			root.getChildren().add(actorView);
	}

	public void createUnidirectionalChannel(ActorView source, ActorView target) {
		System.out.println("NOT YET IMPLEMENTED");
	}

	public void createBidirectionalChannel(VisualChannel visualChannel) {
		ChannelView channelView = new ChannelView(context, visualChannel);
		root.getChildren().add(channelView);
	}
}
