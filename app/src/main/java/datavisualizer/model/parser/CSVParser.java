// DataVisualizerFX/src/main/java/datavisualizerfx/model/parser/CSVParser.java
package datavisualizer.model.parser;

import datavisualizer.model.dataset.DataSet;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation of DataParser for parsing CSV files.
 */
public class CSVParser implements DataParser {

    /**
     * Parses a CSV file and returns a DataSet.
     * Assumes the first row contains column headers.
     *
     * @param file The CSV file to parse.
     * @return A DataSet containing the parsed data.
     * @throws IOException If an error occurs during file reading.
     */
    @Override
    public DataSet parse(File file) throws IOException {
        List<String> columnNames = new ArrayList<>();
        List<Map<String, Object>> data = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String headerLine = reader.readLine();
            if (headerLine != null) {
                columnNames.addAll(Arrays.asList(headerLine.split(",")));
            }

            String dataLine;
            while ((dataLine = reader.readLine()) != null) {
                String[] values = dataLine.split(",");
                if (values.length == columnNames.size()) {
                    Map<String, Object> row = new HashMap<>();
                    for (int i = 0; i < columnNames.size(); i++) {
                        row.put(columnNames.get(i), values[i].trim());
                    }
                    data.add(row);
                }
            }
        }
        return new DataSet(columnNames, data);
    }
}