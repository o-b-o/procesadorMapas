package com.proyecto;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Callable;

/**
 * representa una tarea de analisis para un archivo batch
 * diseniada para ser ejecutada por un hilo en un ExecutorService.
 * regresa una lista de los nombres de variable de octave que ha escrito
 */
public class AnalisisTask implements Callable<List<String>> {


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
 * este es el metodo principal que sera ejecutado por un hilo trabajador
 */
@Override
public List<String> call() throws Exception {
    System.out.printf("Thread '%s' iniciando trabajo en: %s\n", Thread.currentThread().getName(), chunkFile.getName());

    OctaveWriter octaveWriter = new OctaveWriter();
    List<String> variablesEscritas = new ArrayList<>();
    
    // usa un bloque try con recursos para asegurarse que el lector se cierra siempre
    try (BufferedReader reader = new BufferedReader(new FileReader(this.chunkFile))) {
        String line;
        String  nombreDeMapaActual = "nombre_desconocido";
        List<String[]> renglonesParaUnMapa = new ArrayList<>(this.config.getLongitudY()); 
        int contadorMapasEnArchivo = 0;

        while ((line = reader.readLine()) != null) {
            if (line.trim().startsWith("# datos de:")) {
                // encontramos un nombre de archiov para el siguiente bloque de datos
                nombreDeMapaActual = line.replace("# datos de:", "").trim();
            } else if (!line.startsWith("#") && !line.trim().isEmpty()) {
                // esta es una linea de datos, aniadimos a nuestra lista temporal
                renglonesParaUnMapa.add(line.split("\t"));

                // revisa si hemos colectado suficientes renglones para un mapa completo
                if (renglonesParaUnMapa.size() == this.config.getLongitudY()) {
                    
                    // tenemos mapa completo, a procesar
                    
                    // 1. filtrar, revisamos el nombre antes de parsear datos
                    boolean pasaFiltroLinea = this.cellLineFilter.isEmpty() || nombreDeMapaActual.contains(this.cellLineFilter);
                    boolean pasaFiltroCalidad = this.qualityFilter.isEmpty() || nombreDeMapaActual.contains(this.qualityFilter);

                    if (pasaFiltroLinea && pasaFiltroCalidad) {
                        
                        // 2. parsear solo si pasa los filtros
  
  
                        int indiceGlobalDeMapa = this.mapStartIndex + contadorMapasEnArchivo;
                        MapaParticular mapaActual = DataLoader.parsearFilasAMapa(renglonesParaUnMapa, indiceGlobalDeMapa);
                        
                        // 3. procesar, analizamos el mapa
                        MapaParticular mapaProcesado = this.proceso.procesar(mapaActual);

                        // 4.escribimos el resultado al archivo compartido
                        String variableName = String.format("map_%04d", mapaProcesado.indice);
                        int[] intensityData = extraerIntensidadMapa(mapaProcesado);
                        
                        synchronized (octaveWriter) {
                            octaveWriter.escribeMatriz(
                                this.octaveFilename,
                                variableName,
                                intensityData,
                                this.config.especPorLadoMapa,
                                this.config.especPorLadoMapa
                            );
                        }
                        variablesEscritas.add(variableName);
                    }
                    
                    // 5. limpiar la memoria para el siguiente mapa
                    renglonesParaUnMapa.clear();
                    contadorMapasEnArchivo++;
                }
            }
        } 
    }
    
    System.out.printf("hilo '%s' termino el trabajo en: %s. Escribio %d mapas.\n", 
                      Thread.currentThread().getName(), chunkFile.getName(), variablesEscritas.size());
    
    return variablesEscritas;
}
    

    private static int[] extraerIntensidadMapa(MapaParticular mapa) {
        VecIntensidades[] espectros = mapa.datos.ejeY;
        int [] datosIntensidad = new int[espectros.length];
        for (int i = 0; i < espectros.length; i++) {
            for (int intensidad : espectros[i].getVecIntensidades()) {
                if (intensidad != 0) {
                    datosIntensidad[i] = intensidad;
                    break;
                }
            }
        }
        return datosIntensidad;
    }
}
