package datavisualizer.model;

import datavisualizer.model.chart.ChartType;
import datavisualizer.model.dataset.DataSet;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Model class to hold the current state of the chart configuration and data.
 */
public class ChartStateModel {
    private ChartType chartType = ChartType.BAR; // Default chart type
    private String xColumn = null;
    private List<String> yColumns = new ArrayList<>();
    private DataSet currentDataSet = null;

    private final List<ChartStateObserver> observers = new ArrayList<>();

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
     * Gets the current DataSet.
     *
     * @return The current DataSet, or null if none is loaded.
     */
    public DataSet getDataSet() { // Added getter for DataSet
        return currentDataSet;
    }

    /**
     * Sets the current DataSet.
     * Usually called when a new file is loaded.
     * Does not notify observers by itself; typically followed by a state update.
     *
     * @param dataSet The new DataSet, or null to clear.
     */
    public void setDataSet(DataSet dataSet) { // Added setter for DataSet
        this.currentDataSet = dataSet;
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
        this.yColumns = (yCol != null) ? new ArrayList<>(List.of(yCol)) : new ArrayList<>();
        notifyObservers();
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
        notifyObservers();
    }

    /**
     * Resets the chart state and data to default values.
     */
    public void resetState() {
        this.chartType = ChartType.BAR;
        this.xColumn = null;
        this.yColumns.clear();
        this.currentDataSet = null; // Clear DataSet reference
        notifyObservers(); // Notify observers about the reset
    }


    public void addObserver(ChartStateObserver observer) {
        if (observer != null && !observers.contains(observer)) {
            observers.add(observer);
        }
    }

    public void removeObserver(ChartStateObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        // Use a copy to avoid ConcurrentModificationException if observer tries to unregister during notification
        for (ChartStateObserver observer : new ArrayList<>(observers)) {
            observer.chartStateChanged();
        }
    }
}