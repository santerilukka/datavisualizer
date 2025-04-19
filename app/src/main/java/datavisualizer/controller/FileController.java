package datavisualizer.controller;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.parser.CSVParser;
import javafx.stage.FileChooser;

import java.io.File;

public class FileController {

    public DataSet loadCSVFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open .csv file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));

        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            CSVParser parser = new CSVParser();
            return parser.parse(selectedFile);
        }

        return null;
    }
}
