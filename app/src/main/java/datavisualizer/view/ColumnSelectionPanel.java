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

    @FXML
    private VBox selectionPanel; // The root VBox defined in FXML
    @FXML
    private ComboBox<String> xAxisComboBox;
    @FXML
    private ComboBox<String> yAxisComboBox;
    @FXML
    private ComboBox<ChartType> chartTypeComboBox;
    @FXML
    private Label xAxisErrorLabel; // Injected error label for X-Axis
    @FXML
    private Label yAxisErrorLabel; // Injected error label for Y-Axis
    @FXML
    private Button swapAxesButton; // Button to swap axes

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
        // These will now also clear errors potentially
        xAxisComboBox.setOnAction(event -> updateChart());
        yAxisComboBox.setOnAction(event -> updateChart());
        chartTypeComboBox.setOnAction(event -> updateChart());

        // Ensure labels are initially hidden
        xAxisErrorLabel.setVisible(false);
        xAxisErrorLabel.setManaged(false);
        yAxisErrorLabel.setVisible(false);
        yAxisErrorLabel.setManaged(false);
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

        // Clear error labels when new data is loaded
        clearErrorLabels();

        if (columnNames != null && !columnNames.isEmpty()) {
            // Populate X-Axis ComboBox
            xAxisComboBox.getItems().addAll(columnNames);
            // Populate Y-Axis ComboBox
            yAxisComboBox.getItems().addAll(columnNames);

            // Set defaults: X = first column, Y = second (or first if only one)
            xAxisComboBox.setValue(columnNames.get(0));
            if (columnNames.size() > 1) {
                yAxisComboBox.setValue(columnNames.get(1));
            } else {
                yAxisComboBox.setValue(columnNames.get(0));
            }
        } else {
             xAxisComboBox.setValue(null);
             yAxisComboBox.setValue(null);
        }
        // Trigger an initial chart update (or clear) based on default selections
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
        // Clear previous errors first
        clearErrorLabels();

        if (chartView != null && appController != null && appController.getDataSet() != null) {
            DataSet dataSet = appController.getDataSet();
            if (dataSet != null && !dataSet.getColumnNames().isEmpty()) {
                String selectedXColumn = xAxisComboBox.getValue();
                String selectedYColumn = yAxisComboBox.getValue();
                ChartType selectedChartType = chartTypeComboBox.getValue();

                boolean valid = true;
                if (selectedXColumn == null) {
                    showError(xAxisErrorLabel, "Please select a column for the X-Axis.");
                    valid = false;
                }

                if (selectedYColumn == null) {
                    showError(yAxisErrorLabel, "Please select a column for the Y-Axis.");
                    valid = false;
                }

                // Only check for equality if both are selected
                if (selectedXColumn != null && selectedYColumn != null && selectedYColumn.equals(selectedXColumn)) {
                    showError(yAxisErrorLabel, "X and Y axes cannot be the same.");
                    valid = false;
                }

                if (!valid) {
                    // If validation fails, clear the chart (optional, prevents showing stale chart)
                    chartView.clearChart();
                    return; // Stop processing
                }

                // Create a list containing the single selected Y column
                List<String> selectedYColumns = List.of(selectedYColumn);

                // Update the chart view
                // TODO: Wrap this in a command for Undo/Redo
                chartView.updateChart(selectedChartType, selectedXColumn, selectedYColumns);

            } else {
                 // Data loaded but no columns? Clear chart.
                 chartView.clearChart();
            }
        } else {
            // No data loaded or chartView not set, ensure chart is clear
            if (chartView != null) {
                chartView.clearChart();
            }
            // Optionally show a persistent message if needed, but avoid alerts here.
        }
    }

    // Helper method to show an error label
    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
        label.setManaged(true);
    }

    // Helper method to clear error labels
    private void clearErrorLabels() {
        xAxisErrorLabel.setText("");
        xAxisErrorLabel.setVisible(false);
        xAxisErrorLabel.setManaged(false);
        yAxisErrorLabel.setText("");
        yAxisErrorLabel.setVisible(false);
        yAxisErrorLabel.setManaged(false);
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
     * Handles the action of swapping the selected X and Y axes.
     */
    @FXML
    private void swapAxes() {
    // Clear previous errors first, especially if swap failed before
    clearErrorLabels();

    String currentX = xAxisComboBox.getValue();
    String currentY = yAxisComboBox.getValue();

    // Check if both are selected
    if (currentX == null || currentY == null) {
        String errorMsg = "Select both X and Y axes to swap.";
        if (currentX == null) {
            showError(xAxisErrorLabel, errorMsg);
        }
        if (currentY == null) {
            showError(yAxisErrorLabel, errorMsg);
        }
        return; // Stop processing
    }

    // Check if they are different (updateChart will handle the error if they are the same)
    if (!currentX.equals(currentY)) {
        // Temporarily disable listeners to prevent multiple updates
        xAxisComboBox.setOnAction(null);
        yAxisComboBox.setOnAction(null);

        xAxisComboBox.setValue(currentY);
        yAxisComboBox.setValue(currentX);

        // Re-enable listeners
        xAxisComboBox.setOnAction(event -> updateChart());
        yAxisComboBox.setOnAction(event -> updateChart());

        // Trigger the chart update after swapping
        updateChart();
    } else {
        // If they are the same, let updateChart show the specific error
        updateChart();
    }
}
}