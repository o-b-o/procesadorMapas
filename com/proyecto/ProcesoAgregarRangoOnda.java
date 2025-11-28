package com.proyecto;

import java.util.ArrayList;
import java.util.List;

/**
 * un proceso que agrega las intensidades en un rango de numeros de onda especificados
 * por cada espectro suma las intensidades en el rango y la regresa
 * como un nuevo mapa donde solo hay una intensidad en cada espectro y es esta suma
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

        // encontrar todos los indices que corresponden al rango de numeros de onda
        List<Integer> indicesEnRango = encuentraIndicesNumeroDeOndaEnRango(ejeX);
        if (indicesEnRango.isEmpty()) {
            System.err.println("Advertencia: No se encontraron wavenumbers en el rango especificado para el mapa " + original.indice);
            //regresar ceros si no hay
            return crearMapaVacio(original);
        }

        // crear el arreglo procesado
        int numEspectros = ejeY_original.length;
        VecIntensidades[] ejeY_procesado = new VecIntensidades[numEspectros];

        //iterar por cada espectro (columna
        for (int i = 0; i < numEspectros; i++) {
            VecIntensidades espectroActual = ejeY_original[i];
            int numPuntos = espectroActual.getVecIntensidades().length;
            int sumaDeIntensidades = 0;

            // en este espectro, suma todoas las intensidades que corresponden a los indices deseados
            for (int indice : indicesEnRango) {
                sumaDeIntensidades += espectroActual.getIntensidadEnIndice(indice);
            }

            // crea un arreglo de intensidades vacio
            int[] nuevasIntensidades = new int[numPuntos];
            // y pone la suma en el primer entrada
            // luego se extrae con extraerIntensidadDeMapa
            nuevasIntensidades[0] = sumaDeIntensidades;

            ejeY_procesado[i] = new VecIntensidades(nuevasIntensidades);
        }

        // ensambla y regresa el mapa nuevo
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
        System.out.printf("Proceso completado para %d mapas.\n", mapasProcesados.size());
        return new ConjuntoDeMapas(mapasProcesados.toArray(new MapaParticular[0]));
    }

    /**
     * metodo de utilidad para encontrar todos los indices del arreglo que corresponden a los numeros de onda en el rango especificado.
     */
    private List<Integer> encuentraIndicesNumeroDeOndaEnRango(VecNumeroDeOnda onda) { 
        List<Integer> indices = new ArrayList<>();
        double[] numerosDeOnda = onda.getVecNumeroDeOnda();
        for (int i = 0; i < numerosDeOnda.length; i++) {
            if (numerosDeOnda[i] >= this.minOnda && numerosDeOnda[i] <= this.maxOnda) {
                indices.add(i);
            }
        }
        return indices;
    }
    
    /**
     * utilidad para crear mapa vacio si no hay datos en el rango
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
