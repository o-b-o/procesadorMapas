// orquestador principal
package com.proyecto;
import java.util.*;
import java.io.File;
import java.io.IOException;
public class Proyecto {
    private static String inputDir;
    private static String outputDir;
    private static DataGeometry geometry;

    public static void main(String[] args) {
	// intentamos correr la secuencia inicial: parsear argumentos, hacer un mapa de parametros, y correr la interfaz al usuario

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
		    System.out.println("2. Procesar datos (lectura concurrente + análisis)");
		    System.out.println("3. Todo el pipeline");
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
            System.out.println("2. Seleccion de frecuencia");
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
            switch (opcion) {
                case 0:
                    try{
			 startTime = System.currentTimeMillis();
			separarDatos();
			 endTime = System.currentTimeMillis();
			System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
			break;} catch (ErrorEnProceso e) {System.out.println("\nerror al procesar los datos ");}
                case 1:
                    try{
			 startTime = System.currentTimeMillis();
			prepararDatos();
			 endTime = System.currentTimeMillis();
			System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
			break;} catch (ErrorEnProceso e) {System.out.println("\nerror al procesar los datos ");}
                case 2:
		    System.out.print("Seleccione numero de onda de interes: ");
            
            
		    numOoo = Menu.getUserInt(scanner);
			 startTime = System.currentTimeMillis();
		    ejecutarFaseAnalisis( outputDir ,  geometry,numOoo);
			 endTime = System.currentTimeMillis();
			System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);

		    //ejecutarAnalisisVolumetrico( outputDir ,  geometry);
		    break;
                case 3:
		    		    System.out.print("Seleccione numero de onda de interes: ");
		    		     numOo = Menu.getUserInt(scanner);
			 startTime = System.currentTimeMillis();
				     prepararDatos();
		    ejecutarFaseAnalisis( outputDir ,  geometry,numOo);
			 endTime = System.currentTimeMillis();
			System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);

                    break;
            case 4:
		System.out.println("4. Separacion de datos personalizada \n en cuantos archivos quiere separar?");
		int archiNum = Menu.getUserInt(scanner);
                    prepararDatos(archiNum);

                    break;
	    case 5: { // Use curly braces to create a new scope for our variables
		// =============================================================
		//  START OF NEW MENU LOGIC
		// =============================================================
		System.out.println("\n--- Configuración de Filtros ---");
		System.out.print("Filtrar por línea celular (ej: 4T1, o presione Enter para todos): ");
		String cellLineFilter = Menu.getUserInput(scanner); // Assuming Menu has a static getUserInput(scanner)
		
		System.out.print("Filtrar por calidad (ej: Q1, o presione Enter para todos): ");
		String qualityFilter = Menu.getUserInput(scanner);
		
		System.out.print("Seleccione numero de onda de interes: ");
		numOo = Menu.getUserDouble(scanner); // It's better to have a dedicated method for doubles
					 startTime = System.currentTimeMillis();
					// Call the analysis method, now passing the filters as arguments
					ejecutarFaseAnalisis2(geometry, numOo, cellLineFilter, qualityFilter);

					 endTime = System.currentTimeMillis();
					System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);

