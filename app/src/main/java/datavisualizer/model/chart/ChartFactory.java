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
     * @param xColumn   The column to use for the X-axis.
     * @param yColumns  The columns to use for the Y-axis.
     * @return The created JavaFX Chart object, or null if the type is unknown.
     */
    public static Chart createChart(ChartType chartType, DataSet dataSet, String xColumn, List<String> yColumns) {
        switch (chartType) {
            case BAR:
                return createBarChart(dataSet, xColumn, yColumns);
            case LINE:
                return createLineChart(dataSet, xColumn, yColumns);
            case PIE:
                return createPieChart(dataSet, xColumn, yColumns.get(0));
            default:
                return null;
        }
    }

    private static BarChart<String, Number> createBarChart(DataSet dataSet, String xColumn, List<String> yColumns) {
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(new javafx.scene.chart.CategoryAxis(), yAxis);
        barChart.setTitle("Bar Chart");

        if (dataSet != null && dataSet.getColumnNames().contains(xColumn)) {
            List<Object> xData = dataSet.getColumnData(xColumn);
            for (String yColumn : yColumns) {
                if (dataSet.getColumnNames().contains(yColumn)) {
                    XYChart.Series<String, Number> series = new XYChart.Series<>();
                    series.setName(yColumn);
                    List<Object> yData = dataSet.getColumnData(yColumn);
                    for (int i = 0; i < xData.size(); i++) {
                        try {
                            double yValue = Double.parseDouble(yData.get(i).toString());
                            series.getData().add(new XYChart.Data<>(xData.get(i).toString(), yValue));
                        } catch (NumberFormatException e) {
                            // Handle non-numeric data in Y-axis if needed
                            System.err.println("Skipping non-numeric value in column " + yColumn + ": " + yData.get(i));
                        }
                    }
                    barChart.getData().add(series);
                }
            }
        }
        return barChart;
    }

    private static LineChart<String, Number> createLineChart(DataSet dataSet, String xColumn, List<String> yColumns) {
    // Use CategoryAxis for X since we are grouping by potentially non-numeric categories
    javafx.scene.chart.CategoryAxis xAxis = new javafx.scene.chart.CategoryAxis();
    NumberAxis yAxis = new NumberAxis();
    LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
    lineChart.setTitle("Line Chart");
    xAxis.setLabel(xColumn); // Set X-axis label

    if (dataSet != null && dataSet.getColumnNames().contains(xColumn) && yColumns != null) {
        List<Object> xData = dataSet.getColumnData(xColumn);

        for (String yColumn : yColumns) {
            if (dataSet.getColumnNames().contains(yColumn)) {
                List<Object> yData = dataSet.getColumnData(yColumn);
                // Use a Map to aggregate Y values for each unique X category
                Map<String, Double> aggregatedData = new LinkedHashMap<>(); // Use LinkedHashMap to maintain insertion order

                // Aggregate data (summing Y values for each X category)
                for (int i = 0; i < xData.size(); i++) {
                    if (i < yData.size()) { // Ensure yData has a corresponding value
                        String category = xData.get(i).toString();
                        try {
                            double yValue = Double.parseDouble(yData.get(i).toString());
                            // Add the yValue to the current sum for this category
                            aggregatedData.merge(category, yValue, Double::sum);
                        } catch (NumberFormatException | NullPointerException e) {
                            // Handle non-numeric or null data in Y-axis
                            System.err.println("Skipping non-numeric/null value in column " + yColumn + " for category " + category + ": " + yData.get(i));
                        }
                    }
                }

                // Create a series for the aggregated data
                XYChart.Series<String, Number> series = new XYChart.Series<>();
                series.setName(yColumn); // Name the series after the Y column

                // Add aggregated data points to the series
                for (Map.Entry<String, Double> entry : aggregatedData.entrySet()) {
                    series.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue()));
                }

                if (!series.getData().isEmpty()) {
                    lineChart.getData().add(series);
                }
            }
        }
    }

    lineChart.setCreateSymbols(true); // Show symbols on data points

    return lineChart;
    }

    /**
     * Creates a PieChart based on the DataSet.
     *
     * @param dataSet  The dataset to visualize.
     * @param labelColumn The column containing the labels for the pie slices.
     * @param valueColumn The column containing the numeric values for the pie slices.
     * @return A configured PieChart, or null if data is unsuitable.
     */
    private static PieChart createPieChart(DataSet dataSet, String labelColumn, String valueColumn) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        List<Object> labelData = dataSet.getColumnData(labelColumn);
        List<Object> valueData = dataSet.getColumnData(valueColumn);

        // Aggregate values for duplicate labels
        Map<String, Double> aggregatedData = new LinkedHashMap<>();
        for (int i = 0; i < labelData.size(); i++) {
            if (i < valueData.size()) { // Ensure valueData has a corresponding value
                 String label = labelData.get(i) != null ? labelData.get(i).toString() : "N/A";
                 try {
                    double value = Double.parseDouble(valueData.get(i).toString());
                    if (value >= 0) { // Pie slices typically represent non-negative values
                         aggregatedData.merge(label, value, Double::sum);
                    } else {
                         System.err.println("Skipping negative value in PieChart for label " + label + ": " + value);
                    }
                 } catch (NumberFormatException | NullPointerException e) {
                     System.err.println("Skipping non-numeric/null value in PieChart for label " + label + ": " + valueData.get(i));
                 }
            }
        }

        // Add aggregated data to the PieChart data list and format the label to include the value
        final AtomicReference<Double> total = new AtomicReference<>(0.0); // Use AtomicReference for modification within lambda/stream
        aggregatedData.values().forEach(val -> total.updateAndGet(v -> v + val)); // Calculate total for percentage calculation

        for (Map.Entry<String, Double> entry : aggregatedData.entrySet()) {
             // Format the label string to include the category name and its percentage
             double percentage = (total.get() == 0) ? 0 : (entry.getValue() / total.get()) * 100;
             String labelWithValue = String.format("%s (%.1f%%)", entry.getKey(), percentage);

             PieChart.Data slice = new PieChart.Data(labelWithValue, entry.getValue());
             pieChartData.add(slice);
        }


        if (pieChartData.isEmpty()) {
            // Return null or an empty chart if no valid data was found
            return null;
        }

        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("Pie Chart: " + valueColumn + " by " + labelColumn);

        // Ensure labels are visible to show the names (which now include values/percentages)
        pieChart.setLabelsVisible(true);

        return pieChart;
    }
}