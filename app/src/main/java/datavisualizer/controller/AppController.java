package datavisualizer.controller;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.chart.ChartType;
import datavisualizer.view.MainView;
import datavisualizer.view.ColumnSelectionPanel;
import datavisualizer.view.ErrorDisplayView;
import datavisualizer.model.command.Command;
import datavisualizer.model.command.ChangeChartTypeCommand;
import datavisualizer.model.command.UpdateVisibleColumnsCommand;

import javafx.stage.Stage;

import java.util.List;
import java.util.Objects;
import java.util.ArrayList;
import java.util.Collections;

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

    // --- Chart State Fields ---
    private ChartType currentChartType = ChartType.BAR; // Default
    private String currentXColumn = null;
    private List<String> currentYColumns = new ArrayList<>();
    // --- End Chart State Fields ---


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
            // Reset chart state when new data is loaded
            this.currentChartType = ChartType.BAR; // Reset to default
            this.currentXColumn = null;
            this.currentYColumns.clear();
            commandManager.clearHistory(); // Clear undo/redo

            if (mainView != null) {
                mainView.displayDataSet(this.dataSet);
                // Set initial state in panel after populating
                ColumnSelectionPanel panel = mainView.getColumnSelectionPanel();
                if (panel != null && !dataSet.getColumnNames().isEmpty()) {
                    String defaultX = dataSet.getColumnNames().get(0);
                    String defaultY = dataSet.getColumnNames().size() > 1 ? dataSet.getColumnNames().get(1) : defaultX;
                    this.currentXColumn = defaultX; // Set initial controller state
                    this.currentYColumns = List.of(defaultY);
                    this.currentChartType = ChartType.BAR;
                    panel.reflectChartState(this.currentChartType, this.currentXColumn, defaultY); // Update panel UI
                    triggerChartViewUpdate(); // Trigger initial chart render
                }
            }
        } else {
            System.err.println("Failed to load data file or operation cancelled.");
        }
    }

    /**
     * Closes the currently open file, clearing the dataset and resetting the view.
     */
    public void closeCurrentFile() {
        this.dataSet = null; // Clear the dataset
        // Reset chart state
        this.currentChartType = ChartType.BAR;
        this.currentXColumn = null;
        this.currentYColumns.clear();
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
        // Pass controller reference down if needed by views directly (try to avoid)
         if (mainView != null) {
            if (mainView.getChartView() != null) {
                 mainView.getChartView().setAppController(this); // ChartView still needs it for dataSet access
            }
            if (mainView.getColumnSelectionPanel() != null) {
                 mainView.getColumnSelectionPanel().setAppController(this); // Panel needs it for callbacks
            }
        }
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

    public ChartType getCurrentChartType() {
        return currentChartType;
    }

    public String getCurrentXColumn() {
        return currentXColumn;
    }

    public List<String> getCurrentYColumns() {
        // Return immutable copy to prevent external modification
        return Collections.unmodifiableList(currentYColumns);
    }

   /**
     * Updates the internal chart state. Called by Commands.
     * Does not trigger UI update directly.
     *
     * @param type The new chart type.
     * @param xCol The new X-axis column.
     * @param yCol The new Y-axis column (single selection assumed for now).
     */
    public void updateChartState(ChartType type, String xCol, String yCol) {
        this.currentChartType = type;
        this.currentXColumn = xCol;
        // Assuming single Y column selection from panel for now
        this.currentYColumns = (yCol != null) ? new ArrayList<>(List.of(yCol)) : new ArrayList<>();
    }

    /**
     * Reads the current chart state from this controller and tells the ChartView to update.
     * Should be called after the state is modified (e.g., by a command).
     */
    public void triggerChartViewUpdate() {
        if (mainView != null && mainView.getChartView() != null) {
            // Pass the state stored in the controller to the view
            mainView.getChartView().updateChart(this.currentChartType, this.currentXColumn, this.currentYColumns);

             // Also update the selection panel UI to reflect the state
             ColumnSelectionPanel panel = mainView.getColumnSelectionPanel();
             if(panel != null) {
                 // Assuming single Y column for reflectChartState
                 String yCol = this.currentYColumns.isEmpty() ? null : this.currentYColumns.get(0);
                 panel.reflectChartState(this.currentChartType, this.currentXColumn, yCol);
             }
        } else {
             System.err.println("Cannot trigger chart view update: MainView or ChartView is null.");
        }
    }


    /**
     * Requests an update to the chart based on selections from the panel.
     * Performs validation and executes a command to change the state.
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
        if (dataSet == null) {
            System.err.println("Cannot update chart: No data loaded.");
            // No need to clear chart here, state change will handle it if needed
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
            // If validation fails, maybe revert panel selections to controller's current state?
            // panel.reflectChartState(this.currentChartType, this.currentXColumn, this.currentYColumns.isEmpty() ? null : this.currentYColumns.get(0));
            // Or just clear the chart if that's preferred
             if (mainView.getChartView() != null) mainView.getChartView().clearChart();
            return;
        }
        // --- End Validation ---

        // --- Create and Execute Command ---
        // Get previous state from the controller itself
        ChartType previousType = this.currentChartType;
        String previousX = this.currentXColumn;
        List<String> previousY = new ArrayList<>(this.currentYColumns); // Copy

        // Assuming single Y column selection for simplicity in command creation
        List<String> requestedYList = List.of(requestedYCol);

        // Check if state actually changed
        if (!Objects.equals(previousType, requestedType) || !Objects.equals(previousX, requestedXCol) || !previousY.equals(requestedYList)) {
            // Create a command that operates on the AppController state
            // We might need a more general "UpdateChartStateCommand"
            // For now, let's adapt ChangeChartTypeCommand conceptually (needs refactoring)

            // Example using a hypothetical combined command:
             Command updateCmd = new UpdateChartStateCommand(
                 this, // Pass controller
                 previousType, previousX, previousY, // Previous state
                 requestedType, requestedXCol, requestedYList // New state
             );

            // Execute the command. The command's execute() will call
            // appController.updateChartState(...) and appController.triggerChartViewUpdate().
            commandManager.executeCommand(updateCmd);
        }
        // --- End Command Execution ---
    }


    /**
     * Requests swapping of the X and Y axes selected in the panel.
     */
    public void requestAxisSwap() {
        // --- Validation --- (Remains mostly the same)
         if (mainView == null || mainView.getColumnSelectionPanel() == null) { /*...*/ return; }
        ColumnSelectionPanel panel = mainView.getColumnSelectionPanel();
        ErrorDisplayView errorDisplay = panel.getErrorDisplayView();
        if (errorDisplay == null) { /*...*/ return; }
        errorDisplay.clearErrors();

        // Get selections *from the panel* as the user's intent
        String panelX = panel.getSelectedXAxisColumn();
        String panelY = panel.getSelectedYAxisColumn();
        ChartType panelType = panel.getSelectedChartType(); // Keep current type

        boolean valid = true;
        if (panelX == null) { errorDisplay.showXAxisError("Select both X and Y axes to swap."); valid = false; }
        if (panelY == null) { errorDisplay.showYAxisError("Select both X and Y axes to swap."); valid = false; }
        if (!valid) { return; }
        if (panelX.equals(panelY)) { errorDisplay.showYAxisError("X and Y axes cannot be the same."); return; }
        // --- End Validation ---

        // Request an update using the validated panel selections, but swapped
        // This will go through the command creation process in requestChartUpdate
        requestChartUpdate(panelType, panelY, panelX);
    }

    // --- Hypothetical Command (Needs to be created) ---
    // This replaces ChangeChartTypeCommand and UpdateVisibleColumnsCommand
    private static class UpdateChartStateCommand implements Command {
        private final AppController controller;
        private final ChartType prevType, newType;
        private final String prevX, newX;
        private final List<String> prevY, newY;

        public UpdateChartStateCommand(AppController controller,
                                       ChartType prevType, String prevX, List<String> prevY,
                                       ChartType newType, String newX, List<String> newY) {
            this.controller = controller;
            this.prevType = prevType; this.prevX = prevX; this.prevY = new ArrayList<>(prevY);
            this.newType = newType; this.newX = newX; this.newY = new ArrayList<>(newY);
        }

        @Override
        public void execute() {
            // Assuming single Y for updateChartState for now
            controller.updateChartState(newType, newX, newY.isEmpty() ? null : newY.get(0));
            controller.triggerChartViewUpdate(); // Tell controller to refresh the view
        }

        @Override
        public void undo() {
             // Assuming single Y for updateChartState for now
            controller.updateChartState(prevType, prevX, prevY.isEmpty() ? null : prevY.get(0));
            controller.triggerChartViewUpdate(); // Tell controller to refresh the view
        }
    }
}