package datavisualizer.model.chart;

import datavisualizer.model.dataset.DataSet;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.CategoryAxis;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Factory class for creating different types of charts.
 */
public class ChartFactory {

    /**
     * Creates a chart based on the specified ChartType and DataSet.
     *
     * @param chartType The type of chart to create.
     * @param dataSet   The dataset to visualize.
     * @param xColumn   The column to use for the X-axis (label column for PieChart).
     * @param yColumns  The columns to use for the Y-axis (value column for PieChart - only the first is used).
     * @return The created JavaFX Chart object, or null if the type is unknown or data is unsuitable.
     */
    public static Chart createChart(ChartType chartType, DataSet dataSet, String xColumn, List<String> yColumns) {
        if (!validateInput(dataSet, xColumn, yColumns, chartType)) {
            return null;
        }

        switch (chartType) {
            case BAR:
                return createBarChart(dataSet, xColumn, yColumns);
            case LINE:
                return createLineChart(dataSet, xColumn, yColumns);
            case PIE:
                // Pie chart uses the first Y column as the value column
                return createPieChart(dataSet, xColumn, yColumns.get(0));
            default:
                System.err.println("Unsupported chart type: " + chartType);
                return null;
        }
    }

    /**
     * Validates the input parameters for chart creation.
     *
     * @param dataSet   The dataset.
     * @param xColumn   The X-axis column name.
     * @param yColumns  The list of Y-axis column names.
     * @param chartType The type of chart.
     * @return true if the input is valid, false otherwise.
     */
    private static boolean validateInput(DataSet dataSet, String xColumn, List<String> yColumns, ChartType chartType) {
        if (dataSet == null || xColumn == null || yColumns == null || yColumns.isEmpty()) {
            System.err.println("Cannot create chart: DataSet or columns are invalid.");
            return false;
        }
        if (!dataSet.getColumnNames().contains(xColumn)) {
            System.err.println("Cannot create chart: X-axis column '" + xColumn + "' not found in DataSet.");
            return false;
        }

        String firstYColumn = yColumns.get(0);
        if (chartType == ChartType.PIE && !dataSet.getColumnNames().contains(firstYColumn)) {
            System.err.println("Cannot create PieChart: Value column '" + firstYColumn + "' not found in DataSet.");
            return false;
        }

        if (chartType != ChartType.PIE) {
            for (String yCol : yColumns) {
                if (!dataSet.getColumnNames().contains(yCol)) {
                    // Log a warning but allow creation with valid columns
                    System.err.println("Warning: Y-axis column '" + yCol + "' not found in DataSet. Skipping this column.");
                }
            }
            // Check if at least one valid Y column remains for non-pie charts
            boolean hasValidY = yColumns.stream().anyMatch(dataSet.getColumnNames()::contains);
            if (!hasValidY) {
                 System.err.println("Cannot create chart: No valid Y-axis columns found in DataSet.");
                 return false;
            }
        }
        return true;
    }

    /**
     * Aggregates data for XY charts (Bar, Line). Sums Y values for duplicate X categories.
     *
     * @param dataSet The dataset.
     * @param xColumn The X-axis column name.
     * @param yColumn The Y-axis column name.
     * @param chartTypeName Name of the chart type for logging purposes (e.g., "BarChart").
     * @return A map where keys are X categories and values are aggregated Y values.
     */
    private static Map<String, Double> aggregateXYData(DataSet dataSet, String xColumn, String yColumn, String chartTypeName) {
        Map<String, Double> aggregatedData = new LinkedHashMap<>(); // Maintain order
        List<Object> xData = dataSet.getColumnData(xColumn);
        List<Object> yData = dataSet.getColumnData(yColumn);

        int size = Math.min(xData.size(), yData.size()); // Process only matching pairs
        for (int i = 0; i < size; i++) {
            String category = xData.get(i) != null ? xData.get(i).toString() : "N/A";
            Object yValueObj = yData.get(i);
            if (yValueObj != null) {
                try {
                    double yValue = Double.parseDouble(yValueObj.toString());
                    aggregatedData.merge(category, yValue, Double::sum);
                } catch (NumberFormatException e) {
                    System.err.println("Skipping non-numeric value in " + chartTypeName + " for column " + yColumn + ", category " + category + ": " + yValueObj);
                }
            } else {
                 System.err.println("Skipping null value in " + chartTypeName + " for column " + yColumn + ", category " + category);
            }
        }
        return aggregatedData;
    }

