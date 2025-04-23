// DataVisualizerFX/src/main/java/datavisualizerfx/model/parser/JSONParser.java
package datavisualizer.model.parser;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import datavisualizer.model.dataset.DataSet;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

/**
 * Implementation of DataParser for parsing JSON files.
 * Assumes the JSON structure is a list of objects, where each object represents a row
 * and the keys are the column names.
 */
public class JSONParser implements DataParser {

    private final Gson gson = new Gson();

    /**
     * Parses a JSON file and returns a DataSet.
     *
     * @param file The JSON file to parse.
     * @return A DataSet containing the parsed data.
     * @throws IOException If an error occurs during file reading or parsing.
     */
    @Override
    public DataSet parse(File file) throws IOException {
        try (FileReader reader = new FileReader(file)) {
            Type type = new TypeToken<List<Map<String, Object>>>() {}.getType();
            List<Map<String, Object>> data = gson.fromJson(reader, type);

            if (data != null && !data.isEmpty()) {
                List<String> columnNames = new ArrayList<>(data.get(0).keySet());
                return new DataSet(columnNames, data);
            } else {
                return new DataSet(new ArrayList<>(), new ArrayList<>());
            }
        }
    }
}