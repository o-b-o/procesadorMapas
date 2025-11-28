package com.proyecto;
import java.io.*;
/**
 * Clase encargada de partir un mega archivo (resultado de unir los datos de dos o mas celulas) en porciones para procesamiento en batches (concurrente o secuencial).
 * <p>
 * 
 * 
 * 
 *
 * @author uwu
 * @version 0.9
 */

public class FileSplitter {
    /**
     * archivo de entrada
     */ 
    private String inputFile;
    /**
     * directorio de salida
     */
    private String outputDir;

    /**
     * el constructor toma un archivo de entrada y un directorio de salida
     */
    public FileSplitter(String inputFile, String outputDir) {
        this.inputFile = inputFile;
        this.outputDir = outputDir;
    }

    /**
     * metodo para separar por renglones
     * @param numeroArchivos cuantos archivos quieres de resultado
     * @param lineasEnDatosOriginales cuantas lineas hay en un archivo original
     */
    public void separaPorRenglones(int numeroArchivos, int lineasEnDatosOriginales) throws IOException {
        System.out.printf("Dividiendo archivo en %d archivos por filas\n", numeroArchivos);
        
        File outDir = new File(outputDir);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        
        // Count rows
        BufferedReader contador = new BufferedReader(new FileReader(inputFile));
        long renglonesTotales = 0;
        String line;
        while ((line = contador.readLine()) != null) {
	    // no contamos los comentarios (metadata)
	    if (!line.startsWith("#")) renglonesTotales++;
        }
        contador.close();
        
        System.out.printf("Total filas: %d\n", renglonesTotales);
        
        long renglonesPorArchivo = (long) Math.ceil((double) renglonesTotales / numeroArchivos);
        System.out.printf("Filas por archivo: %d\n", renglonesPorArchivo);
	// cuantos archivos (celulas) tenemos en este archivo
        long archivosEnArchivo = renglonesTotales / lineasEnDatosOriginales;
        System.out.printf("este archivo se compone de : %d archivos (celulas)\n", archivosEnArchivo);
        
        // separar
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        
        int idXArchivo = 0;
        long renglonesEnArchivoActual = 0 ;
        BufferedWriter writer = null;
        
        while ((line = reader.readLine()) != null) {

	    boolean esDato= !line.startsWith("#");
	    if (!esDato){
		// si no es dato decidimos si necesitamos un nuevo archivo antes de escribirlo
		if (writer == null || (renglonesEnArchivoActual > renglonesPorArchivo && renglonesEnArchivoActual % lineasEnDatosOriginales == 0)) {

		    if (writer != null) writer.close();
		    
		    String nombreArchivo = String.format("%s/chunk_%04d.csv", outputDir, idXArchivo);
		    writer = new BufferedWriter(new FileWriter(nombreArchivo));
		    idXArchivo++;
		    renglonesEnArchivoActual = 0;
		}
	    }
            
	    if (writer != null)
		{
		    writer.write(line);
		    writer.newLine();
		}
	    if (esDato){
	    renglonesEnArchivoActual++;
	    }
	    
	    
	}
        
        if (writer != null){ writer.close();}
        reader.close();
        
        System.out.printf("dividido en %d archivos\n", idXArchivo);
    }
}
