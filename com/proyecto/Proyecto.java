package com.proyecto;
import java.util.*;
import java.io.File;
import java.io.IOException;
/**
 * Clase principal, gestora de la entrada de informacion por parte del usuario, de llamar la rutina de lectura de datos, o de procesamiento de datos. 
 * <p>
 * 
 * @author uwu
 * @version 0.9
 */

public class Proyecto {
    /**
     * directorio de entrada
     */
    private static String inputDir;
    /**
     * directorio de salida
     */
    private static String outputDir;
    /**
     * configuracion
     */ 
    private static DataGeometry geometry;
    /**
     * cuerpo principal del programa
     * intenta  correr la secuencia inicial: parsear argumentos, hacer un mapa de parametros, y correr la interfaz al usuario
     */
    public static void main(String[] args) {
	

	Scanner scanner = new Scanner(System.in);
	    
	    try{
            parseArgs(args) ;
	    Map<String, String> mapaArgs  = parseArgs(args);
	    // sacar valores del mapa de argumentos, proviendo uno por defecto si no existen
		    int xCord = Integer.parseInt(mapaArgs.getOrDefault("-rows", "0"));
		    int yCord = Integer.parseInt(mapaArgs.getOrDefault("-cols", "0"));
		    inputDir = mapaArgs.getOrDefault("-inputDir", "./input/");
		    outputDir = mapaArgs.getOrDefault("-outputDir", "./output/");
		    geometry = new DataGeometry(xCord, yCord, inputDir, outputDir);
		    System.out.printf("coordenada X (columnas) %d coordenada Y (renglones)%d:\n", geometry.getLongitudX(),geometry.getLongitudY());


		    
		    
		    
		    System.out.println("Configuracion del programa provista:");
		    System.out.println("cantidad de columnas en cada archivo = " + xCord);
		    System.out.println("cantidad de renglones en cada archivo= " + yCord);
		    System.out.println("directorio de lectura de archivos = " + inputDir);
		    System.out.println("directorio de escritura de analisis = " + outputDir);
		    Logger.log(LogLevel.INFO, "Configuracion del programa provista: ");
  		    Logger.log(LogLevel.INFO,"cantidad de columnas en cada archivo = " + xCord);
		    Logger.log(LogLevel.INFO,"cantidad de renglones en cada archivo = " + yCord);
		    Logger.log(LogLevel.INFO,"directorio de lectura de archivos = " + inputDir);
		    Logger.log(LogLevel.INFO,"directorio de escritura de analisis = " + outputDir);
		    
             // --------------------------------------
            // menu simple pero eficaz
	    //
		    System.out.println("\n=== OPCIONES ===");
		    System.out.println("0. separar datos (asume que ya se han unido en un superarchivo)");
		    System.out.println("1. Preparar datos (merge + split)");
		    System.out.println("2. barrido de frecuencias");
		    System.out.println("3. nadota ni lo intentes");
		    System.out.printf("4. Separacion de datos personalizada (en cantidad de archivos) \n note que el programa no partira datos de celulas, por lo que la cantidad de celulas\n en un archivo dado esla cantidad minima de archivos que genera \n ");
		    System.out.println("5. filtrado por tipo o calidad");
		    System.out.println("6. proceso Concurrente");
	    System.out.println("7. comparacion proceso secuencial de filtrado y proceso Concurrente equivalente");
		    System.out.print("Seleccione: ");
            
	    
		    int opcion = Menu.getUserInt(scanner);
		    System.out.print("Seleccione: ");
		    
		    while (opcion != 1 && opcion != 2 && opcion !=3 && opcion !=4&& opcion !=5 && opcion !=6 && opcion !=7 && opcion !=0){
			
			System.out.println("\nsu respuesta no esta en las opciones. Intente de nuevo :D");
			System.out.println("\n=== OPCIONES ===");
			System.out.println("1. Preparar datos (merge + split)");
			System.out.println("2. barrido de frecuencias");
			System.out.println("3. fuck u");
			System.out.println("4. Separacion de datos personalizada");
			System.out.println("5. filtrado por tipo o calidad");
			System.out.println("6. proceso Concurrente");
			System.out.println("7. comparacion proceso secuencial de filtrado y proceso Concurrente equivalente");
			
			System.out.print("Seleccione: ");
			
			
			opcion = Menu.getUserInt(scanner);
			
		    }
		    int numOoo;
		    double numOo;
		    long startTime;
		    long endTime; //endtimes hehehhh uh.
		    /**
		     * switch de opciones que puede escoger el usuario
		     * @param 0 selecciona y ejecuta la rutina de particion de un mega archivo (generado por el caso 1 sin ejecutar previamente la rutina de creacion de el mega archivo
		     * @param 1 selecciona y ejecuta la rutina de aglomeracion de datos y separacion en batches
		     * @param 2 selecciona el analisis por barrido de frecuencias (CANCELADA // no tiempo
		     * @param 3 Selecciona nada CANCELADO
		     * @param 4 selecciona la creacion y particion del mega archivo resultante en un numero de archivos seleccionado por ususaria
		     * @param 5 pide al usuario que provea filtros de calidad y nombre, y una frecuencia de medicion y su ancho de banda aplica los filtros y regresa un archivo que puede ser virualizado en octave
		     * @param 6 hace lo mismo pero concurrente
		     * @param 7 compara el proceso secuencial con el concurrente y reporta ambos tiempos
		     */


		    FiltroConfig conc;
		    String cellLineFilter;
		    String qualityFilter; 

		    double banda;

		    switch (opcion) {
		    case 0:
			try{
			    startTime = System.currentTimeMillis();
			    separarDatos();
			    endTime = System.currentTimeMillis();
			    System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
			    break;} catch (ErrorEnProceso e) {System.out.println("error al procesar los datos \n");}
		    case 1:
			try{
			    startTime = System.currentTimeMillis();
			    prepararDatos();
			    endTime = System.currentTimeMillis();
			    System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
			    break;} catch (ErrorEnProceso e) {System.out.println("error al procesar los datos \n");}
		    case 2:
			System.out.printf("mejor olvidelo\n");

			
			
			break;
		    case 3:
			System.out.printf("lolno\n");
			break;
		    case 4:
			System.out.println("4. Separacion de datos personalizada \n en cuantos archivos quiere separar?");
			int archiNum = Menu.getUserInt(scanner);
			prepararDatos(archiNum);
			
			break;
		    case 5: { 

			 conc = configFiltros(scanner);
			 cellLineFilter = conc.filtroLineaCelular;
			 qualityFilter = conc.filtroCalidad;
			 numOo = conc.numeroDeOnda;
			 banda = conc.anchoDeBanda;


			startTime = System.currentTimeMillis();
			// llamar el metodo de analisis con los filtros como parametros
			ejecutarFaseAnalisis2(geometry, numOo, banda, cellLineFilter, qualityFilter);
			
			endTime = System.currentTimeMillis();
			System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
			break;
			
		    }
		    case 6: {
			 conc = configFiltros(scanner);
			 cellLineFilter = conc.filtroLineaCelular;
			 qualityFilter = conc.filtroCalidad;
			 numOo = conc.numeroDeOnda;
			 banda = conc.anchoDeBanda;


			// llama al metodo de analisis pasando los filtros como argumentos
			startTime = System.currentTimeMillis();
			ejecutarAnalisisConcurrente(geometry, numOo, cellLineFilter, qualityFilter);
			endTime = System.currentTimeMillis();
			System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
			break;

		    }
		    case 7: {

						conc = configFiltros(scanner);
			 cellLineFilter = conc.filtroLineaCelular;
			 qualityFilter = conc.filtroCalidad;
			 numOo = conc.numeroDeOnda;
			 banda = conc.anchoDeBanda;
			long concSeqDelta=0;			
		startTime = System.currentTimeMillis();
		
		ejecutarFaseAnalisis2(geometry, numOo, banda, cellLineFilter, qualityFilter);
		
		endTime = System.currentTimeMillis();
		concSeqDelta=endTime-startTime;
		System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
		
		startTime = System.currentTimeMillis();
		ejecutarAnalisisConcurrente(geometry, numOo, cellLineFilter, qualityFilter);
		endTime = System.currentTimeMillis();
		System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
		System.out.printf("tiempo analisis secuencial %d ms tiempo analisis concurrente %d\n", concSeqDelta, endTime-startTime);
		
		break;
		    }
			
		    }
	    }catch (ErrorLectura e){
		System.out.println("Error de lectura de los datos: \n");
	    }catch (Exception e){
		System.out.println("Error: \n");
	    } 
	    
    }    

