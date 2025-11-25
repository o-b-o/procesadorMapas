package com.proyecto;
import com.tarea2.tareea2.MiClaseUtil;
import com.tarea4.Tarea4;
import java.util.Map;
import java.util.HashMap;
import java.util.Scanner;  // para leer input del usuario en tiempo de ejecucion

public class Proyecto {
    private static String inputDir;
    private static String outputDir;

    public static void main(String[] args){
	try{
	    System.out.println("=== SISTEMA DE PROCESAMIENTO DE ESPECTROS ===\n");
	    if (args.length != 8) { // ayuda al usuario si los parametros que paso no son adecuados
            System.err.println("Uso: java Proyecto -inputDir <dirEntrada> -outputDir <dirSalida> -rows n -cols m");

        }
            // gestion de argumentos
            //
	    Map<String, String> mapaArgs  = parseArgs(args);
	    // sacar valores del mapa de argumentos, proviendo uno por defecto si no existen
	    int xCord = Integer.parseInt(mapaArgs.getOrDefault("-rows", "0"));
	    int yCord = Integer.parseInt(mapaArgs.getOrDefault("-cols", "0"));
            inputDir = mapaArgs.getOrDefault("-inputDir", "./input/");
            outputDir = mapaArgs.getOrDefault("-outputDir", "./output/");
	    


	    System.out.println("Configuracion del programa provista:");
	    System.out.println("cantidad de renglones en cada archivo = " + xCord);
	    System.out.println("cantidad de columnas en cada archivo= " + yCord);
	    System.out.println("directorio de lectura de archivos = " + inputDir);
	    System.out.println("directorio de escritura de analisis = " + outputDir);


            // definicion de geometria


	    DataGeometry dataGeom= new DataGeometry(xCord,yCord);
	    // System.out.printf("%d longitud en x , %d longitud en Y\n",dataGeom.getLongitudX() ,dataGeom.getLongitudY() );



	    // inicio
	    // lectura de datos
            SeqHandlr gestorSecuencial = new SeqHandlr(inputDir,outputDir);
            int totalArchivos = gestorSecuencial.contarArchivos();
            String megaFile = inputDir + "/mega_dataset.csv";
            ejecutarFaseSplit2(megaFile, outputDir, dataGeom);

              // FASE 3: Interactive queries
              ejecutarFaseInteractiva();

	    System.out.printf("Total de archivos encontrados: %d\n", totalArchivos);

            //juntaArchivos(inputDir, outputDir);


            // debug crap should be gone when this is done..

            /// generacion de estructuras de datos
	    VecNumeroDeOnda uwu = new VecNumeroDeOnda(new double[]{2.0,2.0,3.0,4.0});
	    // System.out.printf("%f  valor en el indice \n",  uwu.getVecNumeroDeOndaAtIndex(3) );
	    VecIntensidades owo = new VecIntensidades(new int[]{2,2,3,4});
	    //System.out.printf("%d  valor en el indice  \n",  owo.getIntensidadAtIndex(3));
	    DatosEspectro ewe = new DatosEspectro(uwu,owo);
	    //System.out.printf("%d  valor en el indice  \n",  ewe.getVecIntensidades().getIntensidadAtIndex(3) );
	    MapaParticular gwg = new MapaParticular(30,ewe);
	    //System.out.printf("  mapa correspondiente al archivo %d \n",  gwg.datos.getVecIntensidades().getIntensidadAtIndex(3) );
	    
	    /// generacion de conjuntos de mapas
	    ConjuntoDeMapas xwx = new ConjuntoDeMapas(new MapaParticular[]{gwg,gwg});
	    //System.out.printf("  datos extraidos de un mapa en un arreglo %d \n",  xwx.getMapaWithIndex(0).datos.getVecIntensidades().getIntensidadAtIndex(3) );
	    
	    // procesamiento de datos
	    // creacion procesador secuencial
	    ProcesoSecuencial AxA = new ProcesoSecuencial("agregar");
	    //AxA.procesar(gwg);
	    //	    AxA.procesarConjunto(mapasLUCE);
	    // creacion procesador concurrente
	    ProcesoConcurrente ExE = new ProcesoConcurrente("a");
	    ExE.procesar(gwg);
	    ExE.procesarConjunto(xwx);
	    // creacion selector
	    SelectorSimple QwQ = new SelectorSimple("u");
	    QwQ.seleccionar(xwx, new int[] {0,1,3});
	    // creacion orquestador
	    Procesador<ProcesoConcurrente, SelectorSimple> WuW = new Procesador<>("u", ExE, QwQ);
	    ConjuntoDeMapas MuM;
	    // ejecucion programa
	    MuM = WuW.ejecutarProceso(xwx);
	    //System.out.printf("  datos extraidos de un mapa en un arreglo %d \n", MuM.getMapaWithIndex(0).datos.getVecIntensidades().getIntensidadAtIndex(3) );
	    return;
	    
	} // seccion de manejo de errores
	// --------------------------
	// --------------------------------
	catch (ErrorLectura e){
	    System.err.printf("ERROR LEYENDO ARCHIVO: %s%n", e.getMessage());
	    System.err.printf("archivo problematico: %s%n", e.getNombreArchivo());
	    e.printStackTrace();
	    System.exit(1);
	} catch (ErrorEnProceso e){
	    System.err.printf("ERROR PROCESANDO ARCHIVO: %s\n", e.getMessage());
	    System.err.printf("archivo problematico: \n");
	}
	catch (ErrorProcesamiento e){
	    System.err.printf("ERROR DE PROCESAMIENTO!! AAAAAAAAA");

	}
    catch (Exception e){
        System.err.printf("FUCKAAAAAAAAA");

    }
    }
// Query 1: Get specific map
private static void consultarMapaEspecifico(ConjuntoDeMapas datos, Scanner scanner, String outputDir)
        throws ErrorEnProceso, ErrorProcesamiento {

    System.out.print("Ingrese número de mapa (0-" + (datos.arregloDeMapas.length - 1) + "): ");
    int mapaIdx = scanner.nextInt();

    if (mapaIdx < 0 || mapaIdx >= datos.arregloDeMapas.length) {
        System.out.println("Índice fuera de rango");
        return;
    }

    // Use Procesador to extract
    SelectorSimple selector = new SelectorSimple("extraer");
    ProcesoSecuencial extractor = new ProcesoSecuencial("extraer_mapa", outputDir);
    Procesador<ProcesoSecuencial, SelectorSimple> proc =
        new Procesador<>("extract", extractor, selector);

    ConjuntoDeMapas resultado = proc.ejecutarProceso(
        new ConjuntoDeMapas(new MapaParticular[]{datos.getMapaWithIndex(mapaIdx)})
    );

    System.out.printf("\n✓ Mapa %d extraído\n", mapaIdx);
    MapaParticular mapa = resultado.getMapaWithIndex(0);
    System.out.printf("  Espectros: %d\n", mapa.datos.ejeY.length);
    System.out.printf("  Puntos por espectro: %d\n", mapa.datos.ejeX.getVecNumeroDeOnda().length);

    // Write to file
    extractor.procesarConjunto(resultado);
}

// Query 2: Get spectrum at coordinates
private static void consultarEspectroPorCoordenadas(ConjuntoDeMapas datos, Scanner scanner)
        throws ErrorEnProceso, ErrorProcesamiento {

    System.out.print("Ingrese coordenada X (0-29): ");
    int x = scanner.nextInt();
    System.out.print("Ingrese coordenada Y (0-29): ");
    int y = scanner.nextInt();

    // Convert 2D coordinates to 1D index
    int espectroIdx = (y * 30) + x; // Assuming 30×30 grid

    System.out.printf("Buscando espectro en posición (%d, %d) = índice %d\n", x, y, espectroIdx);

    // Extract spectrum from all maps
    ProcesoConcurrente extractor = new ProcesoConcurrente("extraer_espectro:" + espectroIdx);
    SelectorSimple selector = new SelectorSimple("all");
    Procesador<ProcesoConcurrente, SelectorSimple> proc =
        new Procesador<>("extract_spectrum", extractor, selector);

    ConjuntoDeMapas resultado = proc.ejecutarProceso(datos);

    System.out.printf("\n✓ Espectro extraído de %d mapas\n", resultado.arregloDeMapas.length);
}

// Query 3: Filter by wavenumber
private static void filtrarPorNumeroDeOnda(ConjuntoDeMapas datos, Scanner scanner)
        throws ErrorEnProceso, ErrorProcesamiento {

    System.out.print("Ingrese número de onda mínimo: ");
    double minOnda = scanner.nextDouble();
    System.out.print("Ingrese número de onda máximo: ");
    double maxOnda = scanner.nextDouble();

    System.out.printf("Filtrando rango: %.2f - %.2f cm⁻¹\n", minOnda, maxOnda);

    // Use Procesador with filtering
    String parametros = String.format("filtrar:%.2f-%.2f", minOnda, maxOnda);
    ProcesoConcurrente filtrador = new ProcesoConcurrente(parametros);
    SelectorSimple selector = new SelectorSimple("all");
    Procesador<ProcesoConcurrente, SelectorSimple> proc =
        new Procesador<>("filter", filtrador, selector);

    long inicio = System.currentTimeMillis();
    ConjuntoDeMapas resultado = proc.ejecutarProceso(datos);
    long fin = System.currentTimeMillis();

    System.out.printf("\n✓ Filtrado completado en %.2f segundos\n", (fin - inicio) / 1000.0);
    System.out.printf("  Mapas procesados: %d\n", resultado.arregloDeMapas.length);

    // Write results
    ProcesoSecuencial writer = new ProcesoSecuencial("escribir_filtrado", outputDir);
    writer.procesarConjunto(resultado);
}
// Add to Proyecto.java
private static void ejecutarFaseInteractiva() throws ErrorLectura, ErrorEnProceso, ErrorProcesamiento {
    System.out.println("\n=== FASE 4: ANÁLISIS INTERACTIVO ===\n");

    Scanner scanner = new Scanner(System.in);

    System.out.println("Opciones de consulta:");
    System.out.println("1. Seleccionar mapa específico");
    System.out.println("2. Seleccionar espectro por coordenadas (x, y)");
    System.out.println("3. Filtrar por número de onda");
    System.out.print("\nSeleccione opción: ");

    int opcion = scanner.nextInt();

    // Read the chunked data
    ConcurrentHandlr handler = new ConcurrentHandlr(outputDir + "/chunks/", outputDir);
    ConjuntoDeMapas datos = handler.leerDatosCompletos();

    switch (opcion) {
        case 1:
            consultarMapaEspecifico(datos, scanner, outputDir);
            break;
        case 2:
            consultarEspectroPorCoordenadas(datos, scanner);
            break;
        case 3:
            filtrarPorNumeroDeOnda(datos, scanner);
            break;
        default:
            System.out.println("Opción inválida");
    }

    scanner.close();
}
private static void ejecutarFaseSplit(String megaFile, String outputDir) throws Exception {
    System.out.println("=== FASE 2: DIVISIÓN POR FILAS ===\n");

    SeqHandlr handler = new SeqHandlr(outputDir, outputDir);
    handler.dividirMegaCSVPorFilasEnArchivos(
        megaFile,
        outputDir + "/revertidos/",
        1076  // Number of original files
    );

    System.out.println();
    }
private static void ejecutarFaseSplit2(String megaFile, String outputDir, DataGeometry geom) throws Exception {
    System.out.println("=== FASE 2: DIVISIÓN EN CHUNKS (POR CELDAS) ===\n");

    int numCores = Runtime.getRuntime().availableProcessors();
    System.out.printf("Procesadores disponibles: %d\n", numCores);

    SeqHandlr handler = new SeqHandlr(outputDir, outputDir);
    handler.dividirMegaCSVPorColumnas(
        megaFile,
        outputDir + "/chunks/",
        numCores
    );

    System.out.println();
}
private static void ejecutarFaseSplit3(String megaFile, String outputDir) throws Exception {
    System.out.println("=== FASE 2: DIVISIÓN EN CHUNKS ===\n");

    int numCores = Runtime.getRuntime().availableProcessors();
    System.out.printf("Procesadores disponibles: %d\n", numCores);

    // Use the column-based splitter
    SeqHandlr handler = new SeqHandlr(outputDir, outputDir);
    handler.dividirMegaCSVPorColumnas(
        megaFile,
        outputDir + "chunks/",
        numCores
    );

    System.out.println();
}

