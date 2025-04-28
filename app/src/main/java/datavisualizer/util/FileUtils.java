package datavisualizer.util;

import java.io.File;

/**
 * Utility class for file-related operations.
 */
public class FileUtils {

    /**
     * Gets the file extension of a given file.
     *
     * @param file The file.
     * @return The file extension (e.g., "csv", "json") or an empty string if no extension.
     */
    public static String getFileExtension(File file) {
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        if (dotIndex > 0 && dotIndex < name.length() - 1) {
            return name.substring(dotIndex + 1).toLowerCase();
        }
        return "";
    }
}