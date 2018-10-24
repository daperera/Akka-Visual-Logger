package com.akkaVisualizor.javaFX;

import java.io.IOException;

import com.akkaVisualizor.AkkaVisualDebugger;
import com.akkaVisualizor.Context;
import com.akkaVisualizor.javaFX.pane.MainPane;
import com.akkaVisualizor.javaFX.view.ActorView;
import com.akkaVisualizor.javaFX.view.ChannelView;
import com.akkaVisualizor.javaFX.view.MessageView;
import com.akkaVisualizor.visualModel.VisualActor;
import com.akkaVisualizor.visualModel.VisualChannel;
import com.akkaVisualizor.visualModel.VisualMessage;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {

	private MainPane root;
	private Scene scene;
	
	private static AkkaVisualDebugger appEntryPoint;
	private static Context context;
	
	public static void launch(AkkaVisualDebugger appEntryPoint, Context context) {
		App.appEntryPoint = appEntryPoint;
		App.context = context;
		Application.launch();
	}

	@Override
	public void start(Stage stage) throws IOException {
		
		// register itself to the app entryPoint
		appEntryPoint.registerJavaFXApplication(this);
		
		// javafx init
		root = new MainPane(context);
		scene = new Scene(root);
		stage.setTitle(context.getConfiguration().getTitle());
		stage.setScene(scene);
		stage.show();
		
		// set keyboard listener
		scene.setOnKeyPressed(e -> context.getGlobalMouseController().onKeyPressed(e));
		scene.setOnKeyReleased(e -> context.getGlobalMouseController().onKeyReleased(e));
		
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
	
	public Scene getScene() {
		return scene;
	}
}
