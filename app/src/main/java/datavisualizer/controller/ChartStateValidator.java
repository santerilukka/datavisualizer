package datavisualizer.controller;

import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.chart.ChartType;
import datavisualizer.view.ErrorDisplayView;

/**
 * Handles validation logic related to chart state updates and user input.
 */
public class ChartStateValidator {

    private final DataSet dataSet;
    private final ErrorDisplayView errorDisplay;

    /**
     * Constructs a validator.
     *
     * @param dataSet      The current dataset (can be null if no data loaded).
     * @param errorDisplay The view component responsible for showing errors.
     */
    public ChartStateValidator(DataSet dataSet, ErrorDisplayView errorDisplay) {
        this.dataSet = dataSet;
        this.errorDisplay = errorDisplay;

        if (this.errorDisplay == null) {
            // Log or throw an exception, as error display is crucial for feedback
            System.err.println("ChartStateValidator Error: ErrorDisplayView cannot be null.");
            // Optionally throw new IllegalArgumentException("ErrorDisplayView cannot be null.");
        }
    }

    /**
     * Validates the parameters for a chart update request.
     * Displays errors using the provided ErrorDisplayView.
     *
     * @param requestedType The requested chart type.
     * @param requestedXCol The requested X-axis column.
     * @param requestedYCol The requested Y-axis column.
     * @return true if the request is valid, false otherwise.
     */
    public boolean validateUpdateRequest(ChartType requestedType, String requestedXCol, String requestedYCol) {
        if (errorDisplay == null) return false; // Cannot proceed without error display
        errorDisplay.clearErrors();

        if (dataSet == null) {
            System.err.println("Validation Error: Cannot update chart: No data loaded.");
            // Optionally show a general error if needed: errorDisplay.showGeneralError("No data loaded.");
            return false;
        }

        boolean valid = true;
        if (requestedType == null) {
            // This usually indicates a programming error if the ComboBox allows null selection
            System.err.println("Validation Error: ChartType is null.");
            valid = false;
        }
        if (requestedXCol == null) {
            errorDisplay.showXAxisError("Please select a column for the X-Axis.");
            valid = false;
        }
        if (requestedYCol == null) {
            errorDisplay.showYAxisError("Please select a column for the Y-Axis.");
            valid = false;
        }
        // Check for same axes only if both are selected
        if (requestedXCol != null && requestedYCol != null && requestedXCol.equals(requestedYCol)) {
            errorDisplay.showYAxisError("X and Y axes cannot be the same.");
            valid = false;
        }

        return valid;
    }

    /**
     * Validates the parameters for an axis swap request based on panel selections.
     * Displays errors using the provided ErrorDisplayView.
     *
     * @param panelX The X-axis column selected in the panel.
     * @param panelY The Y-axis column selected in the panel.
     * @return true if the swap request is valid, false otherwise.
     */
    public boolean validateSwapRequest(String panelX, String panelY) {
        if (errorDisplay == null) return false; // Cannot proceed without error display
        errorDisplay.clearErrors();

        if (dataSet == null) {
            System.err.println("Validation Error: Cannot swap axes: No data loaded.");
            // Optionally show a general error: errorDisplay.showGeneralError("No data loaded.");
            return false;
        }

        boolean valid = true;
        if (panelX == null) {
            errorDisplay.showXAxisError("Select both X and Y axes to swap.");
            valid = false;
        }
        if (panelY == null) {
            errorDisplay.showYAxisError("Select both X and Y axes to swap.");
            valid = false;
        }
        // Check for same axes only if both are selected
        if (panelX != null && panelY != null && panelX.equals(panelY)) {
            // Show error on Y axis for consistency with update validation
            errorDisplay.showYAxisError("X and Y axes cannot be the same.");
            valid = false;
        }

        return valid;
    }
}