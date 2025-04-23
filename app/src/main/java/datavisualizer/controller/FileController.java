package datavisualizer.controller;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.parser.CSVParser;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;

public class FileController {

    public DataSet loadCSVFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .csv file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
    
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            CSVParser parser = new CSVParser();
            try {
                return parser.parse(selectedFile);
            } catch (IOException e) {
                e.printStackTrace();
                // Optionally, display an error message to the user
            }
        }
        return null;
    }
}
