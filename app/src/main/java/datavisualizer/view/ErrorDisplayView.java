package datavisualizer.view;

import javafx.scene.control.Label;

/**
 * Handles the display and clearing of validation error messages for UI components.
 */
public class ErrorDisplayView {

    private final Label xAxisErrorLabel;
    private final Label yAxisErrorLabel;

    /**
     * Constructs an ErrorDisplayView.
     *
     * @param xAxisErrorLabel The Label component for displaying X-axis errors.
     * @param yAxisErrorLabel The Label component for displaying Y-axis errors.
     */
    public ErrorDisplayView(Label xAxisErrorLabel, Label yAxisErrorLabel) {
        if (xAxisErrorLabel == null || yAxisErrorLabel == null) {
            throw new IllegalArgumentException("Error labels cannot be null.");
        }
        this.xAxisErrorLabel = xAxisErrorLabel;
        this.yAxisErrorLabel = yAxisErrorLabel;
    }

    /**
     * Displays an error message related to the X-axis selection.
     *
     * @param message The error message to display.
     */
    public void showXAxisError(String message) {
        showError(xAxisErrorLabel, message);
    }

    /**
     * Displays an error message related to the Y-axis selection.
     *
     * @param message The error message to display.
     */
    public void showYAxisError(String message) {
        showError(yAxisErrorLabel, message);
    }

    /**
     * Clears any validation error messages shown by this view.
     */
    public void clearErrors() {
        clearError(xAxisErrorLabel);
        clearError(yAxisErrorLabel);
    }

    /**
     * Helper method to show an error message on a specific label.
     * Makes the label visible and managed.
     *
     * @param label   The label to update.
     * @param message The message to display.
     */
    private void showError(Label label, String message) {
        label.setText(message);
        label.setVisible(true);
        label.setManaged(true);
    }

     /**
     * Helper method to clear the error message from a specific label.
     * Makes the label invisible and unmanaged.
     *
     * @param label The label to clear.
     */
    private void clearError(Label label) {
        label.setText("");
        label.setVisible(false);
        label.setManaged(false);
    }
}