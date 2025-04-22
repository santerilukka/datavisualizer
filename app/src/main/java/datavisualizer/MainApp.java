// DataVisualizerFX/src/main/java/datavisualizerfx/MainApp.java
package datavisualizer;

import datavisualizer.controller.AppController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Main application class for the DataVisualizerFX application.
 * Initializes the primary stage and loads the main view.
 */
public class MainApp extends Application {

    private AppController appController;

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage The primary stage for this application, onto which
     * the application scene can be set.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/main_view.fxml"));
        Scene scene = new Scene(loader.load());
        appController = loader.getController();
        appController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("DataVisualizerFX");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * Main entry point for the application.
     *
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        launch(args);
    }
}