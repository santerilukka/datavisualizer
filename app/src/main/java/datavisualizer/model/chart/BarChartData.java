// DataVisualizerFX/src/main/java/datavisualizerfx/model/chart/BarChartData.java
package datavisualizer.model.chart;

import javafx.scene.chart.XYChart;

import java.util.List;

/**
 * Represents the data needed for a bar chart.
 * This class might be used for more complex bar chart configurations in the future.
 */
public class BarChartData {
    private String name;
    private List<XYChart.Data<String, Number>> data;

    /**
     * Constructs a new BarChartData.
     *
     * @param name The name of the data series.
     * @param data The list of data points.
     */
    public BarChartData(String name, List<XYChart.Data<String, Number>> data) {
        this.name = name;
        this.data = data;
    }

    /**
     * Gets the name of the data series.
     *
     * @return The name.
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the list of data points.
     *
     * @return The list of data points.
     */
    public List<XYChart.Data<String, Number>> getData() {
        return data;
    }
}