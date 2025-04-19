package datavisualizer.controller;

import datavisualizer.model.ChartType;
import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.command.ChangeChartTypeCommand;
import datavisualizer.model.command.Command;
import datavisualizer.model.chart.ChartFactory;
import datavisualizer.model.parser.CSVParser;
import javafx.stage.FileChooser;

import java.io.File;

public class AppController {
    private DataSet currentDataSet;
    private CommandManager commandManager;
    private FileChooser fileChooser;
    private ChartType currentChartType = ChartType.BAR;
    
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
            // Notify observers that data has changed
        }
    }
    
    public void setChartType(ChartType type) {
        Command command = new ChangeChartTypeCommand(this, currentChartType, type);
        commandManager.executeCommand(command);
        currentChartType = type;
    }
    
    public void columnSelectionChanged(String columnName) {
        // Handle column selection change
    }
    
    public void undo() {
        commandManager.undo();
    }
    
    public void redo() {
        commandManager.redo();
    }
    
    // Other controller methods as needed
}