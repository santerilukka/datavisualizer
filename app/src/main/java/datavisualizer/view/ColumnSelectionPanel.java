package datavisualizer.view;

import datavisualizer.controller.AppController;
import datavisualizer.model.chart.ChartType;
import datavisualizer.model.dataset.DataSet;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Panel for selecting columns and chart type.
 */
public class ColumnSelectionPanel {

    @FXML private VBox selectionPanel; // The root VBox defined in FXML
    @FXML private ComboBox<String> xAxisComboBox;// Axis selection controls
    @FXML private ComboBox<String> yAxisComboBox;
    @FXML private ComboBox<ChartType> chartTypeComboBox;
    @FXML private Label xAxisErrorLabel; // Error labels
    @FXML private Label yAxisErrorLabel;
    @FXML private Button swapAxesButton; // Button to swap axes

    private AppController appController;
    private ChartView chartView; // Reference to the ChartView
    private ErrorDisplayView errorDisplayView; // Reference to the ErrorDisplayView

    /**
     * Initializes the panel. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize the error display view with the injected labels
        errorDisplayView = new ErrorDisplayView(xAxisErrorLabel, yAxisErrorLabel);
        // Populate the chart type ComboBox
        chartTypeComboBox.getItems().setAll(ChartType.values());
        chartTypeComboBox.setValue(ChartType.BAR); // Default selection
    
        // Add listeners to request updates from the controller
        xAxisComboBox.setOnAction(event -> updateChart());
        yAxisComboBox.setOnAction(event -> updateChart());
        chartTypeComboBox.setOnAction(event -> updateChart());
    
        // Ensure labels are initially hidden and cleared via ErrorDisplayView
        errorDisplayView.clearErrors();
    }

    /**
     * Sets the application controller.
     *
     * @param appController The application controller.
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    /**
     * Sets the chart view.
     *
     * @param chartView The chart view instance.
     */
    public void setChartView(ChartView chartView) {
        this.chartView = chartView;
    }


    /**
     * Populates the column selection components based on the loaded dataset.
     *
     * @param columnNames The list of column names from the dataset.
     */
    public void populateColumns(List<String> columnNames) {
        // Clear previous items
        xAxisComboBox.getItems().clear();
        yAxisComboBox.getItems().clear();
        // Clear validation errors when new data is loaded/cleared
        if (errorDisplayView != null) { // Check if initialized
             errorDisplayView.clearErrors();
        }

        if (columnNames != null && !columnNames.isEmpty()) {
            // Populate ComboBoxes
            xAxisComboBox.getItems().addAll(columnNames);
            yAxisComboBox.getItems().addAll(columnNames);

            // Set defaults
            String defaultX = columnNames.get(0);
            String defaultY = columnNames.size() > 1 ? columnNames.get(1) : columnNames.get(0);
            xAxisComboBox.setValue(defaultX);
            yAxisComboBox.setValue(defaultY);

        } else {
             // No columns, clear selections
             xAxisComboBox.setValue(null);
             yAxisComboBox.setValue(null);
        }
    }

    /**
     * Gets the root VBox node of this panel.
     *
     * @return The VBox selection panel.
     */
    public VBox getSelectionPanel() {
        return selectionPanel;
    }


    /**
     * Handles the action of updating the chart by requesting it from the controller.
     * This is called by UI interactions (button press, selection changes).
     */
    @FXML
    private void updateChart() {
        if (appController != null) {
            // Request the update from the controller, passing current selections
            appController.requestChartUpdate(getSelectedChartType(), getSelectedXAxisColumn(), getSelectedYAxisColumn());
        } else {
            System.err.println("ColumnSelectionPanel: AppController not set, cannot update chart.");
        }
    }


     /**
     * Gets the currently selected X-axis column.
     *
     * @return The name of the selected X-axis column, or null if none selected.
     */
    public String getSelectedXAxisColumn() {
        return xAxisComboBox.getValue();
    }

    /**
     * Gets the currently selected Y-axis column.
     *
     * @return The name of the selected Y-axis column, or null if none selected.
     */
    public String getSelectedYAxisColumn() {
        return yAxisComboBox.getValue();
    }


    /**
     * Gets the currently selected chart type.
     *
     * @return The selected ChartType, or null if none selected.
     */
    public ChartType getSelectedChartType() {
        return chartTypeComboBox.getValue();
    }

    /**
     * Handles the action of swapping axes by requesting it from the controller.
     */
    @FXML
    private void swapAxes() {
        if (appController != null) {
            appController.requestAxisSwap();
        } else {
            System.err.println("ColumnSelectionPanel: AppController not set, cannot swap axes.");
        }
    }

     /**
     * Updates the ComboBox selections to reflect the given chart state.
     * Used to ensure UI consistency after programmatic changes (e.g., undo/redo, swap).
     * Disables listeners temporarily to prevent triggering updates.
     *
     * @param type The chart type to select.
     * @param xCol The X-axis column to select.
     * @param yCol The Y-axis column to select.
     */
    public void reflectChartState(ChartType type, String xCol, String yCol) {
        // Temporarily disable listeners
        chartTypeComboBox.setOnAction(null);
        xAxisComboBox.setOnAction(null);
        yAxisComboBox.setOnAction(null);

        // Update selections
        chartTypeComboBox.setValue(type);
        xAxisComboBox.setValue(xCol);
        yAxisComboBox.setValue(yCol);

        // Re-enable listeners
        chartTypeComboBox.setOnAction(event -> updateChart());
        xAxisComboBox.setOnAction(event -> updateChart());
        yAxisComboBox.setOnAction(event -> updateChart());
    }

    /**
     * Gets the error display view associated with this panel.
     *
     * @return The ErrorDisplayView instance.
     */
    public ErrorDisplayView getErrorDisplayView() {
        return errorDisplayView;
    }
}