    /**
     * funcion encargada de la interaccion con usuario al configurar filtros
     * @param un scanner abierto
     */
    private static FiltroConfig configFiltros(Scanner scanner){
			    			System.out.printf("Configuracion de Filtros\n");
			System.out.printf("Filtrar por linea celular (ej: 4T1, o presione Enter para todos): \n");
			 String cellLineFilter = Menu.getUserInput(scanner); 
			
			System.out.printf("Filtrar por calidad (ej: Q1, o presione Enter para todos): \n");
			 String qualityFilter = Menu.getUserInput(scanner);
			
			System.out.printf("Seleccione numero de onda de interes: \n");
			double numOo = Menu.getUserDouble(scanner,1000); 
			
			System.out.printf("Seleccione ancho de banda\n");
			double banda = Menu.getUserDouble(scanner,100);
			    return new FiltroConfig(cellLineFilter, qualityFilter, numOo, banda);
    }
    /**
     * funcion ejecutora de analisis concurrente
     * @param config es un objeto DataGeometry con la informacion necesaria para el proceso
     * @param targetWavernumber el numero de onda que busca el proceso
     * @param cellLineFilter filtro de linea celular
     * @param qualityFilter filtro de calidad
     * ejecuta un proceso de seleccion de numero de onda y escribe los resultados en un archivo que peude ser leido por octave.
     */
private static void ejecutarAnalisisConcurrente(DataGeometry config, double targetWavenumber, String cellLineFilter, String qualityFilter) {
    System.out.println("\n=== FASE 3: ANÁLISIS DE DATOS (CONCURRENTE) ===\n");

    // configuracion
    String chunkDir = config.outputDir + "chunks/";
    System.out.printf("chunkdir %s \n", chunkDir);
    String procdDir = config.outputDir + "/procd/";
        System.out.printf("outdir %s procddir %s \n", chunkDir, procdDir);
    String filterTag = cellLineFilter.isEmpty() ? "todos" : cellLineFilter;
    filterTag += "_";
    filterTag += qualityFilter.isEmpty() ? "todos" : qualityFilter;
    String octaveFilename = String.format("%s/resultado_CONCURRENTE_%s_onda_%.0f.m", procdDir, filterTag, targetWavenumber);
    

    File procdDirFile = new File(procdDir);
    if (!procdDirFile.exists()) procdDirFile.mkdirs();
    System.out.printf("procdir %s, prodirfile %s\n",procdDir, procdDirFile);
    // crear servicio ejecutor
    int numCores = Runtime.getRuntime().availableProcessors();
    java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(numCores);
    System.out.printf("Iniciando analisis con un pool de %d threads.\n", numCores);

    // crear y someter tareas
    File[] chunks = obtenerChunksOrdenados(config.outputDir);
    if (chunks.length == 0) { executor.shutdown(); return; }

    List<java.util.concurrent.Future<List<String>>> listaDeFuturos = new ArrayList<>();
    ProcesoAgregarRangoOnda procesoCompartido = new ProcesoAgregarRangoOnda(targetWavenumber, targetWavenumber + 10.0);
    int mapIndexCounter = 0;

    for (File chunkFile : chunks) {
        // crear instancia de tarea para este archivo  porcion especifico
        AnalisisTask task = new AnalisisTask(chunkFile, config, cellLineFilter, qualityFilter, procesoCompartido, octaveFilename, mapIndexCounter);

	// someter la tarea, empieza a correr en un hilo en el fondo

        java.util.concurrent.Future<List<String>> futuro = executor.submit(task);
        listaDeFuturos.add(futuro);
        
        // estimacion para mantener los indices
	// 
         // estimacion de los mapas contados
        mapIndexCounter += Math.max(1, (int)(chunkFile.length() / (1600 * 900 * 4)));
    }

    // colectar resultados
    int totalMapasProcesados = 0;
    for (java.util.concurrent.Future<List<String>> futuro : listaDeFuturos) {
        try {
            // futuro.get() bloquea y espera a que el hilo termine
            List<String> variablesEscritas = futuro.get();
            totalMapasProcesados += variablesEscritas.size();
        } catch (Exception e) {
            System.err.printf("Una tarea de analisis fallo catastroficamente! Al colectar datos algo no pudo ..\n");
            e.printStackTrace();
        }
    }

    // cerrar y finalizar
    executor.shutdown(); // cerrar el ejecutor (sino el programa se queda volando hangueado en el espacio)

    System.out.printf("Analisis concurrente completado. Se procesaron %d mapas en total.\n", totalMapasProcesados);
    
    if (totalMapasProcesados > 0) {
        try {
            OctaveWriter octaveWriter = new OctaveWriter();
            octaveWriter.aniadeComandosVisualizacion(octaveFilename, "map_0000", targetWavenumber);
        } catch (IOException e) {             System.err.printf("ocurrio un error al terminar de escribir el archivo de salida\n"); }
    }
}


/**
     * funcion ejecutora de analisis secuencial
     * carga las porciones, filtra de acuerdo a los criterios de usuario
     * procesa los mapas que pasan el filtro promediando el valor de intensidad en el rango seleccionado
     * escribe los resultados en un script de octave
     * @param config es un objeto DataGeometry con la informacion necesaria para el proceso
     * @param targetWavernumber el numero de onda que busca el proceso
     * @param anchoDeBanda el ancho del intervalo sobre el cual se promedian intensidades
     * @param cellLineFilter filtro de linea celular
     * @param qualityFilter filtro de calidad
     * ejecuta un proceso de seleccion de numero de onda y escribe los resultados en un archivo que peude ser leido por octave.
     */
    private static void ejecutarFaseAnalisis2(DataGeometry config, double targetWavenumber, double anchoDeBanda, String cellLineFilter, String qualityFilter) {
    System.out.println(" analisis con filtrado por nombre \n");

    // configuracion
    String chunkDir = outputDir + "/chunks/";
    String procdDir = outputDir + "/procd";
    System.out.printf("outdir %s procddir %s \n", chunkDir, procdDir);
    // el archivo de salida refleja los filtros usados
    String filterTag = cellLineFilter.isEmpty() ? "todos" : cellLineFilter;
    filterTag += "_";
    filterTag += qualityFilter.isEmpty() ? "todos" : qualityFilter;
    String octaveFilename = String.format("%s/resultado_%s_onda_%.0f.m", procdDir, filterTag, targetWavenumber);

    System.out.printf("Filtros activos: Linea Celular = '%s', Calidad = '%s'\n", 
                      cellLineFilter.isEmpty() ? "N/A" : cellLineFilter, 
                      qualityFilter.isEmpty() ? "N/A" : qualityFilter);
    System.out.printf("Leyendo chunks desde: %s\n", chunkDir);
    System.out.printf("Escribiendo salida en: %s\n", octaveFilename);


    DataLoader dataLoader = new DataLoader();
    
    double minOnda = targetWavenumber - (anchoDeBanda / 2.0);
    double maxOnda = targetWavenumber + (anchoDeBanda / 2.0);
    ProcesoAgregarRangoOnda miProceso = new ProcesoAgregarRangoOnda(minOnda, maxOnda);

        
    OctaveWriter octaveWriter = new OctaveWriter();

    // adquirir la lista de archivos porcionados
    File[] chunks = obtenerChunksOrdenados(outputDir);
    if (chunks.length == 0) return;

    // loop principal de procesanmiento
    int mapIndexCounter = 0;
    int mapasProcesadosCount = 0;

    for (File chunkFile : chunks) {
        try {
            System.out.printf("Revisando archivo: %s \n", chunkFile.getName());
            // leer, usar la clase dataloader para leer un conjunto de archivos y sus nombres
            List<MapaConNombre> mapasConNombre = dataLoader.leerConjuntoDeArchivoConNombres(
                chunkFile,
                config.getLongitudY(),
                mapIndexCounter);

            // filtrar y procesar
            for (MapaConNombre mapaWrapper : mapasConNombre) {
                String filename = mapaWrapper.originalFilename;
                MapaParticular mapaActual = mapaWrapper.mapa;

                // booleano que decide si se procesa o no un mapa
                boolean pasaFiltroLinea = cellLineFilter.isEmpty() || filename.contains(cellLineFilter);
                boolean pasaFiltroCalidad = qualityFilter.isEmpty() || filename.contains(qualityFilter);

                if (pasaFiltroLinea && pasaFiltroCalidad) {
                    System.out.printf("El mapa de '%s' paso el filtro. Procesando...\n", filename);
                    mapasProcesadosCount++;

		    // procesar

                    MapaParticular mapaProcesado = miProceso.procesar(mapaActual);

                    // escribir
                    String variableName = String.format("map_%04d", mapaProcesado.indice);
                    int[] intensityData = extraerIntensidadesDeMapa(mapaProcesado);
                    octaveWriter.escribeMatriz(
                        octaveFilename,
                        variableName,
                        intensityData,
                        geometry.especPorLadoMapa,
                        geometry.especPorLadoMapa
                    );
                } else {
                    // el mapa no paso un filtro asi que se salta
                    System.out.printf("El mapa de '%s' no paso el filtro. Saltando.\n", filename);
                }
            } // fin del loop sobre los mapas en la porcion
            
            mapIndexCounter += mapasConNombre.size();

        } catch (IOException e) {
            System.err.printf("Error fatal procesando el archivo %s. Saltando este archivo.\n", chunkFile.getName());
            e.printStackTrace();
        }
    } // fin de loop sobre las porciones

    System.out.printf("Analisis con filtrado completado. Se procesaron %d mapas en total.\n", mapasProcesadosCount);
    
    // aniadir comando de visualizacion al final del archivo de resultado
    if (mapasProcesadosCount > 0) {
        try {
            octaveWriter.aniadeComandosVisualizacion(octaveFilename, "map_0000", targetWavenumber);
        } catch (IOException e) {
            System.err.println("Error al escribir los comandos de visualizacion.");
        }
    }
}