		// =============================================================
		//  END OF NEW MENU LOGIC
		// =============================================================
	    }
	    case 6: { // Use curly braces to create a new scope for our variables
		// =============================================================
		//  START OF NEW MENU LOGIC
		// =============================================================
		System.out.println("\n--- Configuración de Filtros ---");
		System.out.print("Filtrar por línea celular (ej: 4T1, o presione Enter para todos): ");
		String cellLineFilter = Menu.getUserInput(scanner); // Assuming Menu has a static getUserInput(scanner)
		
		System.out.print("Filtrar por calidad (ej: Q1, o presione Enter para todos): ");
		String qualityFilter = Menu.getUserInput(scanner);
		
		System.out.print("Seleccione numero de onda de interes: ");
		numOo = Menu.getUserDouble(scanner); // It's better to have a dedicated method for doubles
		
		// Call the analysis method, now passing the filters as arguments
					 startTime = System.currentTimeMillis();
		ejecutarAnalisisConcurrente(geometry, numOo, cellLineFilter, qualityFilter);
					 endTime = System.currentTimeMillis();
					System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);

		// =============================================================
		//  END OF NEW MENU LOGIC
		// =========================>====================================
	    }
	    case 7: { // Use curly braces to create a new scope for our variables
		// =============================================================
		//  START OF NEW MENU LOGIC
		// =============================================================
		System.out.println("\n--- Configuración de Filtros ---");
		System.out.print("Filtrar por línea celular (ej: 4T1, o presione Enter para todos): ");
		String cellLineFilter = Menu.getUserInput(scanner); // Assuming Menu has a static getUserInput(scanner)
		
		System.out.print("Filtrar por calidad (ej: Q1, o presione Enter para todos): ");
		String qualityFilter = Menu.getUserInput(scanner);
		
		System.out.print("Seleccione numero de onda de interes: ");
		numOo = Menu.getUserDouble(scanner); // It's better to have a dedicated method for doubles

		long concSeqDelta=0;
		startTime = System.currentTimeMillis();
					// Call the analysis method, now passing the filters as arguments
					ejecutarFaseAnalisis2(geometry, numOo, cellLineFilter, qualityFilter);

					 endTime = System.currentTimeMillis();
					 concSeqDelta=endTime-startTime;
					 System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);

					startTime = System.currentTimeMillis();
		ejecutarAnalisisConcurrente(geometry, numOo, cellLineFilter, qualityFilter);
					 endTime = System.currentTimeMillis();
					System.out.printf("tiempo ejecucion %d ms\n", endTime-startTime);
					System.out.printf("tiempo analisis secuencial %d ms tiempo analisis concurrente %d\n", concSeqDelta, endTime-startTime);

		// =============================================================
		//  END OF NEW MENU LOGIC
		// =============================================================
	    }
			  
	    }
	    }catch (ErrorLectura e){
	            System.out.println("Error de lectura de los datos: \n");
	}catch (Exception e){
	            System.out.println("Error: \n");
	} 
    }

// Add this new method to Proyecto.java

