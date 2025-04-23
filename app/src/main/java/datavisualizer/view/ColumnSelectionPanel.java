// DataVisualizerFX/src/main/java/datavisualizer/view/ColumnSelectionPanel.java
package datavisualizer.view;

import datavisualizer.controller.AppController;
import datavisualizer.model.chart.ChartType;
import datavisualizer.model.dataset.DataSet;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Panel for selecting columns and chart type.
 */
public class ColumnSelectionPanel {

    @FXML
    private VBox selectionPanel; // The root VBox defined in FXML
    @FXML
    private ComboBox<String> xAxisComboBox;
    @FXML
    private ComboBox<String> yAxisComboBox;
    @FXML
    private VBox columnCheckboxes; // Container for Y-axis checkboxes
    @FXML
    private ComboBox<ChartType> chartTypeComboBox;

    private AppController appController;
    private ChartView chartView; // Reference to the ChartView

    /**
     * Initializes the panel. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Populate the chart type ComboBox
        chartTypeComboBox.getItems().setAll(ChartType.values());
        chartTypeComboBox.setValue(ChartType.BAR); // Default selection

        // Add listeners to update the chart when selections change
        xAxisComboBox.setOnAction(event -> updateChart());
        chartTypeComboBox.setOnAction(event -> updateChart());
        // Checkboxes will trigger update via the updateChart button or dynamically if preferred

        yAxisComboBox.setOnAction(event -> updateChart());
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
        yAxisComboBox.getItems().clear(); // Clear Y-axis combo box
    
        if (columnNames != null && !columnNames.isEmpty()) {
            // Populate X-Axis ComboBox
            xAxisComboBox.getItems().addAll(columnNames);
            xAxisComboBox.setValue(columnNames.get(0)); // Default X to the first column
    
            // Populate Y-Axis ComboBox
            yAxisComboBox.getItems().addAll(columnNames);
            // Default Y to the second column if available, otherwise the first
            if (columnNames.size() > 1) {
                yAxisComboBox.setValue(columnNames.get(1));
            } else {
                yAxisComboBox.setValue(columnNames.get(0));
            }
        } else {
             xAxisComboBox.setValue(null);
             yAxisComboBox.setValue(null);
        }
        // Trigger an initial chart update based on default selections
        updateChart();
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
     * Handles the action of updating the chart based on current selections.
     * This can be called by a button press or automatically on selection changes.
     */
    @FXML
    private void updateChart() {
        if (chartView != null && appController != null && appController.getDataSet() != null) {
            DataSet dataSet = appController.getDataSet();
            if (dataSet != null && !dataSet.getColumnNames().isEmpty()) {
                String selectedXColumn = xAxisComboBox.getValue();
                // Get selected Y column from the ComboBox
                String selectedYColumn = yAxisComboBox.getValue();
                ChartType selectedChartType = chartTypeComboBox.getValue();

            // --- Validation ---
            if (selectedXColumn == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Selection Missing");
                alert.setHeaderText(null);
                alert.setContentText("Please select a column for the X-Axis.");
                alert.showAndWait();
                return;
            }

            if (selectedYColumn == null) { // Check Y-axis selection
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Selection Missing");
                alert.setHeaderText(null);
                alert.setContentText("Please select a column for the Y-Axis.");
                alert.showAndWait();
                return;
           }
            if (selectedYColumn.equals(selectedXColumn)) { // Compare single Y column
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Selection");
                alert.setHeaderText(null);
                alert.setContentText("The X-Axis and Y-Axis columns cannot be the same.");
                alert.showAndWait();
                return;
           }
            // --- End Validation ---


            // Create a list containing the single selected Y column
            List<String> selectedYColumns = List.of(selectedYColumn);

            // Update the chart view
            chartView.updateChart(selectedChartType, selectedXColumn, selectedYColumns);

            // TODO: Undo/Redo is needed for this action, wrap the updateChart call
            // in a command and execute it via commandManager.


        } // This closing brace was missing for the inner if (dataSet != null...)
    } else {
        // This else corresponds to the outer if (chartView != null...)
        // It might be better to disable controls until data is loaded instead of showing this repeatedly.
        /*
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("No Data Loaded");
        alert.setHeaderText(null);
        alert.setContentText("Please open a data file first.");
        alert.showAndWait();
         */
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
     * Gets the currently selected chart type.
     *
     * @return The selected ChartType, or null if none selected.
     */
    public ChartType getSelectedChartType() {
        return chartTypeComboBox.getValue();
    }

}