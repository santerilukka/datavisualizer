package datavisualizer.model.command;

import datavisualizer.model.ChartStateModel;
import datavisualizer.model.chart.ChartType;

import java.util.List;
import java.util.ArrayList; // Import ArrayList

/**
 * Command to update the state of the ChartStateModel.
 */
public class UpdateChartStateCommand implements Command { // Changed to public
    private final ChartStateModel model; // Reference to the model

    private final ChartType prevType, newType;
    private final String prevX, newX;
    private final List<String> prevY, newY;

    /**
     * Constructs a command to update the chart state.
     *
     * @param model      The ChartStateModel to operate on.
     * @param prevType   The previous chart type (for undo).
     * @param prevX      The previous X-axis column (for undo).
     * @param prevY      The previous Y-axis columns (for undo).
     * @param newType    The new chart type to set.
     * @param newX       The new X-axis column to set.
     * @param newY       The new Y-axis columns to set.
     */
    public UpdateChartStateCommand(ChartStateModel model,
                                   ChartType prevType, String prevX, List<String> prevY,
                                   ChartType newType, String newX, List<String> newY) {
        this.model = model;
        this.prevType = prevType;
        this.prevX = prevX;
        // Store immutable copies for safety during undo/redo
        this.prevY = (prevY != null) ? List.copyOf(prevY) : List.of();
        this.newType = newType;
        this.newX = newX;
        // Store immutable copies for safety during undo/redo
        this.newY = (newY != null) ? List.copyOf(newY) : List.of();
    }

    @Override
    public void execute() {
        model.updateState(newType, newX, newY); // Only update model
    }

    @Override
    public void undo() {
        model.updateState(prevType, prevX, prevY); // Only update model
    }
}