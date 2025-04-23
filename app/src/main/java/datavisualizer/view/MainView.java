// DataVisualizerFX/src/main/java/datavisualizerfx/view/MainView.java
package datavisualizer.view;

import datavisualizer.controller.AppController;
import datavisualizer.model.dataset.DataSet;
import datavisualizer.model.chart.ChartType;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * The main view of the application, containing the chart display and controls.
 */
public class MainView {

    @FXML
    private BorderPane mainPane;
    // Inject the root node of the included FXML
    @FXML
    private VBox columnSelectionPanel;

    // Inject the controller of the included FXML
    @FXML
    private ColumnSelectionPanel columnSelectionPanelController;

    private ChartView chartView;
    private AppController appController;
    private DataSet currentDataSet; // To hold the currently loaded dataset

    /**
     * Initializes the main view. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        chartView = new ChartView();
        mainPane.setCenter(chartView.getChartContainer());
    
        // Add this line to connect the ColumnSelectionPanel to the ChartView
        if (columnSelectionPanelController != null) {
            columnSelectionPanelController.setChartView(chartView);
        }
    }

    /**
     * Sets the application controller for this view.
     *
     * @param appController The application controller.
     */
    public void setAppController(AppController appController) {
        this.appController = appController;
        chartView.setAppController(appController); // Pass controller to chart view
        if (columnSelectionPanelController != null) {
            columnSelectionPanelController.setAppController(appController); // Pass controller to column selection
        }
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
     * Shows/hides the column selection panel.
     */
    @FXML
    private void showColumnSelectionPanel() {
        mainPane.setRight(mainPane.getRight() == null ? columnSelectionPanel : null);
    }

    /**
     * Gets the chart view instance.
     *
     * @return The ChartView.
     */
    public ChartView getChartView() {
        return chartView;
    }

    /**
     * Gets the column selection panel instance.
     *
     * @return The ColumnSelectionPanel.
     */
    public ColumnSelectionPanel getColumnSelectionPanel() {
        return columnSelectionPanelController;
    }

    /**
     * Displays the data set in the chart view and updates the column selection panel.
     *
     * @param dataSet The dataset to display.
     */
    public void displayDataSet(DataSet dataSet) {
        this.currentDataSet = dataSet;
        if (dataSet != null) {
            List<String> columnNames = dataSet.getColumnNames();
            if (columnSelectionPanelController != null) {
                columnSelectionPanelController.populateColumns(columnNames);
            }
            // Clear any existing chart and show a prompt or leave it empty
            chartView.clearChart();
            // Optionally, dispay a message to the chart area
            //chartView.showMessage("Please select X and Y axes from the panel.");
        }
    }
}