package datavisualizer.model.dataset;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataSet {
    private List<String> headers;
    private List<Map<String, String>> data;
    
    public DataSet() {
        this.headers = new ArrayList<>();
        this.data = new ArrayList<>();
    }
    
    public List<String> getHeaders() {
        return headers;
    }
    
    public void setHeaders(List<String> headers) {
        this.headers = headers;
    }
    
    public List<Map<String, String>> getData() {
        return data;
    }
    
    public void addRow(Map<String, String> row) {
        this.data.add(row);
    }
    
    public int getRowCount() {
        return data.size();
    }
}