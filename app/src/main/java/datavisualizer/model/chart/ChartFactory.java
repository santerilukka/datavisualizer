// DataVisualizerFX/src/main/java/datavisualizerfx/model/chart/ChartFactory.java
package datavisualizer.model.chart;

import datavisualizer.model.dataset.DataSet;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.Chart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.List;

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
        NumberAxis yAxis = new NumberAxis();
        LineChart<String, Number> lineChart = new LineChart<>(new javafx.scene.chart.CategoryAxis(), yAxis);
        lineChart.setTitle("Line Chart");

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
                            // Handle non-numeric
                            System.err.println("Skipping non-numeric value in column " + yColumn + ": " + yData.get(i));
                        }
                    }
                    lineChart.getData().add(series);
                }
            }
        }
        return lineChart;
    }
}