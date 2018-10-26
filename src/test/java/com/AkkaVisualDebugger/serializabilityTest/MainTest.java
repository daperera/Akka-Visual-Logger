package com.AkkaVisualDebugger.serializabilityTest;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

import com.akkaVisualizor.akkaModel.ActorType;

import akka.actor.ActorSystem;

public class MainTest {
	public static void main(String[] args) {
		ActorSystem system = ActorSystem.create();
		
		ActorType type = new ActorType("class", DefaultActor::props, system);
		
		
		serialize(type);
		
		
		
		System.out.println("end");
		system.terminate();
	}
	
	public static void serialize(Object o) {
		try {
			ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("test")); //Saving of object in a file 
	        out.writeObject(o); // Method for serialization of object 
	        out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
