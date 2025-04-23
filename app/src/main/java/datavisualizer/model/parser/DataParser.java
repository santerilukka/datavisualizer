// DataVisualizerFX/src/main/java/datavisualizerfx/model/parser/DataParser.java
package datavisualizer.model.parser;

import datavisualizer.model.dataset.DataSet;

import java.io.File;
import java.io.IOException;

/**
 * Interface for parsing data from different file formats.
 * Implementations will handle specific formats like CSV and JSON.
 */
public interface DataParser {

    /**
     * Parses data from the given file and returns a DataSet.
     *
     * @param file The file to parse.
     * @return A DataSet containing the parsed data.
     * @throws IOException If an error occurs during file reading or parsing.
     */
    DataSet parse(File file) throws IOException;
}