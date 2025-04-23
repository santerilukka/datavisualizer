// DataVisualizerFX/src/main/java/datavisualizerfx/view/ColumnSelectionPanel.java
package datavisualizer.view;

import datavisualizer.controller.AppController;
import datavisualizer.model.command.HideColumnCommand; // Import Command
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.VBox;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * View that allows the user to select which columns to display in the chart.
 * This will likely contain CheckBoxes for each column.
 */
public class ColumnSelectionPanel {

    @FXML
    private VBox selectionPanel;

    @FXML
    private VBox columnCheckboxes;

    private AppController appController;
    private List<String> allColumnNames = new ArrayList<>();
    private List<String> previouslyVisibleColumns = new ArrayList<>(); // To track visible columns for undo

    /**
     * Initializes the column selection panel. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialization logic if needed
    }

    /**
     * Sets the application controller for this view.
     *
     * @param appController The application controller.
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
    }

    /**
     * Updates the panel with the column names from the dataset.
     * Creates checkboxes for each column.
     *
     * @param columnNames The list of column names to display for selection.
     */
    public void populateColumns(List<String> columnNames) {
        this.allColumnNames = new ArrayList<>(columnNames);
        columnCheckboxes.getChildren().clear();
        for (String columnName : columnNames) {
            CheckBox checkBox = new CheckBox(columnName);
            checkBox.setSelected(true); // Initially select all columns
            columnCheckboxes.getChildren().add(checkBox);
        }
        previouslyVisibleColumns = new ArrayList<>(columnNames); // Initially all are visible
    }

    /**
     * Handles the action when the "Apply Changes" button is clicked.
     */
    @FXML
    private void applyColumnSelection() {
        if (appController != null && appController.getDataSet() != null) {
            List<String> currentVisibleColumns = getSelectedColumns();

            // Identify columns to hide and show
            List<String> columnsToHide = previouslyVisibleColumns.stream()
                    .filter(col -> !currentVisibleColumns.contains(col))
                    .collect(Collectors.toList());
            List<String> columnsToShow = currentVisibleColumns.stream()
                    .filter(col -> !previouslyVisibleColumns.contains(col))
                    .collect(Collectors.toList());

            if (!columnsToHide.isEmpty() || !columnsToShow.isEmpty()) {
                HideColumnCommand command = new HideColumnCommand(
                        appController.getMainView().getChartView(), // Assuming MainView has getChartView()
                        appController.getDataSet(),
                        columnsToHide,
                        previouslyVisibleColumns // Store the state before the change
                );
                appController.getCommandManager().executeCommand(command);
                previouslyVisibleColumns = currentVisibleColumns; // Update the currently visible columns
            }
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("No Data Loaded");
            alert.setHeaderText(null);
            alert.setContentText("Please open a data file first.");
            alert.showAndWait();
        }
    }

    /**
     * Gets the list of currently selected columns from the checkboxes.
     *
     * @return A list of the names of the selected columns.
     */
    public List<String> getSelectedColumns() {
        List<String> selected = new ArrayList<>();
        for (javafx.scene.Node node : columnCheckboxes.getChildren()) {
            if (node instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) node;
                if (checkBox.isSelected()) {
                    selected.add(checkBox.getText());
                }
            }
        }
        return selected;
    }

    /**
     * Gets the container for the column selection panel.
     *
     * @return The VBox that holds the selection controls.
     */
    public VBox getSelectionPanel() {
        return selectionPanel;
    }
}