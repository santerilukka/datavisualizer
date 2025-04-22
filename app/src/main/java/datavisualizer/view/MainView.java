// DataVisualizerFX/src/main/java/datavisualizerfx/view/MainView.java
package datavisualizer.view;

import datavisualizer.controller.AppController;
import datavisualizer.model.dataset.DataSet;
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

        // Add stub views to the main pane (these will be replaced by FXML integration)
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