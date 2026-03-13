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
     * @param caminoArchivo       camino del script de matlab (e.g., "./output/results.m").
     * @param nombreVariable   nombre de la variable matriz en el script (e.g., "map_0001").
     * @param data           la informacion que sera convertida en la matriz de salida
     * @param numRenglones        numero de renglones para matriz salida
     * @param numCols        numero de columnas para matriz salida
     * @throws IOException            si hay errores escribiendo al archivo
     * @throws IllegalArgumentException si el tamanio de el arreglo de datos no tiene tamanio numRenglones*numCols
     */
    public void escribeMatriz(String caminoArchivo, String nombreVariable, int[] data, int numRenglones, int numCols) throws IOException {

        // valida la entrada

        if (data.length != numRenglones * numCols) {
            throw new IllegalArgumentException(
                "Data size (" + data.length + ") does not match matrix dimensions (" + numRenglones + "x" + numCols + ")."
            );
        }

        //abre el archivo en modo append
        // el argumento true le dice a FileWriter que no sobreescriba, que aniada
        BufferedWriter writer = new BufferedWriter(new FileWriter(caminoArchivo, true));

        // escribir la matriz
        writer.write(nombreVariable + " = [");

        int dataIndex = 0; // un contador para el arreglo 1d

        // renglones
        for (int r = 0; r < numRenglones; r++) {
            // columnas
            for (int c = 0; c < numCols; c++) {
                writer.write(String.valueOf(data[dataIndex++]));

                // aniade espacio entre numeros, pero no despues del ultimo en renglon
                if (c < numCols - 1) {
                    writer.write(" ");
                }
            }
            // aniade punto y coma entre renglones, pero no despues del ultimo
            if (r < numRenglones - 1) {
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
    public void aniadeComandosVisualizacion(String caminoArchivo, String nombrePrimerMapa, double numeroDeOnda) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(caminoArchivo, true));
        writer.write("# Comandos de Visualizacion\n");
        writer.write("figure;\n");
        writer.write(String.format("imagesc(%s);\n", nombrePrimerMapa));
        writer.write(String.format("title('Intensidad en la onda ~%.2f cm^{-1}');\n", numeroDeOnda));
        writer.write("colorbar;\n");
        writer.write("axis equal tight;\n"); 
        writer.close();
    }
}
