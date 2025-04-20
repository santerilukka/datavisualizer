package datavisualizer.model.chart;

import datavisualizer.model.ChartType;
import datavisualizer.model.dataset.DataSet;

public class ChartFactory {
    public static Chart createChart(ChartType type, DataSet dataset) {
        switch (type) {
            case BAR:
                return new BarChartData(dataset);
            case LINE:
                return new LineChartData(dataset);
            default:
                throw new IllegalArgumentException("Unsupported chart type: " + type);
        }
    }
}