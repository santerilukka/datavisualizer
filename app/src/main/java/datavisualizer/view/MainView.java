package datavisualizer.view;

import datavisualizer.controller.AppController;
import datavisualizer.model.dataset.DataSet;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * The main view of the application, containing the chart display and controls.
 */
public class MainView {

    @FXML private BorderPane mainPane;
    @FXML private VBox columnSelectionPanel;
    @FXML private ColumnSelectionPanel columnSelectionPanelController;

    private ChartView chartView;
    private AppController appController;
    private VBox startScreen;

    /**
     * Initializes the main view. This method is automatically called after the FXML file has been loaded.
     */
    @FXML
    public void initialize() {
        // Initialize ChartView but don't add it to the center yet
        chartView = new ChartView();

        // Create the start screen using the helper method
        startScreen = createStartScreen();

        // Set the start screen as the initial center content
        mainPane.setCenter(startScreen);

        // Connect ColumnSelectionPanel to ChartView (needed later)
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
        if (columnSelectionPanelController != null) {
            columnSelectionPanelController.setAppController(appController);
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
     * Handles the action of closing the current data file.
     */
    @FXML
    private void closeFile() {
        if (appController != null) {
            appController.closeCurrentFile();
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
        if (dataSet != null) {
            // Data loaded successfully, show the chart view
            mainPane.setCenter(chartView.getChartContainer()); // Switch center to chart view

            List<String> columnNames = dataSet.getColumnNames();
            if (columnSelectionPanelController != null) {
                columnSelectionPanelController.populateColumns(columnNames);
            }
            // Clear any existing chart before new selections are made
            chartView.clearChart();

        } else {
            // Data loading failed or was cancelled, show start screen
            mainPane.setCenter(startScreen); // Switch back to start screen
            if (columnSelectionPanelController != null) {
                columnSelectionPanelController.populateColumns(null);
            }
            // Also clear the chart view state if going back to start screen
            if (chartView != null) {
                chartView.clearChart();
            }
        }
    }

    /**
     * Creates and configures the VBox used as the start screen.
     *
     * @return The configured VBox for the start screen.
     */
    private VBox createStartScreen() {
        VBox screen = new VBox(20); // VBox with spacing
        screen.setAlignment(Pos.CENTER);
        screen.setPadding(new Insets(20));

        Label welcomeLabel = new Label("Welcome to DataVisualizer!");
        welcomeLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Label instructionLabel = new Label("Open a CSV or JSON data file to start");
        instructionLabel.setWrapText(true);
        instructionLabel.setAlignment(Pos.CENTER);

        Button openFileButton = new Button("Open Data File");
        openFileButton.setOnAction(event -> openFile()); // Reuse the existing openFile method

        screen.getChildren().addAll(welcomeLabel, instructionLabel, openFileButton);
        return screen;
    }
}