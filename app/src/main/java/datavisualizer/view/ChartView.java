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
    private Node selectionPromptNode;

    public ChartView() {
        chartContainer = new BorderPane();
        selectionPromptNode = createSelectionPromptNode();
        clearChart();
    }

    public BorderPane getChartContainer() {
        return chartContainer;
    }

    /**
     * Updates the displayed chart based on the state provided by the controller.
     *
     * @param chartType The type of chart to display.
     * @param xColumn   The column to use for the X-axis.
     * @param yColumns  The columns to use for the Y-axis.
     */
    public void updateChart(ChartType chartType, String xColumn, List<String> yColumns) {
        // Check if essential components are available (controller for data)
        if (appController == null || appController.getDataSet() == null) {
            System.err.println("ChartView: Cannot update chart: AppController or DataSet is null.");
            clearChart();
            return;
        }
        DataSet dataSet = appController.getDataSet();

        // Check if columns are provided (basic check, controller should validate more)
        if (xColumn == null || yColumns == null /* Allow empty yColumns for some chart types? */ ) {
             System.err.println("ChartView: Cannot update chart: Columns not specified.");
             clearChart();
            return;
        }

        // Attempt to create the chart using the provided state
        Chart newChart = ChartFactory.createChart(chartType, dataSet, xColumn, yColumns);

        if (newChart != null) {
            // Chart created successfully, display it
            chartContainer.setCenter(newChart);
            // Update internal fields mainly for getter consistency if needed
            this.currentChart = newChart;
            this.currentXColumn = xColumn;
            this.currentYColumns = new ArrayList<>(yColumns); // Store a mutable copy
            this.currentChartType = chartType;
        } else {
            // Chart creation failed
            System.err.println("ChartView: Failed to create chart. Type: " + chartType + ", X: " + xColumn + ", Y: " + yColumns);
            clearChart(); // Show prompt if chart creation fails
        }
    }

    public void clearChart() {
        chartContainer.setCenter(selectionPromptNode);
        // Clear internal tracking fields
        currentChart = null;
        currentXColumn = null;
        currentYColumns.clear();
        currentChartType = null;
    }

    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    public ChartType getCurrentChartType() { return currentChartType; }
    public String getCurrentXColumn() { return currentXColumn; }
    public List<String> getCurrentYColumns() { return List.copyOf(currentYColumns); }

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