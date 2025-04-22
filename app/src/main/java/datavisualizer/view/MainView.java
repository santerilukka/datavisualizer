// DataVisualizerFX/src/main/java/datavisualizerfx/view/MainView.java
package datavisualizer.view;

import datavisualizer.controller.AppController;
import datavisualizer.model.dataset.DataSet;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

/**
 * The main view of the application, containing the chart display and controls.
 */
public class MainView {

    @FXML
    private BorderPane mainPane;

    private ChartView chartView;
    private ColumnSelectionPanel columnSelectionPanel;
    private AppController appController;

    /**
     * Initializes the main view. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        chartView = new ChartView();
        columnSelectionPanel = new ColumnSelectionPanel();

        // Add the chart view to the center and column selection to the right
        mainPane.setCenter(chartView.getChartContainer());
        mainPane.setRight(columnSelectionPanel.getSelectionPanel());
    }

    /**
     * Sets the application controller for this view.
     *
     * @param appController The application controller.
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
        columnSelectionPanel.setAppController(appController); // Pass controller to column selection
    }

    /**
     * Handles the action of opening a data file.
     */
    @FXML
    private void openFile() {
        if (appController != null) {
            appController.openFile();
        }
    }

    /**
     * Handles the action of exiting the application.
     */
    @FXML
    private void exitApplication() {
        Platform.exit();
    }

    /**
     * Handles the undo action.
     */
    @FXML
    private void undoAction() {
        if (appController != null) {
            appController.getCommandManager().undo();
        }
    }

    /**
     * Handles the redo action.
     */
    @FXML
    private void redoAction() {
        if (appController != null) {
            appController.getCommandManager().redo();
        }
    }

    /**
     * Shows the column selection panel.
     */
    @FXML
    private void showColumnSelectionPanel() {
        // You might want to control the visibility of the panel here
        if (mainPane.getRight() == null) {
            mainPane.setRight(columnSelectionPanel.getSelectionPanel());
        } else {
            mainPane.setRight(null); // Or some other way to hide it
        }
    }

    /**
     * Displays the data set in the chart view and updates the column selection panel.
     *
     * @param dataSet The dataset to display.
     */
    public void displayDataSet(DataSet dataSet) {
        if (dataSet != null) {
            chartView.updateChart(null, null, dataSet.getColumnNames()); // Initial display
            columnSelectionPanel.populateColumns(dataSet.getColumnNames());
        }
    }
}