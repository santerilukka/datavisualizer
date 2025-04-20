package datavisualizer.controller;

import datavisualizer.model.ChartType;
import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.command.ChangeChartTypeCommand;
import datavisualizer.model.command.Command;
import datavisualizer.model.chart.ChartFactory;
import datavisualizer.model.chart.Chart;
import datavisualizer.controller.CommandManager;
import datavisualizer.model.command.Command;
import datavisualizer.view.MainView;
import datavisualizer.model.parser.CSVParser;
import javafx.stage.FileChooser;

import java.io.File;

import java.util.List;

public class AppController {
    private DataSet currentDataSet;
    private CommandManager commandManager;
    private FileChooser fileChooser;
    private ChartType currentChartType = ChartType.BAR;
    private MainView mainView;

    public AppController() {
        commandManager = new CommandManager();
        fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
    }

    public void loadCSVFile() {
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            CSVParser parser = new CSVParser();
            currentDataSet = parser.parse(file);

            if (currentDataSet != null) {
                updateChartView();
            }
        }
    }

    public void setChartType(ChartType type) {
        this.currentChartType = type;
        updateChartView();
    }

    private void updateChartView() {
        if (currentDataSet != null) {
            Chart chart = ChartFactory.createChart(currentChartType, currentDataSet);
            mainView.getChartView().displayChart(chart.render());
        }
    }

    public void undo() {
    if (commandManager != null) {
        commandManager.undo();
    }
    }

    public void redo() {
        if (commandManager != null) {
            commandManager.redo();
        }
    }

    public void columnSelectionChanged(String columnName) {
    // Handle column selection logic here
    System.out.println("Column selected: " + columnName);
}
    
    // Other controller methods as needed
}