    public static void juntaArchivos(String inputDir, String outputDir) throws ErrorLectura {
        SeqHandlr gestorSecuencial = new SeqHandlr(inputDir,outputDir);
        int totalArchivos = gestorSecuencial.contarArchivos();

        int BATCH_SIZE = 1; // Adjust based on your RAM
        int numBatches = (int) Math.ceil((double) totalArchivos / BATCH_SIZE);
        System.out.printf("Total archivos: %d, Batches calculados: %d\n", totalArchivos, numBatches);

        System.out.printf("Dividiendo en %d batches de %d archivos\n\n", numBatches, BATCH_SIZE);

        for (int batch = 0; batch < numBatches; batch++) {
            int startIdx = batch * BATCH_SIZE;
            int endIdx = Math.min(startIdx + BATCH_SIZE, totalArchivos);

            System.out.printf("--- BATCH %d/%d (archivos %d-%d) ---\n", batch + 1, numBatches, startIdx, endIdx - 1);

            // Read batch
            ConjuntoDeMapas batchData = gestorSecuencial.leerDatosCompletos(startIdx, endIdx);


            // Process batch


            //		String programa = (batch == 0) ? "primera" : "agregar";
// Process batch DIRECTLY without Procesador orchestrator
            String programa = (batch == 0) ? "primera" : "agregar";
            System.out.printf("DEBUG: Batch %d using programa='%s'\n", batch, programa);

            ProcesoSecuencial proc = new ProcesoSecuencial(programa, outputDir);

            //String programa =  "agregar";
            /*
              ProcesoSecuencial procesador = new ProcesoSecuencial(programa, outputDir);
            SelectorSimple selector = new SelectorSimple("all");
            Procesador<ProcesoSecuencial, SelectorSimple> proc = new Procesador<>("merge", procesador, selector);
            */
            proc.procesarConjunto(batchData);

            System.out.printf("Batch %d completado\n\n", batch + 1);

            // Suggestion: Force garbage collection between batches
            System.gc();
        }

    System.out.println("=== MERGE COMPLETADO ===");
    System.out.printf("Archivo final: %smega_dataset.csv\n", outputDir);


        }

    public static Map<String, String> parseArgs(String[] args) {
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
}
