package datavisualizer.model.dataset;

import java.util.List;
import java.util.Map;

public class DataSet {
    private List<String> columns;
    private List<Map<String, String>> rows;

    public DataSet(List<String> columns, List<Map<String, String>> rows) {
        this.columns = columns;
        this.rows = rows;
    }

    public List<String> getColumns() {
        return columns;
    }

    public List<Map<String, String>> getRows() {
        return rows;
    }

    public void printPreview() {
        System.out.println("Columns: " + columns);
        for (int i = 0; i < Math.min(5, rows.size()); i++) {
            System.out.println(rows.get(i));
        }
    }
}
