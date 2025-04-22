package datavisualizer.model.chart;

import datavisualizer.model.dataset.DataSet;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.Node;

public class BarChartData extends Chart {
    public BarChartData(DataSet dataset) {
        super(dataset);
    }

    @Override
    public Node render() {
        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String, Number> barChart = new BarChart<>(xAxis, yAxis);

        // Populate chart with data from the dataset
        // Example: Add series and data points here

        return barChart;
    }
}