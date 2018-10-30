package com.akkaVisualizor.javaFX;

import java.io.IOException;
import java.util.Optional;

import com.akkaVisualizor.AkkaVisualLogger;
import com.akkaVisualizor.javaFX.pane.MainPane;
import com.akkaVisualizor.javaFX.view.ActorView;
import com.akkaVisualizor.javaFX.view.ChannelView;
import com.akkaVisualizor.javaFX.view.MessageView;
import com.akkaVisualizor.utils.Context;
import com.akkaVisualizor.visualModel.visual.VisualChannel;
import com.akkaVisualizor.visualModel.visual.VisualMessage;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextInputDialog;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;

public class App extends Application {

	private MainPane root;
	private Scene scene;

	private static AkkaVisualLogger appEntryPoint;
	private static Context context;

	private static final String documentationUrl = "https://github.com/daperera/akkaVisualDebugger/blob/master/README.md"; 

	public static void launch(AkkaVisualLogger appEntryPoint, Context context) {
		App.appEntryPoint = appEntryPoint;
		App.context = context;

		Application.launch();
		context.getAkkaModel().terminateAkkaSystem(); // terminate akka system as well when the main window is closed
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

	/*
	public void createActor(VisualActor visualActor, String name, double x, double y) throws Exception {
		ActorView actorView = new ActorView(context, visualActor, name, x, y);
		root.getSimulationPane().getChildren().add(actorView);
	}
	 */
	public void createUnidirectionalChannel(ActorView source, ActorView target) {
		System.out.println("NOT YET IMPLEMENTED");
	}

	public void createChannel(VisualChannel visualChannel) {
		ChannelView channelView = new ChannelView(context, visualChannel);
		root.getSimulationPane().getChildren().add(channelView);
	}

	public void createMessage(VisualMessage visualMessage) {
		MessageView messageView = new MessageView(context, visualMessage);
		Platform.runLater(() -> root.getSimulationPane().getChildren().add(messageView));
	}

	public Scene getScene() {
		return scene;
	}

	public String askActorName(boolean restarted) {
		String name = null; // store result here

		// create dialog
		TextInputDialog dialog = new TextInputDialog();
		if(restarted) {
			dialog.setHeaderText("Invalid name. Please enter an other name");
		} else {
			dialog.setHeaderText("Please enter the name of the new Actor");
		}
		dialog.setTitle("Actor Name");
		dialog.setContentText("Actor name:");

		// get response value
		Optional<String> result = dialog.showAndWait();
		if(result.isPresent()) { // user entered result
			name = result.get();
		} else { // user pressed cancel or closed the window
			name = null;
		}

		return name;
	}

	public void displayHelp() {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Help");
		alert.setHeaderText(null);

		// setting main panel content 

		// constructing link toward online documentation
		Hyperlink documentationLink = new Hyperlink("here");
		documentationLink.setOnAction(e -> getHostServices().showDocument(documentationUrl));

		Text title1 = new Text("Create an actor\n");
		title1.setStyle("-fx-font-weight: bold");
		Text title2 = new Text("Create a channel between actors\n");
		title2.setStyle("-fx-font-weight: bold");
		Text title3 = new Text("Send a message between two actors\n");
		title3.setStyle("-fx-font-weight: bold");
		Text title4 = new Text("Shortcuts list\n");
		title4.setStyle("-fx-font-weight: bold");

		// setting text
		TextFlow content = new TextFlow(
				title1,
				new Text("Drag and drop from the Actor Types panel (left).\n\n"),
				title2,
				new Text("Select the source actor with <left click>, then the target actor with <ctr + left_click>.\n\n"),
				title3,
				new Text("Select the message type from the Messages panel (left). Select the source actor with <left click>, " +
						"then the target actor with <alt + left_click>.\n" +
						"\n"),
				title4,
				new Text("<suppr> : delete selected items.\n" +
						"<back_space> : enter replay mode and go to the first event of the event history.\n" +
						"<q> : enter replay mode and go backward in the event history (undo event).\n" +
						"<d> : enter replay mode and go upward in the event history (redo event).\n\n"),

				new Text("Find documentation "), documentationLink, new Text("."));
		alert.getDialogPane().contentProperty().set(content);

		// show dialog
		alert.show();
	}
}
