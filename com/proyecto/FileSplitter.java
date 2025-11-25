package com.proyecto;
import java.io.*;

public class FileSplitter {
    private String inputFile;
    private String outputDir;
    
    public FileSplitter(String inputFile, String outputDir) {
        this.inputFile = inputFile;
        this.outputDir = outputDir;
    }
    
    public void splitByRows(int numFiles, int linesInOriginalData) throws IOException {
        System.out.printf("Dividiendo archivo en %d archivos por filas\n", numFiles);
        
        File outDir = new File(outputDir);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        
        // Count rows
        BufferedReader counter = new BufferedReader(new FileReader(inputFile));
        long totalRows = 0;
        String line;
        while ((line = counter.readLine()) != null) {
	    // no contamos los comentarios (metadata)
	    if (!line.startsWith("#")) totalRows++;
        }
        counter.close();
        
        System.out.printf("Total filas: %d\n", totalRows);
        
        long rowsPerFile = (long) Math.ceil((double) totalRows / numFiles);
        System.out.printf("Filas por archivo: %d\n", rowsPerFile);
	// cuantos archivos (celulas) tenemos en este arcivo
        long filesInFile = totalRows / linesInOriginalData;
        System.out.printf("este archivo se compone de : %d archivos (celulas)\n", filesInFile);
        
        // Split
        BufferedReader reader = new BufferedReader(new FileReader(inputFile));
        
        int fileIdx = 0;
        long rowsInCurrentFile = 0;
        BufferedWriter writer = null;
        
        while ((line = reader.readLine()) != null) {

	    boolean esDato= !line.startsWith("#");				  if (!esDato){
		// si es dato decidimos si necesitamos un nuevo archivo antes de escribirlo
		if (writer == null || (rowsInCurrentFile > rowsPerFile && rowsInCurrentFile % linesInOriginalData == 0)) {
		    if (writer != null) writer.close();
		    
		    String filename = String.format("%s/chunk_%04d.csv", outputDir, fileIdx);
		    writer = new BufferedWriter(new FileWriter(filename));
		    fileIdx++;
		    rowsInCurrentFile = 0;
		}
	    }
            
            writer.write(line);
            writer.newLine();
	    if (esDato){
	    rowsInCurrentFile++;
	    }
	    
	    
	}
        
        if (writer != null) writer.close();
        reader.close();
        
        System.out.printf("✓ Dividido en %d archivos\n", fileIdx);
    }
}
