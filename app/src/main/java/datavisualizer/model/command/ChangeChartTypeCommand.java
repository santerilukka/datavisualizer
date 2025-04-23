// DataVisualizerFX/src/main/java/datavisualizerfx/model/command/ChangeChartTypeCommand.java
package datavisualizer.model.command;

import datavisualizer.model.chart.ChartType;
import datavisualizer.model.dataset.DataSet;
import datavisualizer.view.ChartView;

import java.util.List;

/**
 * Command to change the type of the displayed chart.
 */
public class ChangeChartTypeCommand implements Command {

    private final ChartView chartView;
    private final DataSet dataSet;
    private final ChartType newChartType;
    private final ChartType previousChartType;
    private final String xColumn;
    private final List<String> yColumns;

    /**
     * Constructs a ChangeChartTypeCommand.
     *
     * @param chartView         The chart view to update.
     * @param dataSet           The current dataset.
     * @param newChartType      The new chart type to apply.
     * @param previousChartType The previous chart type.
     * @param xColumn           The column used for the X-axis.
     * @param yColumns          The columns used for the Y-axis.
     */
    public ChangeChartTypeCommand(ChartView chartView, DataSet dataSet, ChartType newChartType, ChartType previousChartType, String xColumn, List<String> yColumns) {
        this.chartView = chartView;
        this.dataSet = dataSet;
        this.newChartType = newChartType;
        this.previousChartType = previousChartType;
        this.xColumn = xColumn;
        this.yColumns = yColumns;
    }

    /**
     * Executes the command to change the chart type.
     */
    @Override
    public void execute() {
        if (chartView != null) {
            chartView.updateChart(newChartType, xColumn, yColumns);
        }
    }

    /**
     * Undoes the command, reverting to the previous chart type.
     */
    @Override
    public void undo() {
        if (chartView != null) {
            chartView.updateChart(previousChartType, xColumn, yColumns);
        }
    }
}