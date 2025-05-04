package datavisualizer.controller;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.chart.ChartType;
import datavisualizer.model.ChartStateModel;
import datavisualizer.model.ChartStateObserver;
import datavisualizer.view.MainView;
import datavisualizer.view.ColumnSelectionPanel;
import datavisualizer.view.ErrorDisplayView;
import datavisualizer.model.command.Command;
import datavisualizer.model.command.UpdateChartStateCommand;

import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.Collections;

/**
 * Controller for the main application window.
 * Handles application-level actions
 */
public class AppController implements ChartStateObserver {
    private MainView mainView;
    // private DataSet dataSet; // Removed DataSet field from Controller
    private Stage primaryStage;

    private final CommandManager commandManager = new CommandManager();
    private final FileController fileController = new FileController();
    private final ChartStateModel chartStateModel = new ChartStateModel(); // Model holds state and data

    public AppController() {
        chartStateModel.addObserver(this); // Register as observer
    }

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
            // this.dataSet = loadedDataSet; // Removed setting local field
            chartStateModel.setDataSet(loadedDataSet); // Set DataSet in the model
            // Reset chart state using the model (which also clears previous data ref)
            chartStateModel.resetState(); // Reset config, keep new data
            chartStateModel.setDataSet(loadedDataSet); // Re-set DataSet after resetState clears it
            commandManager.clearHistory(); // Clear undo/redo

