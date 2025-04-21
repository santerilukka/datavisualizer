package datavisualizer.view;

import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class ChartView {
    private StackPane chartArea;

    public ChartView() {
        chartArea = new StackPane();
        chartArea.setPrefSize(600, 400);

        // Default message when no data is loaded
        Text placeholderText = new Text("Open a CSV file to display chart");
        chartArea.getChildren().add(placeholderText);
    }

    public void displayChart(Node chartNode) {
        chartArea.getChildren().clear();
        chartArea.getChildren().add(chartNode);
    }

    public Node getNode() {
        return chartArea;
    }
}