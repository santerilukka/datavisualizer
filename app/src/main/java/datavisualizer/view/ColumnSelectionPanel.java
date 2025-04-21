package datavisualizer.view;

import datavisualizer.controller.AppController;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.layout.VBox;

public class ColumnSelectionPanel {
    private VBox panelRoot;
    private ListView<String> columnList;
    private AppController controller;
    
    public ColumnSelectionPanel(AppController controller) {
        this.controller = controller;
        
        panelRoot = new VBox(10);
        panelRoot.setPrefWidth(200);
        
        Label titleLabel = new Label("Select Columns");
        columnList = new ListView<>();
        
        // Initially empty - will be populated when data is loaded
        TitledPane selectionPane = new TitledPane("Available Columns", columnList);
        selectionPane.setCollapsible(false);
        selectionPane.setMaxHeight(Double.MAX_VALUE);
        
        panelRoot.getChildren().addAll(titleLabel, selectionPane);
    }
    
    public void setColumns(String[] columns) {
        columnList.getItems().clear();
        columnList.getItems().addAll(columns);
        
        // Set up selection change listener
        columnList.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    controller.columnSelectionChanged(newSelection);
                }
            }
        );
    }
    
    public Node getNode() {
        return panelRoot;
    }
}