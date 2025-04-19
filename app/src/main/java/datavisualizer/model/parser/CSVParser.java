package datavisualizer.model.parser;

import datavisualizer.model.dataset.DataSet;

import java.io.*;
import java.util.*;

public class CSVParser implements DataParser {

    @Override
    public DataSet parse(File file) {
        List<String> columns = new ArrayList<>();
        List<Map<String, String>> rows = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            // Read the first line to get the column names
            if ((line = reader.readLine()) != null) {
                columns = Arrays.asList(line.split(","));
            }

            // Read the rest of the file
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",", -1);
                Map<String, String> row = new LinkedHashMap<>();
                for (int i = 0; i < columns.size(); i++) {
                    row.put(columns.get(i), i < values.length ? values[i] : "");
                }
                rows.add(row);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return new DataSet(columns, rows);
    }
}