private static void ejecutarAnalisisConcurrente(DataGeometry config, double targetWavenumber, String cellLineFilter, String qualityFilter) {
    System.out.println("\n=== FASE 3: ANÁLISIS DE DATOS (CONCURRENTE) ===\n");

    // --- Part 1: Configuration (Same as before) ---
    String chunkDir = config.outputDir + "chunks/";
    System.out.printf("chunkdir %s \n", chunkDir);
    String procdDir = config.outputDir + "/procd/";
        System.out.printf("outdir %s procddir %s \n", chunkDir, procdDir);
    String filterTag = cellLineFilter.isEmpty() ? "todos" : cellLineFilter;
    filterTag += "_";
    filterTag += qualityFilter.isEmpty() ? "todos" : qualityFilter;
    String octaveFilename = String.format("%s/resultado_CONCURRENTE_%s_onda_%.0f.m", procdDir, filterTag, targetWavenumber);
    
    // ... (logic to create procdDir is the same) ...
    File procdDirFile = new File(procdDir);
    if (!procdDirFile.exists()) procdDirFile.mkdirs();
    System.out.printf("procdir %s, prodirfile %s\n",procdDir, procdDirFile);
    // --- Part 2: Create the ExecutorService ---
    int numCores = Runtime.getRuntime().availableProcessors();
    java.util.concurrent.ExecutorService executor = java.util.concurrent.Executors.newFixedThreadPool(numCores);
    System.out.printf("Iniciando análisis con un pool de %d threads.\n", numCores);

    // --- Part 3: Create and Submit Tasks ---
    File[] chunks = obtenerChunksOrdenados(config.outputDir);
    if (chunks.length == 0) { executor.shutdown(); return; }

    List<java.util.concurrent.Future<List<String>>> listaDeFuturos = new ArrayList<>();
    ProcesoAgregarRangoOnda procesoCompartido = new ProcesoAgregarRangoOnda(targetWavenumber, targetWavenumber + 10.0);
    int mapIndexCounter = 0;

    for (File chunkFile : chunks) {
        // Create a task instance for this specific chunk file.
        AnalisisTask task = new AnalisisTask(chunkFile, config, cellLineFilter, qualityFilter, procesoCompartido, octaveFilename, mapIndexCounter);
        
        // Submit the task. It starts running in a background thread.
        java.util.concurrent.Future<List<String>> futuro = executor.submit(task);
        listaDeFuturos.add(futuro);
        
        // This is a rough estimation to keep the indices correct.
        // It relies on knowing the approximate size of a single map's data on disk.
        // (1600 rows * 900 spectra * ~4 chars/num + tabs) = ~6MB
        mapIndexCounter += Math.max(1, (int)(chunkFile.length() / (1600 * 900 * 4)));
    }

    // --- Part 4: Collect Results ---
    int totalMapasProcesados = 0;
    for (java.util.concurrent.Future<List<String>> futuro : listaDeFuturos) {
        try {
            // futuro.get() BLOCKS and waits for the thread to finish.
            List<String> variablesEscritas = futuro.get();
            totalMapasProcesados += variablesEscritas.size();
        } catch (Exception e) {
            System.err.println("Una tarea de análisis falló catastróficamente!");
            e.printStackTrace();
        }
    }

    // --- Part 5: Shutdown and Finalize ---
    executor.shutdown(); // Always do this!

    System.out.printf("\n✓ Análisis concurrente completado. Se procesaron %d mapas en total.\n", totalMapasProcesados);
    
    if (totalMapasProcesados > 0) {
        try {
            OctaveWriter octaveWriter = new OctaveWriter();
            octaveWriter.appendVisualizationCommands(octaveFilename, "map_0000", targetWavenumber);
        } catch (IOException e) { /* ... */ }
    }
}

    // Inside Proyecto.java

