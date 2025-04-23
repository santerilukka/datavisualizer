package datavisualizer.model.command;

import datavisualizer.model.chart.ChartType;
import datavisualizer.view.ChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Command to update the set of visible Y-columns in the chart.
 */
public class UpdateVisibleColumnsCommand implements Command {

    private final ChartView chartView;
    private final ChartType chartType; // Need chart type for update
    private final String xColumn;      // Need X column for update
    private final List<String> newVisibleColumns;
    private final List<String> previousVisibleColumns;

    /**
     * Constructs an UpdateVisibleColumnsCommand.
     *
     * @param chartView             The chart view to update.
     * @param chartType             The current chart type.
     * @param xColumn               The current X-axis column.
     * @param newVisibleColumns     The target list of visible Y-columns.
     * @param previousVisibleColumns The list of Y-columns that were visible before this command.
     */
    public UpdateVisibleColumnsCommand(ChartView chartView, ChartType chartType, String xColumn, List<String> newVisibleColumns, List<String> previousVisibleColumns) {
        this.chartView = chartView;
        this.chartType = chartType;
        this.xColumn = xColumn;
        this.newVisibleColumns = new ArrayList<>(newVisibleColumns);
        this.previousVisibleColumns = new ArrayList<>(previousVisibleColumns);
    }

    /**
     * Executes the command to update the visible columns.
     */
    @Override
    public void execute() {
        if (chartView != null && chartType != null && xColumn != null) {
            chartView.updateChart(chartType, xColumn, newVisibleColumns);
        }
    }

    /**
     * Undoes the command, reverting to the previous set of visible columns.
     */
    @Override
    public void undo() {
        if (chartView != null && chartType != null && xColumn != null) {
            chartView.updateChart(chartType, xColumn, previousVisibleColumns);
        }
    }
}