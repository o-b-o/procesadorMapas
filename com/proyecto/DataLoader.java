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
// This code goes inside the public class DataLoader { ... }

/**
 * Reads a single file and returns a list of MapaParticular objects, each bundled
 * with its original source filename as extracted from a "# DATA FROM:" header.
 *
 * @param archivo The File object to read (e.g., a chunk file).
 * @param rowsPerMap The number of data rows that constitute a single MapaParticular.
 * @param startIndex The starting index to assign to the maps found in this file.
 * @return A List of MapaConNombre wrappers, each containing a map and its name.
 * @throws IOException If there is an error reading the file.
 */
public List<MapaConNombre> leerConjuntoDeArchivoConNombres(File archivo, int rowsPerMap, int startIndex) throws IOException {
    System.out.printf("Cargando mapas y nombres desde: %s\n", archivo.getName());

    BufferedReader br = new BufferedReader(new FileReader(archivo));
    List<String[]> todasLasFilasDeDatos = new ArrayList<>();
    List<String> nombresDeMapas = new ArrayList<>();
    String line;

    // --- Step 1: Read the entire file, separating data from special headers ---
    while ((line = br.readLine()) != null) {
        if (line.trim().startsWith("# DATA FROM:")) {
            // This is a special header that tells us a new map's data is starting.
            // We extract the name from it and store it.
            String mapName = line.replace("# DATA FROM:", "").trim();
            nombresDeMapas.add(mapName);
        } else if (!line.startsWith("#") && !line.trim().isEmpty()) {
            // This is a data line. Add it to our data list.
            todasLasFilasDeDatos.add(line.split("\t"));
        }
        // Other comments (like column headers) are simply ignored.
    }
    br.close();

    if (todasLasFilasDeDatos.isEmpty()) {
        System.out.println("  No se encontraron filas de datos en el archivo.");
        return new ArrayList<>(); // Return an empty list if there's no data
    }

    // --- Step 2: Parse the data blocks and wrap them with their names ---
    int totalMapasDetectados = todasLasFilasDeDatos.size() / rowsPerMap;
    System.out.printf("  Detectados %d bloques de datos y %d nombres de mapas.\n", totalMapasDetectados, nombresDeMapas.size());

    // It's good practice to check if the numbers match.
    if (totalMapasDetectados != nombresDeMapas.size()) {
        System.err.println("ADVERTENCIA: El número de bloques de datos no coincide con el número de encabezados '# DATA FROM:'. Se usarán nombres por defecto.");
    }
    
    List<MapaConNombre> listaDeMapasEnvueltos = new ArrayList<>();
    for (int i = 0; i < totalMapasDetectados; i++) {
        int fromIndex = i * rowsPerMap;
        int toIndex = fromIndex + rowsPerMap;
        List<String[]> filasDelMapaActual = todasLasFilasDeDatos.subList(fromIndex, toIndex);
        
        int mapIndex = startIndex + i;
        // Get the corresponding name. If we don't have enough names, use a default.
        String mapName = (i < nombresDeMapas.size()) ? nombresDeMapas.get(i) : "mapa_desconocido_" + mapIndex + ".csv";

        // Call the existing helper to create the core MapaParticular object
        MapaParticular mapa = parsearFilasAMapa(filasDelMapaActual, mapIndex);

        // **THE KEY STEP:** Create the wrapper that bundles the map and its name
        MapaConNombre wrapper = new MapaConNombre(mapa, mapName);
        listaDeMapasEnvueltos.add(wrapper);
    }
    
    return listaDeMapasEnvueltos;
}
    /**
     * PRIVATE HELPER METHOD: This is the refactored original method.
     * Its only job is to convert a pre-read list of string arrays into one MapaParticular.
     */
    public static MapaParticular parsearFilasAMapa(List<String[]> filasDelMapa, int indice) {
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
