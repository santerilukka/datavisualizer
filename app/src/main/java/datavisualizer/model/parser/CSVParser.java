package datavisualizer.model.parser;

import datavisualizer.model.dataset.DataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVParser implements DataParser {
    @Override
    public DataSet parse(File file) {
        DataSet dataSet = new DataSet();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            // Read headers
            String headerLine = reader.readLine();
            if (headerLine != null) {
                List<String> headers = Arrays.asList(headerLine.split(","));
                dataSet.setHeaders(headers);
                
                // Read data rows
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] values = line.split(",");
                    Map<String, String> row = new HashMap<>();
                    
                    for (int i = 0; i < Math.min(headers.size(), values.length); i++) {
                        row.put(headers.get(i), values[i]);
                    }
                    
                    dataSet.addRow(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return dataSet;
    }
}