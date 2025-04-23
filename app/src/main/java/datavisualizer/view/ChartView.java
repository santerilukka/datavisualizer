// DataVisualizerFX/src/main/java/datavisualizerfx/view/ChartView.java
package datavisualizer.view;

import datavisualizer.model.chart.ChartFactory;
import datavisualizer.model.chart.ChartType;
import javafx.scene.chart.Chart;
import javafx.scene.layout.BorderPane;

import datavisualizer.controller.AppController;

import java.util.ArrayList;
import java.util.List;

/**
 * View responsible for displaying the chart.
 * This will contain a JavaFX Chart object.
 */
public class ChartView {

    private BorderPane chartContainer;
    private Chart currentChart;
    private String currentXColumn;
    private List<String> currentYColumns = new ArrayList<>();
    private ChartType currentChartType;
    private AppController appController;

    /**
     * Constructs a new ChartView.
     */
    public ChartView() {
        chartContainer = new BorderPane();
        // Initialize UI components from FXML if used (it's a simple BorderPane for now)
    }

    /**
     * Gets the container for the chart.
     *
     * @return The BorderPane that holds the chart.
     */
    public BorderPane getChartContainer() {
        return chartContainer;
    }

    /**
     * Updates the displayed chart with the new type and data.
     *
     * @param chartType The new type of chart to display.
     * @param xColumn   The column to use for the X-axis.
     * @param yColumns  The columns to use for the Y-axis.
     */
    public void updateChart(ChartType chartType, String xColumn, List<String> yColumns) {
        if (appController != null && appController.getDataSet() != null && xColumn != null && !yColumns.isEmpty()) {
            Chart newChart = ChartFactory.createChart(chartType, appController.getDataSet(), xColumn, yColumns);
            if (newChart != null) {
                chartContainer.setCenter(newChart);
                currentChart = newChart;
                currentXColumn = xColumn;
                currentYColumns = new ArrayList<>(yColumns);
                currentChartType = chartType;
            } else {
                System.err.println("Unsupported chart type: " + chartType);
                // Optionally, display an error message to the user
            }
        } else if (appController != null && appController.getDataSet() != null && xColumn != null && yColumns.isEmpty()) {
            // Handle case where only an X-axis is selected (e.g., for distribution)
            Chart newChart = ChartFactory.createChart(chartType, appController.getDataSet(), xColumn, List.of()); // Modify ChartFactory if needed
            if (newChart != null) {
                chartContainer.setCenter(newChart);
                currentChart = newChart;
                currentXColumn = xColumn;
                currentYColumns.clear();
                currentChartType = chartType;
            }
        } else {
            // Handle cases where data is not loaded or columns are not selected
            clearChart();
        }
    }

    /**
     * Hides the specified columns from the current chart.
     *
     * @param columnsToHide The names of the columns to hide.
     */
    public void hideColumns(List<String> columnsToHide) {
        if (currentChart != null) {
            List<String> updatedYColumns = currentYColumns.stream()
                    .filter(col -> !columnsToHide.contains(col))
                    .toList();
            updateChart(currentChartType, currentXColumn, updatedYColumns);
        }
    }

    /**
     * Shows the specified columns that were previously hidden.
     *
     * @param columnsToShow The names of the columns to show.
     */
    public void showColumns(List<String> columnsToShow) {
        if (currentChart != null) {
            List<String> updatedYColumns = new ArrayList<>(currentYColumns);
            for (String col : columnsToShow) {
                if (appController.getDataSet().getColumnNames().contains(col) && !updatedYColumns.contains(col)) {
                    updatedYColumns.add(col);
                }
            }
            updateChart(currentChartType, currentXColumn, updatedYColumns);
        }
    }

    /**
     * Clears the chart display.
     */
    public void clearChart() {
        chartContainer.setCenter(null);
        currentChart = null;
        currentXColumn = null;
        currentYColumns.clear();
        currentChartType = null;
    }

    /**
     * Sets the application controller for this view.
     *
     * @param appController The application controller.
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }
    /**
     * Gets the current chart type being displayed.
     *
     * @return The current ChartType, or null if no chart is displayed.
     */
    public ChartType getCurrentChartType() {
        return currentChartType;
    }

    /**
     * Gets the column currently used for the X-axis.
     *
     * @return The name of the X-axis column, or null if no chart is displayed.
     */
    public String getCurrentXColumn() {
        return currentXColumn;
    }

    // You might also want a getter for Y columns, though the command now handles the list directly
    /**
     * Gets the list of columns currently used for the Y-axis.
     *
     * @return An immutable list of the Y-axis column names.
     */
    public List<String> getCurrentYColumns() {
        return List.copyOf(currentYColumns); // Return an immutable copy
    }
}