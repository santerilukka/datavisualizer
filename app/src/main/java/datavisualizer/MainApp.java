// DataVisualizerFX/src/main/java/datavisualizerfx/MainApp.java
package datavisualizer;

import datavisualizer.controller.AppController; // Import AppController
import datavisualizer.view.MainView; // Import MainView
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

    private AppController appController; // Keep the AppController instance
    private MainView mainView; // Add a MainView instance

    /**
     * Starts the JavaFX application.
     *
     * @param primaryStage The primary stage for this application, onto which
     * the application scene can be set.
     * @throws IOException If the FXML file for the main view cannot be loaded.
     */
    @Override
    public void start(Stage primaryStage) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/main_view.fxml"));
        javafx.scene.Parent root = loader.load();
        Scene scene = new Scene(root, 1024, 640);
        mainView = loader.getController(); // Get the MainView controller
        appController = new AppController(); // Instantiate the AppController
        mainView.setAppController(appController); // Set the AppController in MainView
        appController.setMainView(mainView); // Set the MainView in AppController
        appController.setPrimaryStage(primaryStage);

        primaryStage.setTitle("DataVisualizer");
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