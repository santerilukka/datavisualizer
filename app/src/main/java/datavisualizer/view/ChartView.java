package datavisualizer.view;

import datavisualizer.model.chart.ChartFactory;
import datavisualizer.model.chart.ChartType;
import datavisualizer.model.dataset.DataSet;
import javafx.scene.chart.Chart;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;

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
    private Node selectionPromptNode; // Node to show when no chart is displayed

    /**
     * Constructs a new ChartView.
     */
    public ChartView() {
        chartContainer = new BorderPane();
        selectionPromptNode = createSelectionPromptNode(); // Create the prompt
        clearChart(); // Initialize with the prompt visible
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
        // Check if essential components are available
        if (appController == null || appController.getDataSet() == null) {
            System.err.println("Cannot update chart: AppController or DataSet is null.");
            clearChart(); // Show prompt if data/controller isn't ready
            return;
        }

        DataSet dataSet = appController.getDataSet();

        // Check if columns are selected (handle single Y-column case)
        if (xColumn == null || yColumns == null || yColumns.isEmpty()) {
             // If only X is selected, or Y is missing, we might still want to clear or show prompt
             // Depending on desired behavior for incomplete selections. Let's clear for now.
            clearChart(); // Show prompt if axes are not fully selected
            return;
        }

        // Attempt to create the chart
        Chart newChart = ChartFactory.createChart(chartType, dataSet, xColumn, yColumns);

        if (newChart != null) {
            // Chart created successfully, display it
            chartContainer.setCenter(newChart);
            currentChart = newChart;
            currentXColumn = xColumn;
            currentYColumns = new ArrayList<>(yColumns); // Store a mutable copy
            currentChartType = chartType;
        } else {
            // Chart creation failed (e.g., unsupported type, bad data for type)
            System.err.println("Failed to create chart. Type: " + chartType + ", X: " + xColumn + ", Y: " + yColumns);
            clearChart(); // Show prompt if chart creation fails
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
     * Clears the chart display and shows the selection prompt.
     */
    public void clearChart() {
        chartContainer.setCenter(selectionPromptNode); // Show user prompt node
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

    /**
     * Creates the Node used as a prompt when no chart is selected.
     *
     * @return The configured Node for the prompt.
     */
    private Node createSelectionPromptNode() {
        VBox promptBox = new VBox(10);
        promptBox.setAlignment(Pos.CENTER);
        promptBox.setPadding(new Insets(20));

        Label instructionLabel = new Label("Data loaded successfully!");
        instructionLabel.setStyle("-fx-font-size: 16px;");

        Label instructionLabel2 = new Label("Please select options from the right panel to generate a chart.");
        instructionLabel2.setWrapText(true);
        instructionLabel2.setAlignment(Pos.CENTER);


        promptBox.getChildren().addAll(instructionLabel, instructionLabel2);
        return promptBox;
    }
}