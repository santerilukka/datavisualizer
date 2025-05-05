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


import java.util.ArrayList;
import java.util.List;

/**
 * View responsible for displaying the chart.
 * This will contain a JavaFX Chart object.
 */
public class ChartView {
    private BorderPane chartContainer; // The main container for the chart
    private String currentXColumn;
    private List<String> currentYColumns = new ArrayList<>();
    private ChartType currentChartType;

    private Node selectionPromptNode; // The UI displayed initially, prompting the user to select chart options

    /**
     * Constructs a new ChartView.
     * Initializes the chart container and the selection prompt node.
     * Sets the initial state to show the prompt.
     */
    public ChartView() {
        chartContainer = new BorderPane();
        selectionPromptNode = createSelectionPromptNode();
        clearChart();
    }

    /**
     * Gets the main BorderPane container used for displaying the chart or prompt.
     *
     * @return The BorderPane chart container.
     */
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
    public void updateChart(DataSet dataSet, ChartType chartType, String xColumn, List<String> yColumns) {
        // Check if essential components are available
        if (dataSet == null) {
            System.err.println("ChartView: Cannot update chart: DataSet is null.");
            clearChart();
            return;
        }

        // Check if columns are provided (basic check, controller should validate more)
        if (xColumn == null || yColumns == null /* Allow empty yColumns for some chart types? */ ) {
            System.err.println("ChartView: Cannot update chart: Columns not specified.");
            clearChart();
        return;
        }

        // Attempt to create the chart using the provided state and dataSet
        Chart newChart = ChartFactory.createChart(chartType, dataSet, xColumn, yColumns);

        if (newChart != null) {
            // Chart created successfully, display it
            chartContainer.setCenter(newChart);
            // Update internal fields mainly for getter consistency if needed
            this.currentXColumn = xColumn;
            this.currentYColumns = new ArrayList<>(yColumns); // Store a mutable copy
            this.currentChartType = chartType;
        } else {
            // Chart creation failed
            System.err.println("ChartView: Failed to create chart. Type: " + chartType + ", X: " + xColumn + ", Y: " + yColumns);
            clearChart(); // Show prompt if chart creation fails
        }
    }

    /**
     * Clears the current chart from the display and shows the initial selection prompt.
     */
    public void clearChart() {
        chartContainer.setCenter(selectionPromptNode);
        // Clear internal tracking fields
        currentXColumn = null;
        currentYColumns.clear();
        currentChartType = null;
    }


    // Getters
    public ChartType getCurrentChartType() { return currentChartType; }
    public String getCurrentXColumn() { return currentXColumn; }
    public List<String> getCurrentYColumns() { return List.copyOf(currentYColumns); }


    /**
     * Creates the UI node (a VBox with labels) that prompts the user to select chart options.
     * This is displayed when the application starts or when the data/chart is cleared.
     *
     * @return The Node representing the selection prompt.
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