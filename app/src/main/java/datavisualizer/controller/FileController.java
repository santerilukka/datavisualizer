package datavisualizer.controller;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.parser.CSVParser;
import datavisualizer.model.parser.DataParser;
import datavisualizer.model.parser.JSONParser;
import datavisualizer.util.FileUtils;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

/**
 * Controller responsible for handling file loading operations.
 */
public class FileController {

    /**
     * Opens a file chooser dialog and loads data from the selected file.
     * Determines the file type and uses the appropriate parser.
     * The default filter shows both CSV and JSON files.
     *
     * @param primaryStage The primary stage used to show the file chooser dialog.
     * @return A DataSet containing the loaded data, or null if loading fails or is cancelled.
     */
    public DataSet loadDataFile(Stage primaryStage) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Data File");

        // Create a filter that shows both CSV and JSON files
        FileChooser.ExtensionFilter combinedFilter = new FileChooser.ExtensionFilter("Data Files (*.csv, *.json)", "*.csv", "*.json");
        // Add the combined filter first to make it the default
        fileChooser.getExtensionFilters().add(combinedFilter);

        // Optionally, keep individual filters
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV Files (*.csv)", "*.csv"),
                new FileChooser.ExtensionFilter("JSON Files (*.json)", "*.json"),
                new FileChooser.ExtensionFilter("All Files (*.*)", "*.*")
        );

        // Set the combined filter as the initially selected one
        fileChooser.setSelectedExtensionFilter(combinedFilter);

        File selectedFile = fileChooser.showOpenDialog(primaryStage);

        if (selectedFile != null) {
            primaryStage.setTitle("DataVisualizer - " + selectedFile.getName());
            String fileExtension = FileUtils.getFileExtension(selectedFile);
            DataParser parser = null;

            switch (fileExtension) {
                case "csv":
                    parser = new CSVParser();
                    break;
                case "json":
                    parser = new JSONParser();
                    break;
                default:
                    System.err.println("Unsupported or unrecognized file type: " + fileExtension);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Unsupported File Type");
                    alert.setHeaderText(null);
                    alert.setContentText("The selected file type '." + fileExtension + "' is not supported. Please select a CSV or JSON file.");
                    alert.showAndWait();
                return null; // Indicate failure
            }

            try {
                // Parse the file using the selected parser
                return parser.parse(selectedFile);
            } catch (IOException e) {
                // Handle file reading/parsing error
                e.printStackTrace();
                System.err.println("Error loading file: " + e.getMessage());
                // Optionally, display an error message to the user via an alert
                return null; // Indicates failure
            }
        } else {
            // User cancelled the file chooser
            return null;
        }
    }
}