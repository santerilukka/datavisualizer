package datavisualizer.controller;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.chart.ChartType;
import datavisualizer.view.MainView;
import datavisualizer.view.ColumnSelectionPanel;
import datavisualizer.view.ErrorDisplayView;

import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;

/**
 * Controller for the main application window.
 * Handles application-level actions
 */
public class AppController {

    private MainView mainView;
    private DataSet dataSet;
    private Stage primaryStage;

    private final CommandManager commandManager = new CommandManager();
    private final FileController fileController = new FileController();

    /**
     * Sets the primary stage of the application.
     *
     * @param primaryStage The primary stage.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Handles the action of opening a data file using FileController.
     */
    public void openFile() {
        DataSet loadedDataSet = fileController.loadDataFile(primaryStage);

        if (loadedDataSet != null) {
            this.dataSet = loadedDataSet;
            // Notify the view to update with the new dataset
            if (mainView != null) {
                mainView.displayDataSet(this.dataSet);
                // Reset command history when new data is loaded
                //commandManager.clearHistory();
            }
        } else {
            // Handle case where loading failed or was cancelled (optional: show message)
            System.err.println("Failed to load data file or operation cancelled.");
        }
    }

    /**
     * Closes the currently open file, clearing the dataset and resetting the view.
     */
    public void closeCurrentFile() {
        this.dataSet = null; // Clear the dataset

        // Notify the view to reset to the start screen state
        if (mainView != null) {
            mainView.displayDataSet(null);
        }

        // Clear the undo/redo history
        commandManager.clearHistory();

        // Reset the window title
        if (primaryStage != null) {
            primaryStage.setTitle("DataVisualizer");
        }
        System.out.println("File closed."); // Optional logging
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

    /**
     * Requests an update to the chart based on selections from the panel.
     * Performs validation before updating the view.
     *
     * @param type The selected chart type.
     * @param xCol The selected X-axis column.
     * @param yCol The selected Y-axis column.
     */
    public void requestChartUpdate(ChartType type, String xCol, String yCol) {
        if (mainView == null || mainView.getChartView() == null || mainView.getColumnSelectionPanel() == null) {
            System.err.println("Cannot update chart: View components not ready.");
            return;
        }

        ColumnSelectionPanel panel = mainView.getColumnSelectionPanel();
        ErrorDisplayView errorDisplay = panel.getErrorDisplayView();

        if (errorDisplay == null) {
             System.err.println("Cannot update chart: ErrorDisplayView not initialized.");
             return;
        }
        errorDisplay.clearErrors(); // Clear previous errors

        // Check if data is loaded
        if (dataSet == null) {
            System.err.println("Cannot update chart: No data loaded.");
            mainView.getChartView().clearChart();
            return;
        }

        // Validate selections
        boolean valid = true;
        if (type == null) {
            System.err.println("Chart type is null.");
            valid = false;
        }
        if (xCol == null) {
            errorDisplay.showXAxisError("Please select a column for the X-Axis."); // Use ErrorDisplayView
            valid = false;
        }
        if (yCol == null) {
            errorDisplay.showYAxisError("Please select a column for the Y-Axis."); // Use ErrorDisplayView
            valid = false;
        }

        // Only check for equality if both are selected
        if (xCol != null && yCol != null && xCol.equals(yCol)) {
            errorDisplay.showYAxisError("X and Y axes cannot be the same."); // Use ErrorDisplayView
            valid = false;
        }

        if (!valid) {
            mainView.getChartView().clearChart();
            return;
        }

        // Validation passed, update the chart view
        mainView.getChartView().updateChart(type, xCol, List.of(yCol));

        // Reflect state back to panel
        panel.reflectChartState(type, xCol, yCol);
    }

    /**
     * Requests swapping of the X and Y axes selected in the panel.
     */
    public void requestAxisSwap() {
         if (mainView == null || mainView.getColumnSelectionPanel() == null) {
            System.err.println("Cannot swap axes: View components not ready.");
            return;
        }
        ColumnSelectionPanel panel = mainView.getColumnSelectionPanel();
        ErrorDisplayView errorDisplay = panel.getErrorDisplayView(); // Get the error display

        if (errorDisplay == null) {
             System.err.println("Cannot swap axes: ErrorDisplayView not initialized.");
             return;
        }
        errorDisplay.clearErrors(); // Clear previous errors using ErrorDisplayView

        String currentX = panel.getSelectedXAxisColumn();
        String currentY = panel.getSelectedYAxisColumn();
        ChartType currentType = panel.getSelectedChartType();

        // Validate if both are selected
        boolean valid = true;
        if (currentX == null) {
            errorDisplay.showXAxisError("Select both X and Y axes to swap."); // Use ErrorDisplayView
            valid = false;
        }
        if (currentY == null) {
            errorDisplay.showYAxisError("Select both X and Y axes to swap."); // Use ErrorDisplayView
            valid = false;
        }

        if (!valid) {
            return;
        }

        // Check if they are different before attempting swap logic
        if (currentX.equals(currentY)) {
             errorDisplay.showYAxisError("X and Y axes cannot be the same."); // Use ErrorDisplayView
             return;
        }

        // Request update with swapped axes
        requestChartUpdate(currentType, currentY, currentX);
    }
}