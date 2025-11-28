package com.proyecto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataLoader {

    /**
     * Lee un solo archivo que puede contener multiples bloques de datos (mapas) y lo convierte en un ConjuntoDeMapas
     *
     * @param archivo el objeto File a leer
     * @param renglonesPorMapa el numero de renglones en un solo mapa particular
     * @param indiceInicio el indice inicial para los mapas en este archivo
     * @return un ConjuntoDeMapas que contiene todos los mapas extraidos del archivo
     * @throws IOException si hay error leyendo el archivo
     */
    public ConjuntoDeMapas leerConjuntoDeArchivo(File archivo, int renglonesPorMapa, int indiceInicio) throws IOException {
        System.out.printf("Cargando conjunto de mapas desde: %s\n", archivo.getName());

        // leee todo el archivo separando los datos de los compentaros

        BufferedReader br = new BufferedReader(new FileReader(archivo));
        List<String[]> todasLasFilas = new ArrayList<>();
        String line;

        while ((line = br.readLine()) != null) {
            // si una linea no es comentario y no esta vacia es dato
            if (!line.startsWith("#") && !line.trim().isEmpty()) {
                todasLasFilas.add(line.split("\t"));
            }
            // todo lo demas, me da igual
        }
        br.close();

        if (todasLasFilas.isEmpty()) {
            // regresa vacio si no hay dato
            return new ConjuntoDeMapas(new MapaParticular[0]);
        }

        //determina cuanto mapa en este archivo
        int totalMapasEnArchivo = todasLasFilas.size() / renglonesPorMapa;
        System.out.printf("  Detectados %d mapas en este archivo (bloques de %d filas).\n", totalMapasEnArchivo, renglonesPorMapa);
        
        List<MapaParticular> listaDeMapas = new ArrayList<>();

        // itera por los datos 
        for (int i = 0; i < totalMapasEnArchivo; i++) {
            int fromIndex = i * renglonesPorMapa;
            int toIndex = fromIndex + renglonesPorMapa;

            List<String[]> filasDelMapaActual = todasLasFilas.subList(fromIndex, toIndex);
            
            int mapIndex = indiceInicio + i;


            MapaParticular mapa = parsearFilasAMapa(filasDelMapaActual, mapIndex);
            listaDeMapas.add(mapa);
        }
        
        // convertir la lista al arreglo requerido por el constructor de conjuntodemapas
        return new ConjuntoDeMapas(listaDeMapas.toArray(new MapaParticular[0]));
    }


/**
 * Lee un archivo y regresa una lista de objetos MapaParticular con su nombre original
 *
 * @param archivo archivo a leer
 * @param renglonesPorMapa renglones en un solo mapa particular
 * @param iniceInicio el indice inicial para los mapas de este archivo
 * @return una lista de MapaConNombre
 * @throws IOException si hay errores leyendo el archivo
 */
public List<MapaConNombre> leerConjuntoDeArchivoConNombres(File archivo, int renglonesPorMapa, int iniceInicio) throws IOException {
    System.out.printf("Cargando mapas y nombres desde: %s\n", archivo.getName());

    BufferedReader br = new BufferedReader(new FileReader(archivo));
    List<String[]> todasLasFilasDeDatos = new ArrayList<>();
    List<String> nombresDeMapas = new ArrayList<>();
    String line;

    // lee todo el archivo separando datos de headers
    while ((line = br.readLine()) != null) {
        if (line.trim().startsWith("# datos de:")) {
            // este es un header que nos dice un nuevo mapa comienza
            // le sacamos el nombre y guardamos
            String mapName = line.replace("# datos de:", "").trim();
            nombresDeMapas.add(mapName);
        } else if (!line.startsWith("#") && !line.trim().isEmpty()) {
            // esta es una linea de datos, la aniadimos a la lista de datos
            todasLasFilasDeDatos.add(line.split("\t"));
        }
        // todo lo demas comentario se ignora
    }
    br.close();

    if (todasLasFilasDeDatos.isEmpty()) {
        System.out.println("  No se encontraron filas de datos en el archivo.");
        return new ArrayList<>(); // regresa una lista vacia si no hay dato
    }

    //  lee los bloques de datos y les pone nombre
    int totalMapasDetectados = todasLasFilasDeDatos.size() / renglonesPorMapa;
    System.out.printf("  Detectados %d bloques de datos y %d nombres de mapas.\n", totalMapasDetectados, nombresDeMapas.size());

    // revisa si tiene tantos nombres como bloquesk
    if (totalMapasDetectados != nombresDeMapas.size()) {
        System.err.println("ADVERTENCIA: El numero de bloques de datos no coincide con el numero de encabezados '# datos de:'. Se usaran nombres por defecto.");
    }
    
    List<MapaConNombre> listaDeMapasEnvueltos = new ArrayList<>();
    for (int i = 0; i < totalMapasDetectados; i++) {
        int fromIndex = i * renglonesPorMapa;
        int toIndex = fromIndex + renglonesPorMapa;
        List<String[]> filasDelMapaActual = todasLasFilasDeDatos.subList(fromIndex, toIndex);
        
        int mapIndex = iniceInicio + i;
        // saca el nombre, si no tenemos suficientes usa el de defecto
        String mapName = (i < nombresDeMapas.size()) ? nombresDeMapas.get(i) : "mapa_desconocido_" + mapIndex + ".csv";

        // crea el ojeto mapaparticular
        MapaParticular mapa = parsearFilasAMapa(filasDelMapaActual, mapIndex);

        // crea el elvoltorio de mapa con nombre
        MapaConNombre wrapper = new MapaConNombre(mapa, mapName);
        listaDeMapasEnvueltos.add(wrapper);
    }
    
    return listaDeMapasEnvueltos;
}
    /**
     * metodo privado de utilidad que convierte una lista de strings en un mapa particular
     */
    public static MapaParticular parsearFilasAMapa(List<String[]> filasDelMapa, int indice) {
        int numRenglones = filasDelMapa.size();
        if (numRenglones == 0) {
            throw new IllegalArgumentException("La lista de filas para parsear esta vacía.");
        }
        int numColumnas = filasDelMapa.get(0).length;

      
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
