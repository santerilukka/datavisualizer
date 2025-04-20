package datavisualizer.model.chart;

import datavisualizer.model.dataset.DataSet;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;

public class EmptyChart extends Chart {
    public EmptyChart(DataSet dataSet) {
        super(dataSet);
    }
    
    @Override
    public Node render() {
        StackPane pane = new StackPane();
        pane.getChildren().add(new Text("No data available"));
        return pane;
    }
}