    /**
     * Creates a BarChart based on the DataSet.
     *
     * @param dataSet  The dataset to visualize.
     * @param xColumn  The column for the X-axis categories.
     * @param yColumns The columns for the Y-axis values.
     * @return A configured BarChart.
     */
    private static BarChart<String, Number> createBarChart(DataSet dataSet, String xColumn, List<String> yColumns) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("Bar Chart");
        xAxis.setLabel(xColumn);

        for (String yColumn : yColumns) {
            if (dataSet.getColumnNames().contains(yColumn)) {
                Map<String, Double> aggregatedData = aggregateXYData(dataSet, xColumn, yColumn, "BarChart");
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(yColumn);
                aggregatedData.forEach((category, value) -> series.getData().add(new XYChart.Data<>(category, value)));

                if (!series.getData().isEmpty()) {
                    barChart.getData().add(series);
                }
            }
        }
        return barChart;
    }

    /**
     * Creates a LineChart based on the DataSet.
     *
     * @param dataSet  The dataset to visualize.
     * @param xColumn  The column for the X-axis categories.
     * @param yColumns The columns for the Y-axis values.
     * @return A configured LineChart.
     */
    private static LineChart<String, Number> createLineChart(DataSet dataSet, String xColumn, List<String> yColumns) {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setTitle("Line Chart");
        xAxis.setLabel(xColumn);
        lineChart.setCreateSymbols(true); // Show symbols on data points

        for (String yColumn : yColumns) {
            if (dataSet.getColumnNames().contains(yColumn)) {
                 Map<String, Double> aggregatedData = aggregateXYData(dataSet, xColumn, yColumn, "LineChart");
                 XYChart.Series<String, Number> series = new XYChart.Series<>();
                 series.setName(yColumn);
                 aggregatedData.forEach((category, value) -> series.getData().add(new XYChart.Data<>(category, value)));

                 if (!series.getData().isEmpty()) {
                     lineChart.getData().add(series);
                 }
            }
        }
        return lineChart;
    }

    /**
     * Creates a PieChart based on the DataSet.
     * Aggregates values for duplicate labels.
     *
     * @param dataSet     The dataset to visualize.
     * @param labelColumn The column containing the labels for the pie slices.
     * @param valueColumn The column containing the numeric values for the pie slices.
     * @return A configured PieChart, or null if data is unsuitable.
     */
    private static PieChart createPieChart(DataSet dataSet, String labelColumn, String valueColumn) {
        // Use the aggregation helper, treating labelColumn as xColumn and valueColumn as yColumn
        Map<String, Double> aggregatedData = aggregateXYData(dataSet, labelColumn, valueColumn, "PieChart");

        // Filter out non-positive values as they don't make sense in a standard PieChart
        Map<String, Double> positiveAggregatedData = new LinkedHashMap<>();
        aggregatedData.forEach((label, value) -> {
            if (value > 0) {
                positiveAggregatedData.put(label, value);
            } else if (value < 0) {
                 System.err.println("Skipping negative aggregated value in PieChart for label " + label + ": " + value);
            }
            // Zero values are implicitly skipped
        });

        if (positiveAggregatedData.isEmpty()) {
            System.err.println("No positive data found for PieChart.");
            return null; // Return null if no valid slices
        }

        // Calculate total for percentage calculation
        final AtomicReference<Double> total = new AtomicReference<>(0.0);
        positiveAggregatedData.values().forEach(val -> total.updateAndGet(v -> v + val));


        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        // Add aggregated data to the PieChart data list and format the label
        positiveAggregatedData.forEach((label, value) -> {
            double percentage = (total.get() == 0) ? 0 : (value / total.get()) * 100;
            // Updated format to include raw value
            String labelWithValueAndPercent = String.format("%s: %.2f (%.1f%%)", label, value, percentage);
            PieChart.Data slice = new PieChart.Data(labelWithValueAndPercent, value);
            pieChartData.add(slice);
        });


        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Pie Chart: " + valueColumn + " by " + labelColumn);
        pieChart.setLabelsVisible(true);

        return pieChart;
    }
}