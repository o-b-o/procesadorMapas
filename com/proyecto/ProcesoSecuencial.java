package com.proyecto;
import java.io.*; 

public class ProcesoSecuencial implements ProcesadorMapa{
    private String programa;
    private String outPath;
    public ProcesoSecuencial(String programa){
	this.programa = programa;
	this.outPath = "./output/";
    };
    public ProcesoSecuencial(String programa, String out){
	this.programa = programa;
	this.outPath=out;
    };

    @Override
    public MapaParticular procesar(MapaParticular original){
	//System.out.printf(" uwu %d \n", original.datos.getVecIntensidades().getIntensidadAtIndex(3));
	return original;
    }

    @Override
    public ConjuntoDeMapas procesarConjunto(ConjuntoDeMapas original){
	    if (this.programa.equals("primera")) {
            System.out.printf("Primera iteración: escribiendo headers + datos\n");
            try {
                escribirMegaCSVPrimera(original);
            } catch (IOException e) {
                System.err.println("Error escribiendo mega CSV: " + e.getMessage());
                e.printStackTrace();
            }
        } else if (this.programa.equals("agregar")){
            System.out.printf("Agregando batch al mega archivo\n");
            try {
                agregarAMegaCSV(original);
            } catch (IOException e) {
                System.err.println("Error agregando a mega CSV: " + e.getMessage());
                e.printStackTrace();
            }
        }
	    else if (this.programa == "agregar"){
	    System.out.printf(" agregando MAPAS A MEGA ARCHIVO \n");
	
            try {
                escribirMegaCSV(original);
            } catch (IOException e) {
                System.err.println("Error escribiendo mega CSV: " + e.getMessage());
            }
        
	}
        //System.out.printf(" uwu %d \n", original.getMapaWithIndex(0).datos.getVecIntensidades().getIntensidadAtIndex(3));
        return original;
    }

    private void escribirMegaCSVPrimera(ConjuntoDeMapas mapas) throws IOException {
        File outputDir = new File(outPath);
        if (!outputDir.exists()) {
            outputDir.mkdirs();
        }
        
        BufferedWriter writer = new BufferedWriter(
            new FileWriter(outPath + "/mega_dataset.csv", false), // false = overwrite
            8192 * 4
        );
        
        escribirBatch(mapas, writer);
        writer.close();
        
        System.out.printf("✓ Mega CSV iniciado\n");
    }
    
    private void agregarAMegaCSV(ConjuntoDeMapas mapas) throws IOException {
        BufferedWriter writer = new BufferedWriter(
            new FileWriter(outPath + "/mega_dataset.csv", true), // true = append
            8192 * 4
        );
        
        escribirBatch(mapas, writer);
        writer.close();
        
        System.out.printf("✓ Batch agregado al mega CSV\n");
    }
    
    private void escribirBatch(ConjuntoDeMapas mapas, BufferedWriter writer) throws IOException {
        if (mapas.arregloDeMapas.length == 0) return;
        
        VecNumeroDeOnda ondas = mapas.getMapaWithIndex(0).datos.ejeX;
        int numFilas = ondas.getVecNumeroDeOnda().length;
        
        int totalColumnas = 0;
        for (MapaParticular mapa : mapas.arregloDeMapas) {
            totalColumnas += mapa.datos.ejeY.length;
        }
        
        System.out.printf("  Escribiendo batch: %d filas × %d columnas\n", numFilas, totalColumnas);
        
        for (int fila = 0; fila < numFilas; fila++) {
            StringBuilder linea = new StringBuilder();
            
            // First column: wave number
            linea.append(ondas.getVecNumeroDeOndaAtIndex(fila));
            
            // Remaining columns: all intensity vectors from all maps in this batch
            for (MapaParticular mapa : mapas.arregloDeMapas) {
                for (VecIntensidades intensidades : mapa.datos.ejeY) {
                    linea.append("\t");
                    linea.append(intensidades.getIntensidadAtIndex(fila));
                }
            }
            
            writer.write(linea.toString());
            writer.newLine();
            
            if (fila % 200 == 0) {
                System.out.printf("    [%d%%] Fila %d/%d\n", (fila * 100 / numFilas), fila, numFilas);
            }
        }
    }
    private void escribirMegaCSV(ConjuntoDeMapas mapas) throws IOException {
	BufferedWriter writer = new BufferedWriter(new FileWriter(outPath + "mega_dataset.csv"));
        
        // Assuming all maps share the same wave numbers
        VecNumeroDeOnda ondas = mapas.getMapaWithIndex(0).datos.ejeX;
        int numFilas = ondas.getVecNumeroDeOnda().length;
        
        // Count total columns across all maps
        int totalColumnas = 0;
        for (MapaParticular mapa : mapas.arregloDeMapas) {
            totalColumnas += mapa.datos.ejeY.length;
        }
        
        System.out.printf("Escribiendo mega CSV: %d filas x %d columnas\n", numFilas, totalColumnas);
        
        // Write row by row
        for (int fila = 0; fila < numFilas; fila++) {
            StringBuilder linea = new StringBuilder();
            
            // First column: wave number
            linea.append(ondas.getVecNumeroDeOndaAtIndex(fila));
	    
            // Remaining columns: all intensity vectors from all maps
            for (MapaParticular mapa : mapas.arregloDeMapas) {
                for (VecIntensidades intensidades : mapa.datos.ejeY) {
                    linea.append("\t");
                    linea.append(intensidades.getIntensidadAtIndex(fila));
                }
            }
            
            writer.write(linea.toString());
            writer.newLine();
            
            if (fila % 100 == 0) {
                System.out.printf("  [%d%%] Escribiendo fila %d/%d\n", (fila * 100 / numFilas), fila, numFilas);
            }
        }
        
        writer.close();
        System.out.printf("mega CSV creado exitosamente\n");
    }
    private void partirMegaCSV(ConjuntoDeMapas mapas, int numeroDeBatches) throws IOException {
	// Assuming all maps share the same wave numbers
        VecNumeroDeOnda ondas = mapas.getMapaWithIndex(0).datos.ejeX;
        int numFilas = ondas.getVecNumeroDeOnda().length;

	BufferedWriter writer = new BufferedWriter(new FileWriter(outPath + "mega_dataset.csv"));
	
        // Count total columns across all maps
        int totalColumnas = 0;
        for (MapaParticular mapa : mapas.arregloDeMapas) {
            totalColumnas += mapa.datos.ejeY.length;
        }
        
        System.out.printf("Escribiendo mega CSV: %d filas x %d columnas\n", numFilas, totalColumnas);
        
        // Write row by row
        for (int fila = 0; fila < numFilas; fila++) {
            StringBuilder linea = new StringBuilder();
            
            // First column: wave number
            linea.append(ondas.getVecNumeroDeOndaAtIndex(fila));
	    
            // Remaining columns: all intensity vectors from all maps
            for (MapaParticular mapa : mapas.arregloDeMapas) {
                for (VecIntensidades intensidades : mapa.datos.ejeY) {
                    linea.append("\t");
                    linea.append(intensidades.getIntensidadAtIndex(fila));
                }
            }
            
            writer.write(linea.toString());
            writer.newLine();
            
            if (fila % 100 == 0) {
                System.out.printf("  [%d%%] Escribiendo fila %d/%d\n", (fila * 100 / numFilas), fila, numFilas);
            }
        }
        
        writer.close();
        System.out.printf("✓ Mega CSV creado exitosamente\n");
    }
}
