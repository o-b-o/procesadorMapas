package com.proyecto;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProcesoExtraerIntensidadSeq implements ProcesadorMapa {
    // These are for configuration, not for file paths to read here
    private final double numeroDeOndaDeseado;

    // The constructor is now much simpler. It only needs its own configuration.
    public ProcesoExtraerIntensidadSeq(double numOnd) {
        this.numeroDeOndaDeseado = numOnd;
    }

    // =======================================================================
    //  JOB #1: The Worker - Process ONE map.
    // =======================================================================
    @Override
    public MapaParticular procesar(MapaParticular original) {
        // --- Step 1: Get original data and find the target index ---
        DatosEspectro datosOriginales = original.datos;
        VecNumeroDeOnda ejeX_original = datosOriginales.getVecNumeroOnda();
        VecIntensidades[] ejeY_original = datosOriginales.getVecVecIntensidades();

        int indiceDeseado = findClosestWavenumberIndex(ejeX_original);
        if (indiceDeseado == -1) {
            System.err.println("Advertencia: No se pudo encontrar el número de onda en el mapa " + original.indice);
            // Return a copy of the original if we can't process it
            return new MapaParticular(original.indice, datosOriginales);
        }

        // --- Step 2: Prepare the NEW array to hold the processed results ---
        int numEspectros = ejeY_original.length;
        VecIntensidades[] ejeY_procesado = new VecIntensidades[numEspectros];

        // --- Step 3: Loop through each original spectrum to create a new, processed one ---
        for (int i = 0; i < numEspectros; i++) {
            VecIntensidades espectroActual = ejeY_original[i];
            int numPuntos = espectroActual.getVecIntensidades().length;

            int[] nuevasIntensidades = new int[numPuntos]; // Defaults to all zeros
            int valorDeseado = espectroActual.getIntensidadAtIndex(indiceDeseado);
            nuevasIntensidades[indiceDeseado] = valorDeseado; // Set the one important value

            VecIntensidades espectroProcesado = new VecIntensidades(nuevasIntensidades);
            ejeY_procesado[i] = espectroProcesado;
        }

        // --- Step 4: Assemble and return the final processed object ---
        DatosEspectro datosProcesados = new DatosEspectro(ejeX_original, ejeY_procesado);
        return new MapaParticular(original.indice, datosProcesados);
    }

    // =======================================================================
    //  JOB #2: The Manager - Process a WHOLE SET of maps.
    // =======================================================================
    @Override
    public ConjuntoDeMapas procesarConjunto(ConjuntoDeMapas original) {
        System.out.println("Iniciando proceso de extracción de intensidad para un conjunto de mapas...");
        
        List<MapaParticular> mapasProcesados = new ArrayList<>();
        
        // Loop through all maps in the input set
        for (MapaParticular mapaIndividual : original.arregloDeMapas) {
            // For each map, call our "Worker" method to do the actual processing
            MapaParticular mapaResultado = this.procesar(mapaIndividual);
            mapasProcesados.add(mapaResultado);
        }
        
        System.out.printf("✓ Proceso completado para %d mapas.\n", mapasProcesados.size());
        
        // Return a new ConjuntoDeMapas containing all the processed results
        return new ConjuntoDeMapas(mapasProcesados.toArray(new MapaParticular[0]));
    }

    // =======================================================================
    //  HELPER METHOD: This is perfectly fine as it is.
    // =======================================================================
    private int findClosestWavenumberIndex(VecNumeroDeOnda onda) {
        // ... (your existing code here is correct) ...
        double[] wavenumbers = onda.getVecNumeroDeOnda();
        if (wavenumbers.length == 0) return -1;
        int bestIndex = 0;
        double minDifference = Math.abs(wavenumbers[0] - this.numeroDeOndaDeseado);
        for (int i = 1; i < wavenumbers.length; i++) {
            double difference = Math.abs(wavenumbers[i] - this.numeroDeOndaDeseado);
            if (difference < minDifference) {
                minDifference = difference;
                bestIndex = i;
            }
        }
        return bestIndex;
    }
}
