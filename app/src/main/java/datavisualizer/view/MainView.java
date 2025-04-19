package datavisualizer.view;

import datavisualizer.controller.AppController;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class MainView {
    private AppController controller;
    private BorderPane root;
    private ChartView chartView;
    private ColumnSelectionPanel columnSelectionPanel;

    public MainView(AppController controller) {
        this.controller = controller;
        this.root = new BorderPane();
        setupUI();
    }
    
    private void setupUI() {
        // Create menu bar
        MenuBar menuBar = createMenuBar();
        
        // Create chart area (center)
        chartView = new ChartView();
        
        // Create column selection panel (right)
        columnSelectionPanel = new ColumnSelectionPanel(controller);
        
        // Arrange components in the border pane
        root.setTop(menuBar);
        root.setCenter(chartView.getNode());
        root.setRight(columnSelectionPanel.getNode());
    }
    
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();
        
        // File menu
        Menu fileMenu = new Menu("File");
        MenuItem openItem = new MenuItem("Open CSV File...");
        openItem.setOnAction(e -> controller.loadCSVFile());
        fileMenu.getItems().add(openItem);
        
        // Edit menu
        Menu editMenu = new Menu("Edit");
        MenuItem undoItem = new MenuItem("Undo");
        MenuItem redoItem = new MenuItem("Redo");
        undoItem.setOnAction(e -> controller.undo());
        redoItem.setOnAction(e -> controller.redo());
        editMenu.getItems().addAll(undoItem, redoItem);
        
        menuBar.getMenus().addAll(fileMenu, editMenu);
        return menuBar;
    }
    
    public Parent getRoot() {
        return root;
    }
}