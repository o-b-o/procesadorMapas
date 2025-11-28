package com.proyecto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;
/**
 * Clase de utilidad para escribir datos en un archivo para ser cargado por octave
 */
public class OctaveWriter {

    /**
     * aniade una matriz 2d a un archivo. La matriz esta construida de un arreglo 1d y dimensiones especificadas
     * @param filepath       camino del script de matlab (e.g., "./output/results.m").
     * @param variableName   nombre de la variable matriz en el script (e.g., "map_0001").
     * @param data           la informacion que sera convertida en la matriz de salida
     * @param numRows        numero de renglones para matriz salida
     * @param numCols        numero de columnas para matriz salida
     * @throws IOException            si hay errores escribiendo al archivo
     * @throws IllegalArgumentException si el tamanio de el arreglo de datos no tiene tamanio numRows*numCols
     */
    public void escribeMatriz(String filepath, String variableName, int[] data, int numRows, int numCols) throws IOException {

        // valida la entrada

        if (data.length != numRows * numCols) {
            throw new IllegalArgumentException(
                "Data size (" + data.length + ") does not match matrix dimensions (" + numRows + "x" + numCols + ")."
            );
        }

        //abre el archivo en modo append
        // el argumento true le dice a FileWriter que no sobreescriba, que aniada
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));

        // escribir la matriz
        writer.write(variableName + " = [");

        int dataIndex = 0; // un contador para el arreglo 1d

        // renglones
        for (int r = 0; r < numRows; r++) {
            // columnas
            for (int c = 0; c < numCols; c++) {
                writer.write(String.valueOf(data[dataIndex++]));

                // aniade espacio entre numeros, pero no despues del ultimo en renglon
                if (c < numCols - 1) {
                    writer.write(" ");
                }
            }
            // aniade punto y coma entre renglones, pero no despues del ultimo
            if (r < numRows - 1) {
                writer.write("; ");
            }
        }


        writer.write("];");
        writer.newLine();
        writer.newLine(); 

        // cerrar el ecritor
        // asegurar que se escriba el archivo
        writer.close();
    }

    /**
     * aniade los comandos de visualizacion finales a un archivo de octave
     */
    public void aniadeComandosVisualizacion(String filepath, String firstMapName, double wavenumber) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true));
        writer.write("# Comandos de Visualizacion\n");
        writer.write("figure;\n");
        writer.write(String.format("imagesc(%s);\n", firstMapName));
        writer.write(String.format("title('Intensidad en la onda ~%.2f cm^{-1}');\n", wavenumber));
        writer.write("colorbar;\n");
        writer.write("axis equal tight;\n"); 
        writer.close();
    }
}
