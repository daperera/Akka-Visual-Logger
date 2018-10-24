package com.akkaVisualizor.javaFX;

import java.io.IOException;

import com.akkaVisualizor.Context;
import com.akkaVisualizor.akkaModel.AkkaModel;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.javaFX.pane.MainPane;
import com.akkaVisualizor.javaFX.view.ActorView;
import com.akkaVisualizor.javaFX.view.ChannelView;
import com.akkaVisualizor.javaFX.view.MessageView;
import com.akkaVisualizor.visualModel.GlobalModel;
import com.akkaVisualizor.visualModel.GlobalMouseController;
import com.akkaVisualizor.visualModel.VisualActor;
import com.akkaVisualizor.visualModel.VisualChannel;
import com.akkaVisualizor.visualModel.VisualMessage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	private Configuration conf;
	private AkkaModel akkaModel;
	private MainPane root;
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
		root = new MainPane(context);
		Scene scene = new Scene(root);
		stage.setTitle(conf.getTitle());
		stage.setScene(scene);
		stage.show();
		
	}

	public void createActor(VisualActor visualActor, String name, double x, double y) throws Exception {
			ActorView actorView = new ActorView(context, visualActor, name, x, y);
			root.getSimulationPane().getChildren().add(actorView);
	}

	public void createUnidirectionalChannel(ActorView source, ActorView target) {
		System.out.println("NOT YET IMPLEMENTED");
	}

	public void createBidirectionalChannel(VisualChannel visualChannel) {
		ChannelView channelView = new ChannelView(context, visualChannel);
		root.getSimulationPane().getChildren().add(channelView);
	}

	public void createMessage(VisualMessage visualMessage) {
		MessageView messageView = new MessageView(context, visualMessage);
		root.getSimulationPane().getChildren().add(messageView);
	}
}
