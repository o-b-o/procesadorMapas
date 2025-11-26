package com.proyecto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * Represents a single, self-contained analysis task for one chunk file,
 * designed to be executed by a thread in an ExecutorService.
 * It returns a list of the Octave variable names it successfully wrote.
 */
public class AnalisisTask implements Callable<List<String>> {

    // --- All the inputs a single task needs to do its job ---
    private final File chunkFile;
    private final DataGeometry config;
    private final String cellLineFilter;
    private final String qualityFilter;
    private final ProcesoAgregarRangoOnda proceso;
    private final String octaveFilename;
    private final int mapStartIndex;

    public AnalisisTask(File chunkFile, DataGeometry config, String cellLineFilter, String qualityFilter, ProcesoAgregarRangoOnda proceso, String octaveFilename, int startIndex) {
        this.chunkFile = chunkFile;
        this.config = config;
        this.cellLineFilter = cellLineFilter;
        this.qualityFilter = qualityFilter;
        this.proceso = proceso;
        this.octaveFilename = octaveFilename;
        this.mapStartIndex = startIndex;
    }

    /**
     * This is the main method that will be executed by a worker thread.
     */

// Inside AnalisisTask.java

/**
 * This is the main method that will be executed by a worker thread.
 * This version is memory-efficient and uses a streaming approach.
 */
@Override
public List<String> call() throws Exception {
    System.out.printf("Thread '%s' iniciando trabajo en: %s\n", Thread.currentThread().getName(), chunkFile.getName());

    OctaveWriter octaveWriter = new OctaveWriter();
    List<String> variablesEscritas = new ArrayList<>();
    
    // Use a try-with-resources block to ensure the reader is always closed.
    try (BufferedReader reader = new BufferedReader(new FileReader(this.chunkFile))) {
        String line;
        String currentMapName = "nombre_desconocido";
        List<String[]> rowsForOneMap = new ArrayList<>(this.config.getLongitudY()); // Pre-size the list for efficiency
        int mapCounterInFile = 0;

        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("# DATA FROM:")) {
                // We found a name tag for the next block of data.
                currentMapName = line.replace("# DATA FROM:", "").trim();
            } else if (!line.startsWith("#") && !line.trim().isEmpty()) {
                // This is a data line. Add it to our temporary list.
                rowsForOneMap.add(line.split("\t"));

                // Check if we have collected enough rows to form a complete map.
                if (rowsForOneMap.size() == this.config.getLongitudY()) {
                    
                    // --- We have a full map! Time to process it. ---
                    
                    // 1. FILTER: Check the name BEFORE parsing the data.
                    boolean pasaFiltroLinea = this.cellLineFilter.isEmpty() || currentMapName.contains(this.cellLineFilter);
                    boolean pasaFiltroCalidad = this.qualityFilter.isEmpty() || currentMapName.contains(this.qualityFilter);

                    if (pasaFiltroLinea && pasaFiltroCalidad) {
                        
                        // 2. PARSE: Only parse the data if it passed the filter.
                        // We use the existing, trusted helper from DataLoader.
                        // NOTE: This requires making `parsearFilasAMapa` public and static in DataLoader.
                        int mapGlobalIndex = this.mapStartIndex + mapCounterInFile;
                        MapaParticular mapaActual = DataLoader.parsearFilasAMapa(rowsForOneMap, mapGlobalIndex);
                        
                        // 3. PROCESS: Run the analysis on the parsed map.
                        MapaParticular mapaProcesado = this.proceso.procesar(mapaActual);

                        // 4. WRITE: Write the result to the shared output file.
                        String variableName = String.format("map_%04d", mapaProcesado.indice);
                        int[] intensityData = extractIntensityDataFromMap(mapaProcesado);
                        
                        synchronized (octaveWriter) {
                            octaveWriter.writeMatrix(
                                this.octaveFilename,
                                variableName,
                                intensityData,
                                this.config.especPorLadoMapa,
                                this.config.especPorLadoMapa
                            );
                        }
                        variablesEscritas.add(variableName);
                    }
                    
                    // 5. "FORGET": Clear the list to free memory for the next map.
                    rowsForOneMap.clear();
                    mapCounterInFile++;
                }
            }
        } // End of while loop
    }
    
    System.out.printf("Thread '%s' terminó el trabajo en: %s. Escribió %d mapas.\n", 
                      Thread.currentThread().getName(), chunkFile.getName(), variablesEscritas.size());
    
    return variablesEscritas;
}
    
    public List<String> call2() throws Exception {
        // This logic is essentially the body of the 'for' loop from the sequential version.
        
        System.out.printf("Thread '%s' iniciando trabajo en: %s\n", Thread.currentThread().getName(), chunkFile.getName());

        DataLoader dataLoader = new DataLoader();
        OctaveWriter octaveWriter = new OctaveWriter();
        List<String> variablesEscritas = new ArrayList<>();

        // 1. READ the chunk file for this task
        List<MapaConNombre> mapasConNombre = dataLoader.leerConjuntoDeArchivoConNombres(
            this.chunkFile,
            this.config.getLongitudY(),
            this.mapStartIndex
        );

        // 2. FILTER AND PROCESS
        for (MapaConNombre mapaWrapper : mapasConNombre) {
            String filename = mapaWrapper.originalFilename;
            MapaParticular mapaActual = mapaWrapper.mapa;

            boolean pasaFiltroLinea = this.cellLineFilter.isEmpty() || filename.contains(this.cellLineFilter);
            boolean pasaFiltroCalidad = this.qualityFilter.isEmpty() || filename.contains(this.qualityFilter);

            if (pasaFiltroLinea && pasaFiltroCalidad) {
                // 3. PROCESS the map that passed the filter
                MapaParticular mapaProcesado = this.proceso.procesar(mapaActual);

                // 4. WRITE the result
                String variableName = String.format("map_%04d", mapaProcesado.indice);
                int[] intensityData = extractIntensityDataFromMap(mapaProcesado); // We'll need this helper
                
                // IMPORTANT: The writeMatrix method needs to be thread-safe.
                // Using 'synchronized' ensures only one thread can write to the file at a time,
                // preventing the output from getting garbled.
                synchronized (octaveWriter) {
                    octaveWriter.writeMatrix(
                        this.octaveFilename,
                        variableName,
                        intensityData,
                        this.config.especPorLadoMapa,
                        this.config.especPorLadoMapa
                    );
                }
                variablesEscritas.add(variableName);
            }
        }
        
        System.out.printf("Thread '%s' terminó el trabajo en: %s. Escribió %d mapas.\n", 
                          Thread.currentThread().getName(), chunkFile.getName(), variablesEscritas.size());
        
        return variablesEscritas;
    }

    // This helper method needs to be accessible to the task.
    // We can make it a static method here.
    private static int[] extractIntensityDataFromMap(MapaParticular mapa) {
        VecIntensidades[] espectros = mapa.datos.ejeY;
        int[] intensityData = new int[espectros.length];
        for (int i = 0; i < espectros.length; i++) {
            for (int intensity : espectros[i].getVecIntensidades()) {
                if (intensity != 0) {
                    intensityData[i] = intensity;
                    break;
                }
            }
        }
        return intensityData;
    }
}
