package com.proyecto;
import java.io.*;
import java.util.*;

public class SeparadorArchivos {
    private String inputFile;
    private String outputDir;
    private int numChunks;
    
    public SeparadorArchivos(String input, String output, int chunks) {
        this.inputFile = input;
        this.outputDir = output;
        this.numChunks = chunks;
    }
    
    public void dividirPorFilas() throws IOException {
        System.out.printf("Dividiendo archivo en %d chunks...\n", numChunks);
        
        // Create output directory
        File outDir = new File(outputDir);
        if (!outDir.exists()) {
            outDir.mkdirs();
        }
        
        // First, count total rows
        BufferedReader counter = new BufferedReader(new FileReader(inputFile));
        int totalRows = 0;
        while (counter.readLine() != null) {
            totalRows++;
        }
        counter.close();
        
        System.out.printf("Total de filas: %d\n", totalRows);
        
        // Calculate rows per chunk
        int rowsPerChunk = (int) Math.ceil((double) totalRows / numChunks);
        System.out.printf("Filas por chunk: %d\n", rowsPerChunk);
        
        // Now split the file
        BufferedReader reader = new BufferedReader(new FileReader(inputFile), 8192 * 4);
        
        for (int chunk = 0; chunk < numChunks; chunk++) {
            String chunkFile = outputDir + "/chunk_" + chunk + ".csv";
            BufferedWriter writer = new BufferedWriter(new FileWriter(chunkFile), 8192 * 4);
            
            System.out.printf("Creando chunk %d/%d...\n", chunk + 1, numChunks);
            
            int rowsWritten = 0;
            String line;
            while (rowsWritten < rowsPerChunk && (line = reader.readLine()) != null) {
                writer.write(line);
                writer.newLine();
                rowsWritten++;
                
                if (rowsWritten % 1000 == 0) {
                    System.out.printf("  Chunk %d: %d filas escritas\n", chunk, rowsWritten);
                }
            }
            
            writer.close();
            System.out.printf("✓ Chunk %d completado: %d filas\n", chunk, rowsWritten);
        }
        
        reader.close();
        System.out.println("✓ División completada");
    }
}
