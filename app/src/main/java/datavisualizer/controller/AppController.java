package datavisualizer.controller;
import datavisualizer.model.dataset.DataSet;
import datavisualizer.view.MainView;

import javafx.stage.Stage;

/**
 * Controller for the main application window.
 * Handles application-level actions
 */
public class AppController {

    private MainView mainView;
    private DataSet dataSet;
    private Stage primaryStage;

    private final CommandManager commandManager = new CommandManager();
    private final FileController fileController = new FileController();

    /**
     * Sets the primary stage of the application.
     *
     * @param primaryStage The primary stage.
     */
    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    /**
     * Handles the action of opening a data file using FileController.
     */
    public void openFile() {
        DataSet loadedDataSet = fileController.loadDataFile(primaryStage);

        if (loadedDataSet != null) {
            this.dataSet = loadedDataSet;
            // Notify the view to update with the new dataset
            if (mainView != null) {
                mainView.displayDataSet(this.dataSet);
                // Reset command history when new data is loaded
                //commandManager.clearHistory();
            }
        } else {
            // Handle case where loading failed or was cancelled (optional: show message)
            System.err.println("Failed to load data file or operation cancelled.");
        }
    }

    /**
     * Sets the main view for this controller.
     *
     * @param mainView The main view instance.
     */
    public void setMainView(MainView mainView) {
        this.mainView = mainView;
    }

    /**
     * Gets the command manager instance.
     *
     * @return The command manager.
     */
    public CommandManager getCommandManager() {
        return commandManager;
    }

    /**
     * Gets the current dataset.
     *
     * @return The current dataset.
     */
    public DataSet getDataSet() {
        return dataSet;
    }

    /**
     * Gets the main view instance.
     *
     * @return The main view.
     */
    public MainView getMainView() {
        return mainView;
    }
}