            if (mainView != null) {
                mainView.displayDataSet(loadedDataSet); // Pass loaded data to view for initial setup
                // Set initial state in panel after populating
                ColumnSelectionPanel panel = mainView.getColumnSelectionPanel();
                if (panel != null && !loadedDataSet.getColumnNames().isEmpty()) {
                    String defaultX = loadedDataSet.getColumnNames().get(0);
                    String defaultY = loadedDataSet.getColumnNames().size() > 1 ? loadedDataSet.getColumnNames().get(1) : defaultX;
                    // Update the model with initial defaults
                    chartStateModel.updateState(ChartType.BAR, defaultX, defaultY);
                    // Reflect the model's state in the panel
                    panel.reflectChartState(chartStateModel.getChartType(), chartStateModel.getXColumn(), defaultY);
                    // triggerChartViewUpdate(); // Triggered by model updateState via observer
                }
            }
        } else {
            System.err.println("Failed to load data file or operation cancelled.");
            // Ensure model's dataset is null if loading failed
            if (chartStateModel.getDataSet() != null) {
                 chartStateModel.setDataSet(null);
                 chartStateModel.resetState(); // Also reset config state
            }
        }
    }

    /**
     * Closes the currently open file, clearing the dataset and resetting the view.
     */
    public void closeCurrentFile() {
        // Reset chart state and data using the model
        chartStateModel.resetState(); // This now also clears the DataSet in the model
        commandManager.clearHistory();

        if (mainView != null) {
            mainView.displayDataSet(null); // Show start screen
        }

        if (primaryStage != null) {
            primaryStage.setTitle("DataVisualizer");
        }
        System.out.println("File closed.");
    }

    /**
     * Sets the main view for this controller.
     *
     * @param mainView The main view instance.
     */
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
         if (mainView != null) {
            if (mainView.getColumnSelectionPanel() != null) {
                 mainView.getColumnSelectionPanel().setAppController(this);
            }
        }
    }

    @Override
    public void chartStateChanged() {
        triggerChartViewUpdate();
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
     * Gets the current dataset from the model.
     *
     * @return The current dataset.
     */
    public DataSet getDataSet() {
        return chartStateModel.getDataSet(); // Get DataSet from the model
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
     * Reads the current chart state and data from the ChartStateModel and tells the ChartView to update.
     * Should be called after the state is modified (e.g., by a command or direct model update).
     */
    public void triggerChartViewUpdate() {
        if (mainView != null && mainView.getChartView() != null) {
            // Read state from the model
            ChartType type = chartStateModel.getChartType();
            String xCol = chartStateModel.getXColumn();
            List<String> yCols = chartStateModel.getYColumns();
            // Get the current DataSet from the model
            DataSet currentDataSet = chartStateModel.getDataSet(); // Use the model's dataSet field

            // Pass the state AND the DataSet read from the model to the view
            mainView.getChartView().updateChart(currentDataSet, type, xCol, yCols);

             // Also update the selection panel UI to reflect the model's state
             ColumnSelectionPanel panel = mainView.getColumnSelectionPanel();
             if(panel != null) {
                 // Assuming single Y column for reflectChartState for now
                 String yColSingle = yCols.isEmpty() ? null : yCols.get(0);
                 panel.reflectChartState(type, xCol, yColSingle);
             }
        } else {
             System.err.println("Cannot trigger chart view update: MainView or ChartView is null.");
        }
    }


    /**
     * Requests an update to the chart based on selections from the panel.
     * Performs validation and executes a command to change the state in the ChartStateModel.
     *
     * @param requestedType The selected chart type from the panel.
     * @param requestedXCol The selected X-axis column from the panel.
     * @param requestedYCol The selected Y-axis column from the panel.
     */
    public void requestChartUpdate(ChartType requestedType, String requestedXCol, String requestedYCol) {
        // --- Validation ---
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
        errorDisplay.clearErrors();
        if (chartStateModel.getDataSet() == null) { // Check DataSet in the model
            System.err.println("Cannot update chart: No data loaded.");
            return;
        }
        boolean valid = true;
        if (requestedType == null) { valid = false; /* Log error if needed */ }
        if (requestedXCol == null) { errorDisplay.showXAxisError("Please select a column for the X-Axis."); valid = false; }
        if (requestedYCol == null) { errorDisplay.showYAxisError("Please select a column for the Y-Axis."); valid = false; }
        if (requestedXCol != null && requestedYCol != null && requestedXCol.equals(requestedYCol)) {
            errorDisplay.showYAxisError("X and Y axes cannot be the same."); valid = false;
        }
        if (!valid) {
             // Clear chart or revert panel if validation fails
             if (mainView.getChartView() != null) mainView.getChartView().clearChart();
             // Optionally revert panel state
             // panel.reflectChartState(chartStateModel.getChartType(), chartStateModel.getXColumn(), chartStateModel.getYColumns().isEmpty() ? null : chartStateModel.getYColumns().get(0));
            return;
        }
        // --- End Validation ---

        // --- Create and Execute Command ---
        // Get previous state from the ChartStateModel
        ChartType previousType = chartStateModel.getChartType();
        String previousX = chartStateModel.getXColumn();
        List<String> previousY = chartStateModel.getYColumns(); // Already immutable
        // Assuming single Y column selection for simplicity in command creation
        List<String> requestedYList = (requestedYCol != null) ? List.of(requestedYCol) : Collections.emptyList();

        // Check if state actually changed (optional, command could be idempotent)
        if (!Objects.equals(previousType, requestedType) || !Objects.equals(previousX, requestedXCol) || !previousY.equals(requestedYList)) {
             // Create a command that operates on the ChartStateModel
             Command updateCmd = new UpdateChartStateCommand(
                 chartStateModel, // Pass the model
                 previousType, previousX, previousY, // Previous state
                 requestedType, requestedXCol, requestedYList // New state
             );
            // Execute the command
            commandManager.executeCommand(updateCmd);
        }
        // --- End Command Execution ---
    }

    /**
     * Requests swapping of the X and Y axes selected in the panel.
     */
    public void requestAxisSwap() {
        // --- Validation ---
         if (mainView == null || mainView.getColumnSelectionPanel() == null) { System.err.println("Cannot swap axes: View components not ready."); return; }
        // Check if data is loaded via the model
        if (chartStateModel.getDataSet() == null) {
            System.err.println("Cannot swap axes: No data loaded.");
            return;
        }

        ColumnSelectionPanel panel = mainView.getColumnSelectionPanel();
        ErrorDisplayView errorDisplay = panel.getErrorDisplayView();
        if (errorDisplay == null) { System.err.println("Cannot swap axes: ErrorDisplayView not initialized."); return; }
        errorDisplay.clearErrors();

        // Get selections *from the panel* as the user's intent
        String panelX = panel.getSelectedXAxisColumn();
        String panelY = panel.getSelectedYAxisColumn();
        // Get current type from the model, not the panel, as swap shouldn't change type
        ChartType currentType = chartStateModel.getChartType();

        boolean valid = true;
        if (panelX == null) { errorDisplay.showXAxisError("Select both X and Y axes to swap."); valid = false; }
        if (panelY == null) { errorDisplay.showYAxisError("Select both X and Y axes to swap."); valid = false; }
        if (!valid) { return; }
        if (panelX.equals(panelY)) { errorDisplay.showYAxisError("X and Y axes cannot be the same."); return; }
        // --- End Validation ---

        // Request an update using the validated panel selections, but swapped, keeping current type
        // This will go through the command creation process in requestChartUpdate
        requestChartUpdate(currentType, panelY, panelX);
    }
}