package com.proyecto;
import com.tarea2.tareea2.MiClaseUtil;
import com.tarea4.Tarea4;
import java.util.Map;
import java.util.HashMap;

public class Proyecto {
    public static void main(String[] args){
	try{
	    System.out.println("=== SISTEMA DE PROCESAMIENTO DE ESPECTROS ===\n");
	    if (args.length != 8) {
            System.err.println("Uso: java Proyecto -inputDir <dirEntrada> -outputDir <dirSalida> -rows n -cols m");

        }
	    Map<String, String> mapaArgs  = parseArgs(args);
	    // sacar valores del mapa, proviendo uno si no existen
	    int xCord = Integer.parseInt(mapaArgs.getOrDefault("-rows", "0"));
	    int yCord = Integer.parseInt(mapaArgs.getOrDefault("-cols", "0"));
	    String inputDir = mapaArgs.getOrDefault("-inputDir", "./input/");
	    String outputDir = mapaArgs.getOrDefault("-outputDir", "./output/");
	    


	    System.out.println("Configuracion del programa provista:");
	    System.out.println("cantidad de renglones en cada archivo = " + xCord);
	    System.out.println("cantidad de columnas en cada archivo= " + yCord);
	    System.out.println("directorio de lectura de archivos = " + inputDir);
	    System.out.println("directorio de escritura de analisis = " + outputDir);
	    // definicion de geometria
	    //
	    //	    int xCord = 900;
	    // int yCord = 1600;
	    DataGeometry dataGeom= new DataGeometry(xCord,yCord);
	    // System.out.printf("%d longitud en x , %d longitud en Y\n",dataGeom.getLongitudX() ,dataGeom.getLongitudY() );

	    // lectura de datos
	    SeqHandlr gestorSecuencial = new SeqHandlr(inputDir,outputDir);
	    int totalArchivos = gestorSecuencial.contarArchivos();


        
	    System.out.printf("Total de archivos encontrados: %d\n", totalArchivos);
        
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
        
	    
	// ConjuntoDeMapas mapasLUCE= gestorSecuencial.leerDatosCompletos();

	    
	    
	    ConcurrentHandlr gestorConcurrente = new ConcurrentHandlr(inputDir,outputDir);
	    gestorConcurrente.leerDatosCompletos();
	    
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
	} catch (ErrorLectura e){
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
