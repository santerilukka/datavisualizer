package datavisualizer.model.chart;

import datavisualizer.model.dataset.DataSet;
import javafx.scene.Node;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

public class LineChartData extends Chart {
    public LineChartData(DataSet dataSet) {
        super(dataSet);
    }
    
    @Override
    public Node render() {
        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        LineChart<Number, Number> lineChart = new LineChart<>(xAxis, yAxis);
        
        // Add more implementation for populating the chart with dataSet
        
        return lineChart;
    }
}