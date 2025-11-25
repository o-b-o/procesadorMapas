package com.proyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    /**
     * Reads a single file, which may contain multiple data blocks (maps), and
     * converts it into a ConjuntoDeMapas.
     *
     * @param archivo The File object to read.
     * @param rowsPerMap The number of rows that constitute a single MapaParticular.
     * @param startIndex The starting index to assign to the maps found in this file.
     * @return A ConjuntoDeMapas containing all the maps extracted from the file.
     * @throws IOException If there is an error reading the file.
     */
    public ConjuntoDeMapas leerConjuntoDeArchivo(File archivo, int rowsPerMap, int startIndex) throws IOException {
        System.out.printf("Cargando conjunto de mapas desde: %s\n", archivo.getName());

        // --- Step 1: Read the ENTIRE file, separating data from comments ---
        // This is the cleaned-up, simplified loop.
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        List<String[]> todasLasFilas = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            // If a line is NOT a comment AND is not empty, it's data.
            if (!line.startsWith("#") && !line.trim().isEmpty()) {
                todasLasFilas.add(line.split("\t"));
            }
            // All other lines (comments, empty lines) are simply ignored.
        }
        br.close();

        if (todasLasFilas.isEmpty()) {
            // Return an empty set if the file has no data
            return new ConjuntoDeMapas(new MapaParticular[0]);
        }

        // --- Step 2: Determine how many maps are in this file ---
        int totalMapasEnArchivo = todasLasFilas.size() / rowsPerMap;
        System.out.printf("  Detectados %d mapas en este archivo (bloques de %d filas).\n", totalMapasEnArchivo, rowsPerMap);
        
        List<MapaParticular> listaDeMapas = new ArrayList<>();

        // --- Step 3: Loop through the data in "blocks" of rowsPerMap ---
        for (int i = 0; i < totalMapasEnArchivo; i++) {
            int fromIndex = i * rowsPerMap;
            int toIndex = fromIndex + rowsPerMap;

            List<String[]> filasDelMapaActual = todasLasFilas.subList(fromIndex, toIndex);
            
            int mapIndex = startIndex + i;

            // Call our helper to do the hard work of parsing these specific rows
            MapaParticular mapa = parsearFilasAMapa(filasDelMapaActual, mapIndex);
            listaDeMapas.add(mapa);
        }
        
        // Convert the list to the array required by the ConjuntoDeMapas constructor
        return new ConjuntoDeMapas(listaDeMapas.toArray(new MapaParticular[0]));
    }

    /**
     * PRIVATE HELPER METHOD: This is the refactored original method.
     * Its only job is to convert a pre-read list of string arrays into one MapaParticular.
     */
    private MapaParticular parsearFilasAMapa(List<String[]> filasDelMapa, int indice) {
        int numRenglones = filasDelMapa.size();
        if (numRenglones == 0) {
            throw new IllegalArgumentException("La lista de filas para parsear está vacía.");
        }
        int numColumnas = filasDelMapa.get(0).length;

        // --- The core parsing logic is unchanged ---
        double[] ondas = new double[numRenglones];
        for (int fila = 0; fila < numRenglones; fila++) {
            ondas[fila] = Double.parseDouble(filasDelMapa.get(fila)[0].trim());
        }
        VecNumeroDeOnda vecOnda = new VecNumeroDeOnda(ondas);

        int numEspectros = numColumnas - 1;
        VecIntensidades[] vectoresIntensidad = new VecIntensidades[numEspectros];

        for (int col = 1; col < numColumnas; col++) {
            int[] intensidades = new int[numRenglones];
            for (int fila = 0; fila < numRenglones; fila++) {
                intensidades[fila] = (int) Double.parseDouble(filasDelMapa.get(fila)[col].trim());
            }
            vectoresIntensidad[col - 1] = new VecIntensidades(intensidades);
        }

        DatosEspectro espectro = new DatosEspectro(vecOnda, vectoresIntensidad);
        
        return new MapaParticular(indice, espectro);
    }
}
