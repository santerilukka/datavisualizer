package datavisualizer.model.parser;

import datavisualizer.model.dataset.DataSet;
import java.io.File;

public interface DataParser {
    DataSet parse(File file);
}
