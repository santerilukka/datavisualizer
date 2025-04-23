// DataVisualizerFX/src/main/java/datavisualizerfx/model/command/HideColumnCommand.java
package datavisualizer.model.command;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.view.ChartView;

import java.util.ArrayList;
import java.util.List;

/**
 * Command to hide specific columns in the data visualization.
 */
public class HideColumnCommand implements Command {

    private final ChartView chartView;
    private final DataSet dataSet;
    private final List<String> columnsToHide;
    private final List<String> previouslyVisibleColumns;

    /**
     * Constructs a HideColumnCommand.
     *
     * @param chartView       The chart view to update.
     * @param dataSet         The current dataset.
     * @param columnsToHide   The names of the columns to hide.
     * @param previouslyVisibleColumns The list of columns that were visible before this command.
     */
    public HideColumnCommand(ChartView chartView, DataSet dataSet, List<String> columnsToHide, List<String> previouslyVisibleColumns) {
        this.chartView = chartView;
        this.dataSet = dataSet;
        this.columnsToHide = new ArrayList<>(columnsToHide);
        this.previouslyVisibleColumns = new ArrayList<>(previouslyVisibleColumns);
    }

    /**
     * Executes the command to hide the specified columns.
     */
    @Override
    public void execute() {
        // Logic to update the chart view to hide the specified columns
        if (chartView != null) {
            chartView.hideColumns(columnsToHide);
        }
    }

    /**
     * Undoes the command, making the previously hidden columns visible again.
     */
    @Override
    public void undo() {
        if (chartView != null) {
            chartView.showColumns(previouslyVisibleColumns);
        }
    }
}