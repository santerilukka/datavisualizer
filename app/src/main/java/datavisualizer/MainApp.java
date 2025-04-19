package datavisualizer;

import datavisualizer.controller.AppController;
import datavisualizer.view.MainView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    private AppController appController;
    private MainView mainView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DataVisualizerFX");
        
        // Initialize MVC components
        appController = new AppController();
        mainView = new MainView(appController);
        
        Scene scene = new Scene(mainView.getRoot(), 800, 600);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}