// The method signature now accepts the config object and the filter strings
    private static void ejecutarFaseAnalisis2(DataGeometry config, double targetWavenumber, String cellLineFilter, String qualityFilter) {
    System.out.println("\n=== FASE 3: ANÁLISIS CON FILTRADO POR NOMBRE ===\n");

    // --- Part 1: Configuration ---
    String chunkDir = outputDir + "/chunks/";
    String procdDir = outputDir + "/procd";
    System.out.printf("outdir %s procddir %s \n", chunkDir, procdDir);
    // Make the output filename reflect the filters being used
    String filterTag = cellLineFilter.isEmpty() ? "todos" : cellLineFilter;
    filterTag += "_";
    filterTag += qualityFilter.isEmpty() ? "todos" : qualityFilter;
    String octaveFilename = String.format("%s/resultado_%s_onda_%.0f.m", procdDir, filterTag, targetWavenumber);

    System.out.printf("Filtros activos: Linea Celular = '%s', Calidad = '%s'\n", 
                      cellLineFilter.isEmpty() ? "N/A" : cellLineFilter, 
                      qualityFilter.isEmpty() ? "N/A" : qualityFilter);
    System.out.printf("Leyendo chunks desde: %s\n", chunkDir);
    System.out.printf("Escribiendo salida en: %s\n", octaveFilename);

    // ... (Initialization of tools is the same: DataLoader, OctaveWriter, Proceso...)
    DataLoader dataLoader = new DataLoader();
    ProcesoAgregarRangoOnda miProceso = new ProcesoAgregarRangoOnda(targetWavenumber, targetWavenumber + 10.0); // Using a small range
    OctaveWriter octaveWriter = new OctaveWriter();

    // ... (Getting the list of chunk files is the same)
    File[] chunks = obtenerChunksOrdenados(outputDir);
    if (chunks.length == 0) return;

    // --- The Main Processing Loop (with new logic) ---
    int mapIndexCounter = 0;
    int mapasProcesadosCount = 0;

    for (File chunkFile : chunks) {
        try {
            System.out.printf("--- Revisando archivo: %s ---\n", chunkFile.getName());
            
            // 1. READ: Use the NEW data loader method to get a list of wrapped maps
            List<MapaConNombre> mapasConNombre = dataLoader.leerConjuntoDeArchivoConNombres(
                chunkFile,
                config.getLongitudY(),
                mapIndexCounter);

            // 2. **FILTER AND PROCESS**
            for (MapaConNombre mapaWrapper : mapasConNombre) {
                String filename = mapaWrapper.originalFilename;
                MapaParticular mapaActual = mapaWrapper.mapa;

                // Build a boolean to decide if we should process this map.
                boolean pasaFiltroLinea = cellLineFilter.isEmpty() || filename.contains(cellLineFilter);
                boolean pasaFiltroCalidad = qualityFilter.isEmpty() || filename.contains(qualityFilter);

                if (pasaFiltroLinea && pasaFiltroCalidad) {
                    System.out.printf("  ✓ El mapa de '%s' PASÓ el filtro. Procesando...\n", filename);
                    mapasProcesadosCount++;
                    
                    // 3. PROCESS: This logic is identical, but it now runs on 'mapaActual'
                    MapaParticular mapaProcesado = miProceso.procesar(mapaActual);

                    // 4. WRITE: This logic is identical
                    String variableName = String.format("map_%04d", mapaProcesado.indice);
                    int[] intensityData = extractIntensityDataFromMap(mapaProcesado);
                    octaveWriter.writeMatrix(
                        octaveFilename,
                        variableName,
                        intensityData,
                        geometry.especPorLadoMapa, // Use the value from the config
                        geometry.especPorLadoMapa
                    );
                } else {
                    // This map did not match, so we skip it.
                    System.out.printf("  - El mapa de '%s' NO pasó el filtro. Saltando.\n", filename);
                }
            } // End of loop through maps in the chunk
            
            mapIndexCounter += mapasConNombre.size();

        } catch (IOException e) {
            System.err.printf("Error fatal procesando el archivo %s. Saltando este archivo.\n", chunkFile.getName());
            e.printStackTrace();
        }
    } // End of loop through chunk files

    System.out.printf("\n✓ Análisis con filtrado completado. Se procesaron %d mapas en total.\n", mapasProcesadosCount);
    
    // Add the visualization commands at the end
    if (mapasProcesadosCount > 0) {
        try {
            octaveWriter.appendVisualizationCommands(octaveFilename, "map_0000", targetWavenumber);
        } catch (IOException e) {
            System.err.println("Error al escribir los comandos de visualización.");
        }
    }
}
// This method should be inside your main class, Proyecto.java

    private static void ejecutarFaseAnalisis(String outputDir, DataGeometry geometry, double target) {
    System.out.println("\n=== FASE 3: ANÁLISIS DE DATOS ===\n");

    // --- Part 1: Configuration ---
    // In a real application, you'd get this from the user!
    double targetWavenumber = target;
    
    // Construct the path to where the chunks are located. You are correct.
    String chunkDir = outputDir+ "chunks/";
    String procdDir = outputDir + "/procd";
        System.out.printf("outdir %s procddir %s \n", chunkDir, procdDir);
    // Construct the full path for the final Octave script file.
    String octaveFilename = String.format("%s/resultado_onda_%.0f.m", procdDir, targetWavenumber);


       File procdDirFile = new File(procdDir);

         // Check if the directory exists. If it doesn't, create it.
    if (!procdDirFile.exists()) {
        System.out.printf("El directorio de salida '%s' no existe. Creándolo...\n", procdDir);
        boolean success = procdDirFile.mkdirs(); // Use mkdirs() to create parent dirs if needed
        if (!success) {
            System.err.println("Error: No se pudo crear el directorio de salida. Verifique los permisos.");
            return; // Exit if we can't create the directory
        }
    }
    System.out.printf("Iniciando análisis para la onda ~%.2f cm⁻¹\n", targetWavenumber);
    System.out.printf("Leyendo chunks desde: %s\n", chunkDir);
    System.out.printf("Escribiendo salida en: %s\n", octaveFilename);

    // --- Part 2: Initialization ---
    // These are the "tools" we will use.
    DataLoader dataLoader = new DataLoader();
    //    ProcesoExtraerIntensidadSeq miProceso = new ProcesoExtraerIntensidadSeq(targetWavenumber);
    ProcesoAgregarRangoOnda miProceso = new ProcesoAgregarRangoOnda(targetWavenumber,targetWavenumber+100);
    OctaveWriter octaveWriter = new OctaveWriter(); // Assuming you've created this class
    System.out.printf("directorio %s\n",chunkDir);
    // --- Part 3: Get the List of Files to Process ---
    File dir = new File(chunkDir);
    // Use our lambda filter to get only the .csv files
    File[] chunks = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));

    // Crucial check: What if the directory is empty or doesn't exist?
    if (chunks == null || chunks.length == 0) {
        System.err.println("Error: No se encontraron archivos .csv en el directorio de chunks.");
        return; // Exit the method
    }

    // Process the files in a consistent order (0000, 0001, 0002...)
    java.util.Arrays.sort(chunks);
    System.out.printf("Se encontraron %d chunks para procesar.\n\n", chunks.length);

    // --- Part 4: The Main Processing Loop ---
    int mapIndexCounter = 0; // Keeps track of the global index of each map

    // This is the "for-each" loop. It will run once for each file found.
    for (File chunkFile : chunks) {
        try {
            System.out.printf("--- Procesando archivo: %s ---\n", chunkFile.getName());
            
            // 2. Use the DataLoader. This is the line you asked about.
            // We pass the file to read, the size of each data block, and the starting index.
            ConjuntoDeMapas datosDelChunk = dataLoader.leerConjuntoDeArchivo(
                chunkFile,
                geometry.getLongitudY(), // e.g., 1600
                mapIndexCounter
            );

            // 3. Pass the ENTIRE SET to the process's "Manager" method.
            ConjuntoDeMapas resultados = miProceso.procesarConjunto(datosDelChunk);

            // 4. Write the results. We need to loop through the processed maps.
            for (MapaParticular mapaProcesado : resultados.arregloDeMapas) {
                // Generate a clean variable name for Octave. Maybe use the map's index.
                String variableName = String.format("map_%04d", mapaProcesado.indice);
                
                // Extract the processed intensity data for writing
                // (This is a bit complex, we might need a helper method in MapaParticular)
                int[] intensityData = extractIntensityDataFromMap(mapaProcesado);

                octaveWriter.writeMatrix(
                    octaveFilename,
                    variableName,
                    intensityData,
                    geometry.especPorLadoMapa, // e.g., 30
                    geometry.especPorLadoMapa  // e.g., 30
                );
            }
            
            // UPDATE: Increment our counter by how many maps we just processed in this chunk.
            mapIndexCounter += datosDelChunk.arregloDeMapas.length;
            System.out.printf("--- Fin de %s ---\n\n", chunkFile.getName());

        } catch (IOException e) {
            System.err.printf("Error fatal procesando el archivo %s. Saltando este archivo.\n", chunkFile.getName());
            e.printStackTrace(); // Print the full error for debugging
        }
    }
    
    System.out.printf("✓ Análisis completado. Ejecute el script en Octave: %s\n", octaveFilename);
}
// Add this new method to Proyecto.java
private static File[] obtenerChunksOrdenados(String outputDir) {
    String chunkDir = outputDir + "/chunks/";
    File dir = new File(chunkDir);
    File[] chunks = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));

    if (chunks == null || chunks.length == 0) {
        System.err.println("Error: No se encontraron archivos .csv en el directorio de chunks.");
        return new File[0]; // Return an empty array
    }
    java.util.Arrays.sort(chunks);
    return chunks;
}
private static void ejecutarAnalisisVolumetrico(String outputDir, DataGeometry geometry) throws Exception {
    System.out.println("\n=== FASE 4: ANÁLISIS VOLUMÉTRICO ===\n");

    // --- Part 1: Configuration (No changes here) ---
    double startWavenumber = 0.0;
    double endWavenumber = 3000.0;
    double stepWavenumber = 60.0;

    String chunkDir = outputDir + "/chunks/";
    String procdDir = outputDir + "/procd";
    String octaveFilename = String.format("%s/resultado_volumetrico_%.0f-%.0f.m", procdDir, startWavenumber, endWavenumber);

    File procdDirFile = new File(procdDir);
    if (!procdDirFile.exists()) procdDirFile.mkdirs();

    System.out.printf("Iniciando análisis volumétrico de %.2f a %.2f cm⁻¹ (pasos de %.2f)\n",
                      startWavenumber, endWavenumber, stepWavenumber);

    // --- Part 2: Load ALL data from chunks ONCE ---
    System.out.println("Cargando todos los datos de los chunks en memoria...");
    DataLoader dataLoader = new DataLoader();
    List<MapaParticular> todosLosMapas = new ArrayList<>();
    File[] chunks = new File(chunkDir).listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));
    if (chunks == null || chunks.length == 0) { System.err.println("No chunks found."); return; }
    java.util.Arrays.sort(chunks);
    
    int mapIndexCounter = 0;
    for (File chunkFile : chunks) {
        ConjuntoDeMapas datosDelChunk = dataLoader.leerConjuntoDeArchivo(
            chunkFile, geometry.getLongitudY(), mapIndexCounter); // Using your Y coord for rowsPerCell
        
        // COMPATIBILITY CHANGE: Access the public array directly
        for(MapaParticular mapa : datosDelChunk.arregloDeMapas){
            todosLosMapas.add(mapa);
        }
        
        // COMPATIBILITY CHANGE: Get the size from the public array's length
        mapIndexCounter += datosDelChunk.arregloDeMapas.length;
    }
    ConjuntoDeMapas datosCompletos = new ConjuntoDeMapas(todosLosMapas.toArray(new MapaParticular[0]));
    
    // COMPATIBILITY CHANGE: Get the size from the public array's length
    System.out.printf("✓ Datos completos cargados (%d mapas).\n", datosCompletos.arregloDeMapas.length);

    // --- Part 3: Loop through wavenumbers, process, and collect results ---
    OctaveWriter octaveWriter = new OctaveWriter();
    List<String> sliceVariableNames = new ArrayList<>();

    for (double currentWavenumber = startWavenumber; currentWavenumber <= endWavenumber; currentWavenumber += stepWavenumber) {
        double minOnda = currentWavenumber - (stepWavenumber / 2.0);
        double maxOnda = currentWavenumber + (stepWavenumber / 2.0);
        
        System.out.printf("--- Procesando rango: %.2f - %.2f ---\n", minOnda, maxOnda);

        // This requires a compatible version of ProcesoAgregarRangoOnda (provided below)
        ProcesoAgregarRangoOnda procesadorDeSlice = new ProcesoAgregarRangoOnda(minOnda, maxOnda);
        ConjuntoDeMapas resultadosDelSlice = procesadorDeSlice.procesarConjunto(datosCompletos);

        // We only care about the first map for this visualization
        // COMPATIBILITY CHANGE: Use .length and getMapaWithIndex
        if (resultadosDelSlice.arregloDeMapas.length > 0) {
            MapaParticular primerMapa = resultadosDelSlice.getMapaWithIndex(0);
            String variableName = String.format("slice_w%d", (int)currentWavenumber);
            sliceVariableNames.add(variableName);

            // This requires the compatible helper method (provided below)
            int[] intensityData = extractIntensityDataFromMap(primerMapa);
            octaveWriter.writeMatrix(
                octaveFilename, variableName, intensityData,
                geometry.especPorLadoMapa, geometry.especPorLadoMapa);
        }
    }

    // --- Part 4: Write the final Octave commands (No changes here) ---
    octaveWriter.append3DStackingCommands(octaveFilename, "volumetricData", sliceVariableNames);
    octaveWriter.append3DVisualizationCommands(octaveFilename, "volumetricData");

    System.out.printf("\n✓ Análisis volumétrico completado. Ejecute el script en Octave: %s\n", octaveFilename);
}





