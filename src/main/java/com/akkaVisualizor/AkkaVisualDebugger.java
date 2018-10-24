package com.akkaVisualizor;

import java.util.function.Supplier;

import com.akkaVisualizor.akkaModel.AkkaModel;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.javaFX.App;
import com.akkaVisualizor.visualModel.GlobalModel;
import com.akkaVisualizor.visualModel.GlobalMouseController;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

public class AkkaVisualDebugger {

	private AkkaModel akkaModel;
	private Context	context;

	public static AkkaVisualDebugger create(ActorSystem system) {
		return new AkkaVisualDebugger(system);
	}

	private AkkaVisualDebugger(ActorSystem system) {
		// load context
		context = new Context();
				
		// load Akka simulator
		akkaModel = new AkkaModel(context, system);
		
		// load javaFX
		App.launch(this, context);
	}

	public void registerJavaFXApplication(App app) {
		// load config
		Configuration conf = Configuration.load();

		// load mouse controller
		GlobalMouseController mouseController = new GlobalMouseController(context);

		// load global model
		GlobalModel globalModel = new GlobalModel(context);

		// actualize context
		context.set(conf, akkaModel, mouseController, globalModel, app);
	}

	public void logActorCreated(ActorRef actor, String name) {
		akkaModel.logActorCreated(actor, name);
	}	

	public void logActorDeleted(ActorRef actor) {
		akkaModel.logActorDeleted(actor);
	}

	public void logMessageSent(Object m, ActorRef source, ActorRef target) {
		akkaModel.logMessageSent(m, source, target);
	}

	public void logMessageReceived(Object m, ActorRef source, ActorRef target) {
		akkaModel.logMessageReceived(m, source, target);
	}

	public void logActorType(Class<? extends ActorRef> typeClass, Supplier<Props> typeConstructor) {
		akkaModel.logActorType(typeClass, typeConstructor);
	}

	public void logMessageType(String name, Supplier<Object> messageConstructor) {
		akkaModel.logMessageType(name, messageConstructor);
	}


}