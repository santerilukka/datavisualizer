package datavisualizer.model;

import datavisualizer.model.chart.ChartType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class to hold the current state of the chart configuration.
 */
public class ChartStateModel {

    private ChartType chartType = ChartType.BAR; // Default chart type
    private String xColumn = null;
    private List<String> yColumns = new ArrayList<>();

    /**
     * Gets the currently selected chart type.
     *
     * @return The current ChartType.
     */
    public ChartType getChartType() {
        return chartType;
    }

    /**
     * Gets the currently selected X-axis column.
     *
     * @return The name of the X-axis column, or null if none selected.
     */
    public String getXColumn() {
        return xColumn;
    }

    /**
     * Gets the currently selected Y-axis columns.
     *
     * @return An unmodifiable list of the Y-axis column names.
     */
    public List<String> getYColumns() {
        // Return immutable copy to prevent external modification
        return Collections.unmodifiableList(yColumns);
    }

    /**
     * Updates the chart state.
     *
     * @param type The new chart type.
     * @param xCol The new X-axis column.
     * @param yCol The new Y-axis column (single selection assumed for simplicity).
     *             If multiple Y columns are needed later, this method signature should change.
     */
    public void updateState(ChartType type, String xCol, String yCol) {
        this.chartType = (type != null) ? type : ChartType.BAR; // Default if null
        this.xColumn = xCol;
        // Assuming single Y column selection for now, matching AppController logic
        this.yColumns = (yCol != null) ? new ArrayList<>(List.of(yCol)) : new ArrayList<>();

        // TODO: Implement notification mechanism (e.g., PropertyChangeSupport or JavaFX Properties)
        // if observers need to react to state changes.
    }

     /**
     * Updates the chart state with multiple Y columns.
     *
     * @param type The new chart type.
     * @param xCol The new X-axis column.
     * @param yCols The new list of Y-axis columns.
     */
    public void updateState(ChartType type, String xCol, List<String> yCols) {
        this.chartType = (type != null) ? type : ChartType.BAR; // Default if null
        this.xColumn = xCol;
        this.yColumns = (yCols != null) ? new ArrayList<>(yCols) : new ArrayList<>();

        // TODO: Implement notification mechanism
    }


    /**
     * Resets the chart state to default values.
     */
    public void resetState() {
        this.chartType = ChartType.BAR;
        this.xColumn = null;
        this.yColumns.clear();
        // TODO: Notify observers if implemented
    }
}