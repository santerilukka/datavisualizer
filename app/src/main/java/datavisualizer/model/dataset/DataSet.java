// DataVisualizerFX/src/main/java/datavisualizerfx/model/dataset/DataSet.java
package datavisualizer.model.dataset;

import java.util.List;
import java.util.Map;

/**
 * Represents a dataset loaded from a file.
 * Contains the data and metadata about the dataset.
 */
public class DataSet {

    private List<String> columnNames;
    private List<Map<String, Object>> data;

    /**
     * Constructs a new DataSet.
     *
     * @param columnNames The names of the columns in the dataset.
     * @param data        The data rows, where each row is a map of column name to value.
     */
    public DataSet(List<String> columnNames, List<Map<String, Object>> data) {
        this.columnNames = columnNames;
        this.data = data;
    }

    /**
     * Gets the list of column names.
     *
     * @return The list of column names.
     */
    public List<String> getColumnNames() {
        return columnNames;
    }

    /**
     * Gets the list of data rows.
     *
     * @return The list of data rows.
     */
    public List<Map<String, Object>> getData() {
        return data;
    }

    /**
     * Gets a specific column of data.
     *
     * @param columnName The name of the column to retrieve.
     * @return A list containing the values of the specified column, or null if the column does not exist.
     */
    public List<Object> getColumnData(String columnName) {
        if (columnNames.contains(columnName)) {
            int columnIndex = columnNames.indexOf(columnName);
            List<Object> columnData = new java.util.ArrayList<>();
            for (Map<String, Object> row : data) {
                columnData.add(row.get(columnName));
            }
            return columnData;
        }
        return null;
    }
}