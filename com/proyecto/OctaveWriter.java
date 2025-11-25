package com.proyecto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
/**
 * A utility class for writing data structures into a file
 * formatted as an Octave or MATLAB script.
 */
public class OctaveWriter {

    /**
     * Appends a 2D matrix to a script file. The matrix is constructed from a 1D
     * data array and the specified dimensions.
     *
     * @param filepath       The full path of the output script file (e.g., "./output/results.m").
     * @param variableName   The name of the matrix variable in the script (e.g., "map_0001").
     * @param data           A 1D array of intensity data, which will be shaped into the matrix.
     * @param numRows        The number of rows for the 2D matrix.
     * @param numCols        The number of columns for the 2D matrix.
     * @throws IOException            If there is an error writing to the file.
     * @throws IllegalArgumentException If the size of the data array does not match numRows * numCols.
     */
    public void writeMatrix(String filepath, String variableName, int[] data, int numRows, int numCols) throws IOException {

        // --- Step 1: Validate the inputs ---
        // This is a crucial "guard clause" to prevent errors.
        if (data.length != numRows * numCols) {
            throw new IllegalArgumentException(
                "Data size (" + data.length + ") does not match matrix dimensions (" + numRows + "x" + numCols + ")."
            );
        }

        // --- Step 2: Open the file in APPEND mode ---
        // The 'true' argument tells FileWriter to add to the end of the file, not overwrite it.
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));

        // --- Step 3: Write the matrix definition ---
        writer.write(variableName + " = [");

        int dataIndex = 0; // A separate counter for our 1D data array

        // Outer loop for rows
        for (int r = 0; r < numRows; r++) {
            // Inner loop for columns
            for (int c = 0; c < numCols; c++) {
                writer.write(String.valueOf(data[dataIndex++]));

                // Add a space between numbers, but not after the last one in a row.
                if (c < numCols - 1) {
                    writer.write(" ");
                }
            }
            // Add a semicolon between rows, but not after the very last row.
            if (r < numRows - 1) {
                writer.write("; ");
            }
        }

        // Write the closing bracket and semicolon for the command.
        writer.write("];");
        writer.newLine();
        writer.newLine(); // Add an extra blank line to separate matrix definitions.

        // --- Step 4: Close the file resource ---
        // This is essential to ensure the data is flushed from the buffer to the disk.
        writer.close();
    }

    /**
     * Appends a final block of commands to the script to visualize the data.
     *
     * @param filepath The path to the script file.
     * @param firstMapName The name of the first map's variable to use for the example plot.
     * @param wavenumber The wavenumber that was analyzed.
     * @throws IOException If there is an error writing to the file.
     */

// Add these two new methods to your existing OctaveWriter.java class

/**
 * Appends commands to stack a series of 2D slice matrices into a single 3D matrix.
 * @param filepath The path to the script file.
 * @param volumeName The name of the final 3D matrix variable.
 * @param sliceNames A list of the 2D matrix variable names to stack.
 * @throws IOException
 */
public void append3DStackingCommands(String filepath, String volumeName, List<String> sliceNames) throws IOException {
    if (sliceNames.isEmpty()) return;

    BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));
    writer.write("# --- Ensamblaje de Datos 3D ---\n");
    // The 'cat' command concatenates arrays along a specified dimension.
    // Dimension 3 is the 'depth' or 'slice' dimension.
    writer.write(volumeName + " = cat(3");
    for (String name : sliceNames) {
        writer.write(", " + name);
    }
    writer.write(");\n");

    // Optional: Clear the original slice variables to save memory in Octave
    writer.write("clear");
    for (String name : sliceNames) {
        writer.write(" " + name);
    }
    writer.write(";\n\n");
    writer.close();
}

/**
 * Appends commands to visualize a 3D volumetric matrix.
 * @param filepath The path to the script file.
 * @param volumeName The name of the 3D matrix variable to visualize.
 * @throws IOException
 */
public void append3DVisualizationCommands(String filepath, String volumeName) throws IOException {
    BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));
    writer.write("# --- Comandos de Visualización 3D ---\n");
    writer.write("figure;\n");
    writer.write("title('Visualización Volumétrica');\n");
    
    // Define some slice points (e.g., a quarter, half, and three-quarters of the way through)
    writer.write(String.format("xslice = [round(size(%s, 2) / 2)];\n", volumeName)); // A slice through the middle X
    writer.write(String.format("yslice = [round(size(%s, 1) / 2)];\n", volumeName)); // A slice through the middle Y
    writer.write(String.format("zslice = [round(size(%s, 3) / 4), round(size(%s, 3) / 2)];\n", volumeName, volumeName)); // Two slices through the Z (wavenumber) axis
    
    writer.write(String.format("slice(%s, xslice, yslice, zslice);\n", volumeName));
    writer.write("xlabel('X');\n");
    writer.write("ylabel('Y');\n");
    writer.write("zlabel('Wavenumber Index');\n");
    writer.write("colorbar;\n");
    writer.write("shading interp; % Makes the slices look smooth\n");
    writer.close();
}
    public void appendVisualizationCommands(String filepath, String firstMapName, double wavenumber) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));
        writer.write("# --- Comandos de Visualización ---\n");
        writer.write("figure;\n");
        writer.write(String.format("imagesc(%s);\n", firstMapName));
        writer.write(String.format("title('Intensidad en la onda ~%.2f cm^{-1}');\n", wavenumber));
        writer.write("colorbar;\n");
        writer.write("axis equal tight;\n"); // Makes the pixels square and fits the image
        writer.close();
    }
}
