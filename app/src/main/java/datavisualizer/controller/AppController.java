// DataVisualizerFX/src/main/java/datavisualizerfx/controller/AppController.java
package datavisualizer.controller;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.parser.CSVParser;
import datavisualizer.model.parser.DataParser;
import datavisualizer.model.parser.JSONParser;
import datavisualizer.view.MainView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Controller for the main application window.
 * Handles application-level actions such as opening files.
 */
public class AppController {

    private MainView mainView;
    private DataSet dataSet;
    private final CommandManager commandManager = new CommandManager();
    private Stage primaryStage;

    /**
     * Sets the primary stage of the application.
     *
     * @param primaryStage The primary stage.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Handles the action of opening a data file.
     */
    public void openFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data File");
        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            loadFile(selectedFile);
        }
    }

    /**
     * Loads data from the specified file.
     * Determines the file type and uses the appropriate parser.
     *
     * @param file The file to load.
     */
    private void loadFile(File file) {
        String fileName = file.getName().toLowerCase();
        DataParser parser = null;

        if (fileName.endsWith(".csv")) {
            parser = new CSVParser();
        } else if (fileName.endsWith(".json")) {
            parser = new JSONParser();
        }

        if (parser != null) {
            try {
                dataSet = parser.parse(file);
                // Notify the view to update with the new dataset
                if (mainView != null) {
                    mainView.displayDataSet(dataSet);
                }
            } catch (IOException e) {
                // Handle file reading error
                e.printStackTrace();
                System.err.println("Error loading file: " + e.getMessage());
                // Optionally, display an error message to the user
            }
        } else {
            // Handle unsupported file type
            System.err.println("Unsupported file type: " + fileName);
            // Optionally, display an error message to the user
        }
    }

    /**
     * Sets the main view for this controller.
     *
     * @param mainView The main view instance.
     */
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    /**
     * Gets the command manager instance.
     *
     * @return The command manager.
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Gets the current dataset.
     *
     * @return The current dataset.
     */
    public DataSet getDataSet() {
        return dataSet;
    }

    /**
     * Gets the main view instance.
     * 
     * @return The main view.
     */
    public MainView getMainView() {
        return mainView;
    }
}