package com.eron.javafx;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Border;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.stage.Stage;

public class SimpleTestJavaFX extends Application { 
	
	private static final Logger log = LoggerFactory.getLogger(SimpleTestJavaFX.class);
	
	static final double BORDER_RADIUS = 4;

	@Override
	public void start(Stage primaryStage) throws Exception {
		log.info("application start launch primary stage !");
		primaryStage.setScene(new Scene(createDefault()));
		
		primaryStage.show();
	}
	
	static void clipChildren(Region region, double arc) {

	    final Rectangle outputClip = new Rectangle();
	    outputClip.setArcWidth(arc);
	    outputClip.setArcHeight(arc);
	    region.setClip(outputClip);

	    region.layoutBoundsProperty().addListener((ov, oldValue, newValue) -> {
	        outputClip.setWidth(newValue.getWidth());
	        outputClip.setHeight(newValue.getHeight());
	    });
	}

	static Region createClipped() {
	    final Pane pane = new Pane(createShape());
	    pane.setPrefSize(100, 100);

	    // clipped children still overwrite Border!
	    clipChildren(pane, 3 * BORDER_RADIUS);

	    return pane;
	}
	
	static Shape createShape() {
	    final Ellipse shape = new Ellipse(50, 50);
	    shape.setCenterX(80);
	    shape.setCenterY(80);
	    shape.setFill(Color.LIGHTCORAL);
	    shape.setStroke(Color.LIGHTCORAL);
	    return shape;
	}
	
	static Region createDefault() {
	    final Pane pane = new Pane(createShape());
	    pane.setPrefSize(100, 100);
	    return pane;
	}


	
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	public static class Launcher {
		public static void main(String[] args) {
			SimpleTestJavaFX.main(args);
		}
	}

}



