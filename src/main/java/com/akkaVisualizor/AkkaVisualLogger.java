package com.akkaVisualizor;

import java.util.function.Supplier;

import com.akkaVisualizor.akkaModel.ActorType.SerializableSupplier;
import com.akkaVisualizor.akkaModel.AkkaModel;
import com.akkaVisualizor.akkaModel.Configuration;
import com.akkaVisualizor.javaFX.App;
import com.akkaVisualizor.visualModel.GlobalModel;
import com.akkaVisualizor.visualModel.GlobalController;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.Actor;

public class AkkaVisualLogger {

	private AkkaModel akkaModel;
	private Context	context;

	private boolean start;

	private static AkkaVisualLogger instance;

	public static AkkaVisualLogger getInstance() {
		AkkaVisualLogger res = null;
		try {
			if(instance !=null) {
				res = instance;
			} else {
				throw new Exception("AkkaVisualLogger has not started yet");
			}
		} catch( Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	public static synchronized AkkaVisualLogger create(ActorSystem system) {
		if(instance ==null) {
			instance = new AkkaVisualLogger(system);
		}
		return instance;
	}


	private AkkaVisualLogger(ActorSystem system) {
		// set still starting
		start = false;

		// load context
		context = new Context();

		// load Akka simulator
		akkaModel = new AkkaModel(context, system);

		// load javaFX

		new Thread(() -> App.launch(AkkaVisualLogger.this, context)).start(); // launch asynchronously
		waitStart(); // give time to App to properly launch
	}

	public void registerJavaFXApplication(App app) {
		// load config
		Configuration conf = Configuration.load();

		// load mouse controller
		GlobalController mouseController = new GlobalController(context);

		// load global model
		GlobalModel globalModel = new GlobalModel(context);

		// actualize context
		context.set(conf, akkaModel, mouseController, globalModel, app);

		// consider app done starting
		start = true;
	}

	public void logActorCreated(akka.actor.Actor actor, String name) {
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

	public void logActorType(String typeClass, SerializableSupplier<Props> typeConstructor) {
		akkaModel.logActorType(typeClass, typeConstructor);
	}

	public void logMessageType(String name, Supplier<Object> messageConstructor) {
		akkaModel.logMessageType(name, messageConstructor);
	}

	private void waitStart() {
		while(!start) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}