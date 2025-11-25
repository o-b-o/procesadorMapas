// NEW CLASS: FileMerger.java
package com.proyecto;
import java.io.*;
import java.util.*;

public class FileMerger {
    private String inputDir;
    private String outputFile;
    
    public FileMerger(String inputDir, String outputFile) {
        this.inputDir = inputDir;
        this.outputFile = outputFile;
    }
    
    public void mergeVertically() throws IOException {
        System.out.println("=== MERGING FILES VERTICALLY ===");
        
        File dir = new File(inputDir);
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));
        
        if (files == null || files.length == 0) {
            throw new IOException("No CSV files found");
        }
        
        Arrays.sort(files);
        System.out.printf("Found %d files to merge\n", files.length);
        
        // Create header for merged file
        Header mergedHeader = new Header();
        mergedHeader.addMetadata("type", "merged_vertical");
        mergedHeader.addMetadata("source_dir", inputDir);
        mergedHeader.addMetadata("num_source_files", String.valueOf(files.length));
        mergedHeader.addMetadata("timestamp", new Date().toString());
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile), 8192 * 4);
        writer.write(mergedHeader.toCSVHeader());
        writer.newLine();
        
        // Write source file list as comment
        writer.write("# Source files: ");
        for (int i = 0; i < Math.min(5, files.length); i++) {
            writer.write(files[i].getName() + ", ");
        }
        if (files.length > 5) {
            writer.write("... (" + (files.length - 5) + " more)");
        }
        writer.newLine();
        
        int totalRows = 0;
        for (int i = 0; i < files.length; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(files[i]));
	        String filename = files[i].getName(); // <-- HERE is how you get the name
            String line;


	        writer.write("# DATA FROM: " + filename);
    writer.newLine();
            // Skip header if present
            line = reader.readLine();
            if (line != null && !line.startsWith("#")) {
                writer.write(line);
                writer.newLine();
                totalRows++;
            }
            
            // Copy rest of file
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {  // Skip comment lines
                    writer.write(line);
                    writer.newLine();
                    totalRows++;
                }
            }
            reader.close();
            
            if ((i + 1) % 100 == 0) {
                System.out.printf("  Merged %d/%d files...\n", i + 1, files.length);
            }
        }
        
        writer.close();
        System.out.printf("✓ Merged %d files → %d total rows\n", files.length, totalRows);
        System.out.printf("  Output: %s\n", outputFile);
    }
}
