package com.proyecto;

import java.util.ArrayList;
import java.util.List;

/**
 * A process that aggregates the intensities across a specified range of wavenumbers.
 * For each spectrum, it sums the intensity values within the range and outputs
 * a new spectrum where only one row contains this sum.
 */
public class ProcesoAgregarRangoOnda implements ProcesadorMapa {

    private final double minOnda;
    private final double maxOnda;

    public ProcesoAgregarRangoOnda(double minOnda, double maxOnda) {
        // Ensure min is always less than or equal to max
        if (minOnda > maxOnda) {
            this.minOnda = maxOnda;
            this.maxOnda = minOnda;
        } else {
            this.minOnda = minOnda;
            this.maxOnda = maxOnda;
        }
    }

    @Override
    public MapaParticular procesar(MapaParticular original) {
        DatosEspectro datosOriginales = original.datos;
        VecNumeroDeOnda ejeX = datosOriginales.ejeX;
        VecIntensidades[] ejeY_original = datosOriginales.ejeY;

        // --- Step 1: Find all row indices that fall within our wavenumber range ---
        List<Integer> indicesEnRango = findWavenumberIndicesInRange(ejeX);
        if (indicesEnRango.isEmpty()) {
            System.err.println("Advertencia: No se encontraron wavenumbers en el rango especificado para el mapa " + original.indice);
            // Return a map full of zeros in this case
            return crearMapaVacio(original);
        }

        // --- Step 2: Create the new, processed array of intensity vectors ---
        int numEspectros = ejeY_original.length;
        VecIntensidades[] ejeY_procesado = new VecIntensidades[numEspectros];

        // --- Step 3: Loop through each spectrum (column) ---
        for (int i = 0; i < numEspectros; i++) {
            VecIntensidades espectroActual = ejeY_original[i];
            int numPuntos = espectroActual.getVecIntensidades().length;
            int sumaDeIntensidades = 0;

            // For the current spectrum, sum up all the intensities at our target indices
            for (int indice : indicesEnRango) {
                sumaDeIntensidades += espectroActual.getIntensidadAtIndex(indice);
            }

            // Create a new intensity array that is all zeros...
            int[] nuevasIntensidades = new int[numPuntos];
            // ... and place our calculated sum in the very first row.
            // This is a simple trick to make our downstream `extractIntensityDataFromMap` method work without changes!
            nuevasIntensidades[0] = sumaDeIntensidades;

            ejeY_procesado[i] = new VecIntensidades(nuevasIntensidades);
        }

        // --- Step 4: Assemble and return the new map ---
        DatosEspectro datosProcesados = new DatosEspectro(ejeX, ejeY_procesado);
        return new MapaParticular(original.indice, datosProcesados);
    }

    @Override
    public ConjuntoDeMapas procesarConjunto(ConjuntoDeMapas original) {
        System.out.printf("Iniciando agregación de intensidad para el rango %.2f-%.2f cm⁻¹...\n", minOnda, maxOnda);
        List<MapaParticular> mapasProcesados = new ArrayList<>();
        for (MapaParticular mapaIndividual : original.arregloDeMapas) {
            mapasProcesados.add(this.procesar(mapaIndividual));
        }
        System.out.printf("✓ Proceso completado para %d mapas.\n", mapasProcesados.size());
        return new ConjuntoDeMapas(mapasProcesados.toArray(new MapaParticular[0]));
    }

    /**
     * Helper method to find all array indices corresponding to wavenumbers
     * within the specified min/max range.
     */
    private List<Integer> findWavenumberIndicesInRange(VecNumeroDeOnda onda) {
        List<Integer> indices = new ArrayList<>();
        double[] wavenumbers = onda.getVecNumeroDeOnda();
        for (int i = 0; i < wavenumbers.length; i++) {
            if (wavenumbers[i] >= this.minOnda && wavenumbers[i] <= this.maxOnda) {
                indices.add(i);
            }
        }
        return indices;
    }
    
    /**
     * Helper to create an empty (all zero) map if no data is found in the range.
     */
    private MapaParticular crearMapaVacio(MapaParticular original) {
        DatosEspectro datosOriginales = original.datos;
        int numEspectros = datosOriginales.ejeY.length;
        int numPuntos = datosOriginales.ejeX.getVecNumeroDeOnda().length;
        VecIntensidades[] ejeY_vacio = new VecIntensidades[numEspectros];
        for(int i = 0; i < numEspectros; i++){
            ejeY_vacio[i] = new VecIntensidades(new int[numPuntos]);
        }
        DatosEspectro datosVacios = new DatosEspectro(datosOriginales.ejeX, ejeY_vacio);
        return new MapaParticular(original.indice, datosVacios);
    }
}
