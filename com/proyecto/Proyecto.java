// orquestador principal
package com.proyecto;
import java.util.*;
import java.io.File;
import java.io.IOException;
public class Proyecto {
    private static String inputDir;
    private static String outputDir;
    private static DataGeometry geometry;
    private static int especPorLadoMapa = 30;
    public static void main(String[] args) {
	// intentamos correr la secuencia inicial: parsear argumentos, hacer un mapa de parametros, y correr la interfaz al usuario

	Scanner scanner = new Scanner(System.in);
	    
	    try{
            parseArgs(args) ;
	    Map<String, String> mapaArgs  = parseArgs(args);
	    // sacar valores del mapa de argumentos, proviendo uno por defecto si no existen
		    int xCord = Integer.parseInt(mapaArgs.getOrDefault("-rows", "0"));
		    int yCord = Integer.parseInt(mapaArgs.getOrDefault("-cols", "0"));
		    geometry = new DataGeometry(xCord, yCord);
		    System.out.printf("coordenada X (columnas) %d coordenada Y (renglones)%d:\n", geometry.getLongitudX(),geometry.getLongitudY());
		    inputDir = mapaArgs.getOrDefault("-inputDir", "./input/");
		    outputDir = mapaArgs.getOrDefault("-outputDir", "./output/");
		    
		    
		    
		    System.out.println("Configuracion del programa provista:");
		    System.out.println("cantidad de columnas en cada archivo = " + xCord);
		    System.out.println("cantidad de renglones en cada archivo= " + yCord);
		    System.out.println("directorio de lectura de archivos = " + inputDir);
		    System.out.println("directorio de escritura de analisis = " + outputDir);
		    
		    
		    
             // --------------------------------------
            // menu simple pero eficaz
	    //
		    System.out.println("\n=== OPCIONES ===");
		    System.out.println("1. Preparar datos (merge + split)");
		    System.out.println("2. Procesar datos (lectura concurrente + análisis)");
		    System.out.println("3. Todo el pipeline");
		    System.out.println("4. Separacion de datos personalizada");
            System.out.print("Seleccione: ");
            
	    
            int opcion = Menu.getUserInt(scanner);
	                System.out.print("Seleccione: ");
	    
			while (opcion != 1 && opcion != 2 && opcion !=3 && opcion !=4){

	      System.out.println("\nsu respuesta no esta en las opciones. Intente de nuevo :D");
            System.out.println("\n=== OPCIONES ===");
            System.out.println("1. Preparar datos (merge + split)");
            System.out.println("2. Procesar datos (lectura concurrente + análisis)");
            System.out.println("3. Todo el pipeline");
	    System.out.println("4. Separacion de datos personalizada");
	    
            System.out.print("Seleccione: ");
            
            
	    opcion = Menu.getUserInt(scanner);
	 	
	    }
            
            switch (opcion) {
                case 1:
                    try{
			
			prepararDatos();
			break;} catch (ErrorEnProceso e) {System.out.println("\nerror al procesar los datos ");}
                case 2:
		    ejecutarFaseAnalisis( outputDir ,  geometry);
		    //ejecutarAnalisisVolumetrico( outputDir ,  geometry);
		    break;
                case 3:
                    prepararDatos();
		    ejecutarFaseAnalisis( outputDir ,  geometry);
                    break;
            case 4:
		System.out.println("4. Separacion de datos personalizada \n en cuantos archivos quiere separar?");
		int archiNum = Menu.getUserInt(scanner);
                    prepararDatos(archiNum);

                    break;
            }
	} catch (ErrorLectura e){
	            System.out.println("Error de lectura de los datos: \n");
	}catch (Exception e){
	            System.out.println("Error: \n");
	} 
    }
    
// This method should be inside your main class, Proyecto.java

private static void ejecutarFaseAnalisis(String outputDir, DataGeometry geometry) {
    System.out.println("\n=== FASE 3: ANÁLISIS DE DATOS ===\n");

    // --- Part 1: Configuration ---
    // In a real application, you'd get this from the user!
    double targetWavenumber = 1050.0;
    
    // Construct the path to where the chunks are located. You are correct.
    String chunkDir = outputDir+ "/chunks/";
    String procdDir = outputDir + "/procd";
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
    ProcesoAgregarRangoOnda miProceso = new ProcesoAgregarRangoOnda(1000,2000);
    OctaveWriter octaveWriter = new OctaveWriter(); // Assuming you've created this class

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
                    especPorLadoMapa, // e.g., 30
                    especPorLadoMapa  // e.g., 30
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
                especPorLadoMapa, especPorLadoMapa);
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

    private static void prepararDatos() throws Exception {
	// esta funcion une los archivos en un archivote y luego los separa en batches
        System.out.println("\n=== FASE 1: PREPARACIÓN ===");

	/////
        // union de archivos
        FileMerger merger = new FileMerger(inputDir, outputDir + "/mega_dataset.csv");
        merger.mergeVertically();
        /////
        // separacion  en batches
        int numCores = Runtime.getRuntime().availableProcessors();
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
