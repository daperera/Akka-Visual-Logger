package com.akkaVisualizor.akkaModel;

import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeType;

public class Configuration {
	
	private double defaultHeight = 300;
	private double defaultWidth = 400;
	private String title = "akka debugger";
	private double actorRadius = 30;
	
	private double actorOpacity = 0.74;
	private Color actorFill = Color.valueOf("7d97ae");
	private Color actorStroke = Color.ALICEBLUE;
	private StrokeType actorStrokeType = StrokeType.INSIDE;
	private double strokeWidth = 0.5;
	
	/*
	 setFill(Color.valueOf("7d97ae"));
        setOpacity(0.74);
        setStroke(Color.BLACK);
        setStrokeType(StrokeType.INSIDE);
        setStrokeWidth(0.5);
	 */
	
	public double getActorOpacity() {
		return actorOpacity;
	}
	public Color getActorFill() {
		return actorFill;
	}
	public Color getActorStroke() {
		return actorStroke;
	}
	public StrokeType getActorStrokeType() {
		return actorStrokeType;
	}
	public double getStrokeWidth() {
		return strokeWidth;
	}
	public double getDefaultHeight() {
		return defaultHeight;
	}
	public double getDefaultWidth() {
		return defaultWidth;
	}
	public double getActorRadius() {
		return actorRadius;
	}
	public static Configuration load() {
		Configuration conf = new Configuration();
		conf.parseConfigFile();
		return conf;
	}

	private void parseConfigFile() {
		// parse config file
	}
	
	public String getTitle() {
		return title;
	}
	
}