// You will need a helper method like this, maybe in MapaParticular or here as a static helper.
// It extracts the 1D array of intensities needed by the OctaveWriter.
private static int[] extractIntensityDataFromMap(MapaParticular mapa) {
    VecIntensidades[] espectros = mapa.datos.ejeY;
    int[] intensityData = new int[espectros.length];
    
    // Since our process zeros out all but one row, we can just find the non-zero value in each column.
    for (int i = 0; i < espectros.length; i++) {
        for (int intensity : espectros[i].getVecIntensidades()) {
            if (intensity != 0) {
                intensityData[i] = intensity;
                break; // Move to the next spectrum once we've found the value
            }
        }
    }
    return intensityData;
}
    private static void separarDatos() throws Exception {
	// esta funcion une los archivos en un archivote y luego los separa en batches
        System.out.println("\n=== FASE 1: PREPARACIÓN ===");

        int numCores = Runtime.getRuntime().availableProcessors()*20;
        FileSplitter splitter = new FileSplitter(
            outputDir + "/mega_dataset.csv",
            outputDir + "/chunks/"
	    );
        splitter.splitByRows(numCores,geometry.getLongitudY());
    }

    private static void prepararDatos() throws Exception {
	// esta funcion une los archivos en un archivote y luego los separa en batches
        System.out.println("\n=== FASE 1: PREPARACIÓN ===");

	/////
        // union de archivos
        FileMerger merger = new FileMerger(inputDir, outputDir + "/mega_dataset.csv");
        merger.mergeVertically();
        /////
        // separacion  en batches (no solo importa la cantidad de procesadores, tambien la memoria que se consumira al
	// cargar los batches a ram.. entonces.. separamos en mas que solo numcores
        int numCores = Runtime.getRuntime().availableProcessors()*10;
        FileSplitter splitter = new FileSplitter(
            outputDir + "/mega_dataset.csv",
            outputDir + "/chunks/"
	    );
        splitter.splitByRows(numCores,geometry.getLongitudY());
    }
    // overloading de funciones (permitir que el numero de batches sea variable)
    private static void prepararDatos(int numeroDeArchivos) throws Exception {
       System.out.println(" union de archivos en un superarchivo  ===\n");
        
        // union de archivos
        FileMerger merger = new FileMerger(inputDir, outputDir + "/mega_dataset.csv");
        merger.mergeVertically();
        
        // separacion de mega archivo en batches
        int numCores = Runtime.getRuntime().availableProcessors();
        FileSplitter splitter = new FileSplitter(
            outputDir + "/mega_dataset.csv",
            outputDir + "/chunks/"
	    );
        splitter.splitByRows(numeroDeArchivos, geometry.getLongitudY());
    }


    
    private static void procesarDatos() throws ErrorEnProceso, ErrorLectura {
        System.out.println("\n=== FASE 2: PROCESAMIENTO ===");
        
 

	//	ProcesoExtraerIntensidadSeq proc= new ProcesoExtraerIntensidadSeq(2000.0);
	
        //MapaParticular[] datos = proc.procesar(entrada);
        
        // User interaction
        // ... (we'll add this next)
    }
 public static Map<String, String> parseArgs(String[] args) {
     try{
	 Map<String, String> argMap = new HashMap<>();
        for (int i = 0; i < args.length; i++) {
            // Check if the argument is a flag
            if (args[i].startsWith("-")) {
                // Check if there's a next element to be the value
                if (i + 1 < args.length && !args[i + 1].startsWith("-")) {
                    argMap.put(args[i], args[i + 1]);
                    i++; // Skip the next element because we've consumed it as a value
                } else {
                    // Handle flags without values (boolean flags), e.g., -verbose
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
