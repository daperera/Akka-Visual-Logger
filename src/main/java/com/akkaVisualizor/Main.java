package com.akkaVisualizor;

import akka.actor.ActorSystem;

public class Main {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		
		AkkaVisualDebugger debug = AkkaVisualDebugger.create(system);
		
		system.terminate();
	}
}
