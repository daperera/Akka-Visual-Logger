package com.akkaVisualizor;

import java.util.function.Supplier;

import com.akkaVisualizor.akkaModel.AkkaModel;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.javaFX.App;
import com.akkaVisualizor.visualModel.GlobalModel;
import com.akkaVisualizor.visualModel.GlobalMouseController;

import akka.actor.ActorRef;
import akka.actor.Props;

public class AkkaVisualDebugger {

	private AkkaModel akkaModel;

	public AkkaVisualDebugger create() {
		return new AkkaVisualDebugger();
	}

	private AkkaVisualDebugger() {
		// load context
		Context context = new Context();

		// load config
		Configuration conf = Configuration.load();

		// load Akka simulator 
		akkaModel = new AkkaModel(context);

		// load mouse controller
		GlobalMouseController mouseController = new GlobalMouseController(context);

		// load global model
		GlobalModel model = new GlobalModel(context);
		
		App.launch(this, context);
	}

	public void registerJavaFXApplication(App app) {


		// actualize context
		context.set(conf, akkaModel, mouseController, model, app);
	}

	public void logActorCreated(ActorRef actor, String name) {

	}	

	public void logActorDeleted(ActorRef actor) {

	}

	public void logMessageSent(Object m, ActorRef source, ActorRef target) {

	}

	public void logMessageReceived(Object m, ActorRef source, ActorRef target) {

	}

	public void logActorType(Class<ActorRef> typeClass, Supplier<Props> typeConstructor) {

	}

	public void logMessageType(String name, Supplier<Object> messageConstructor) {

	}


}