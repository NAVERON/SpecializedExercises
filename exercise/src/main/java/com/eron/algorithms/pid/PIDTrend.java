package com.eron.algorithms.pid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class PIDTrend extends Application {


    private static final Logger log = LoggerFactory.getLogger(PIDTrend.class);
    private String NAME = "PID Plot Controller";
    
    @Override 
    public void start(Stage primaryStage) throws Exception {
        Pane root = this.createContent();
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.setTitle(this.NAME);
        primaryStage.show();
    }

    private Pane createContent() {
        // 三个参数 一个chart 显示 
        BorderPane root = new BorderPane();
        // 绘制PID 图形
        PIDEquationPlot plotter = new PIDEquationPlot();
        
        // 控制pid参数设置 
        Slider kpSlider = new Slider(0, 3, 0); 
        Text kpMark = new Text();
        kpSlider.setMajorTickUnit(0.5f); // 标尺刻度 
        kpSlider.setShowTickLabels(true);  // 是否显示刻度 
        kpSlider.setBlockIncrement(0.5f);  // 每次变化的值 
        kpSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            log.info("设置参数 kp --> {}", newValue.doubleValue());
            plotter.setKpParam(newValue.doubleValue());
            kpMark.setText(newValue.toString());
        });
        Slider kiSlider = new Slider(0, 3, 0);
        Text kiMark = new Text();
        kiSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            log.info("设置参数 ki --> {}", newValue.doubleValue());
            plotter.setKiParam(newValue.doubleValue());
            kiMark.setText(newValue.toString());
        });
        Slider kdSlider = new Slider(0, 3, 0);
        Text kdMark = new Text();
        kdSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            log.info("设置参数 kd --> {}", newValue.doubleValue());
            plotter.setKdParam(newValue.doubleValue());
            kdMark.setText(newValue.toString());
        });
        // 设置目标值 
        TextField targetInput = new TextField();
        Text targetMark = new Text();
        targetInput.setPromptText("Please input <target> value");
        targetInput.setOnAction(event -> {
            log.info("设置目标值 --> {}", targetInput.getText());
            String targetVal = targetInput.getText();
            plotter.setTarget(Double.valueOf(targetVal));
            targetMark.setText(targetVal);
            
            targetInput.clear();
        });
        
        VBox params = new VBox(20);
        // params.setPadding(Insets.EMPTY);
        params.setPadding(new Insets(20));
        params.getChildren().addAll(
                kpSlider, kpMark, 
                kiSlider, kiMark, 
                kdSlider, kdMark, 
                targetInput, targetMark);
        
        root.setCenter(plotter);
        root.setRight(params);
        
        return root;
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static class Launcher {
        public static void main(String[] args) {
            PIDTrend.main(args);
        }
    }
    
}