    /*
     * funcion para obtener porciones (batches) de archivos ordenadas
     * @param outputDir directorio de salida (donde la fase de preparacion guarda las porciones)
     */
private static File[] obtenerChunksOrdenados(String outputDir) {
    String chunkDir = outputDir + "/chunks/";
    File dir = new File(chunkDir);
    File[] chunks = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));

    if (chunks == null || chunks.length == 0) {
        System.err.println("error: No se encontraron archivos .csv en el directorio de chunks.");
        return new File[0]; // regresa arreglo vacio si no hay archivos
    }
    java.util.Arrays.sort(chunks);
    return chunks;
}



    /**
     * metodo de utilidad que extrae intensidades de un mapa
     * extrae el arreglo unidimensional de intensidades que necesita OctaveWriter
     */
    
private static int[] extraerIntensidadesDeMapa(MapaParticular mapa) {
    VecIntensidades[] espectros = mapa.datos.ejeY;
    int[] intensityData = new int[espectros.length];
    
    // Como el proceso de filtrado hace que todos los renglones salvo pocos sean cero, solo sumamos las que tienen valores distintos de cero
    for (int i = 0; i < espectros.length; i++) {
	int avgIntensity=0;
	int intensityCount=0;
        for (int intensity : espectros[i].getVecIntensidades()) {
            if (intensity != 0){ 
		avgIntensity += intensity;

            }
	}



	intensityData[i] =avgIntensity;
    }

    return intensityData;
}
    /**
     * esta funcion separa un archivote (nombre hardcodeado a el output de este programa)
     */
    private static void separarDatos() throws Exception {
	
        System.out.printf(" separando:");

        int numCores = Runtime.getRuntime().availableProcessors()*20;
        FileSplitter splitter = new FileSplitter(
            outputDir + "/mega_dataset.csv",
            outputDir + "/chunks/"
	    );
        splitter.separaPorRenglones(numCores,geometry.getLongitudY());
    }
    /**
     * esta funcion une los archivos en un archivote y luego los separa en batches
     */
    private static void prepararDatos() throws Exception {

        System.out.printf("iniciando union \n");

	/////
        // union de archivos
        FileMerger merger = new FileMerger(inputDir, outputDir + "/mega_dataset.csv");
        merger.unirVerticalmente();
	        System.out.printf("iniciando separacion \n");
        /////
        // separacion  en batches (no solo importa la cantidad de procesadores, tambien la memoria que se consumira al
	// cargar los batches a ram.. entonces.. separamos en mas que solo numcores
        int numCores = Runtime.getRuntime().availableProcessors()*10;
        FileSplitter splitter = new FileSplitter(
            outputDir + "/mega_dataset.csv",
            outputDir + "/chunks/"
	    );
        splitter.separaPorRenglones(numCores,geometry.getLongitudY());
    }
       /**
     * esta funcion une los archivos en un archivote y luego los separa en batches igual que la anterior pero usa overloading de funciones (para permitir que el numero de batches sea eleccion del usuario)
     */

    private static void prepararDatos(int numeroDeArchivos) throws Exception {
       System.out.printf(" union de archivos en un superarchivo \n");
        
        // union de archivos
        FileMerger merger = new FileMerger(inputDir, outputDir + "/mega_dataset.csv");
        merger.unirVerticalmente();
        
        // separacion de mega archivo en batches
        int numCores = Runtime.getRuntime().availableProcessors();
        FileSplitter splitter = new FileSplitter(
            outputDir + "/mega_dataset.csv",
            outputDir + "/chunks/"
	    );
        splitter.separaPorRenglones(numeroDeArchivos, geometry.getLongitudY());
    }


    
    /*
     * funcion de parsing de argumentos
     * @param arreglo de argumentos de linea de comandos
     */
 public static Map<String, String> parseArgs(String[] args) {
     try{
	 Map<String, String> argMap = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            // revisa si el argumento comienza con - (es un flag ala -v -c etc)
            if (args[i].startsWith("-")) {
                // revisa si hay un siguiente elemento para que sea su valor
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    argMap.put(args[i], args[i + 1]);
                    i++; // salta el siguiente elemento porque fue asignado como valor
                } else {
                    // maneja flags sin valor (eg -vervose)
                    argMap.put(args[i], "true"); 
                }
            }
        }
        return argMap;
     }
     catch ( Exception e){
	 return null;
     }
     }
}
