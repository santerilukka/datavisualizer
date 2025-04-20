package datavisualizer.model.chart;

import datavisualizer.model.dataset.DataSet;
import javafx.scene.Node;

public abstract class Chart {
    protected DataSet dataset;

    public Chart(DataSet dataset) {
        this.dataset = dataset;
    }

    public abstract Node render();
}