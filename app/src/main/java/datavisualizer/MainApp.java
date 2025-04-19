package datavisualizer;

import datavisualizer.controller.FileController;
import datavisualizer.model.dataset.DataSet;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DataVisualizerFX");

        FileController fileController = new FileController();
        DataSet dataSet = fileController.loadCSVFile();

        if (dataSet != null) {
            dataSet.printPreview();
        }

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
