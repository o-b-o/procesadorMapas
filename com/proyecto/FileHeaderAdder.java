// NEW CLASS: FileHeaderAdder.java
package com.proyecto;
import java.io.*;
import java.util.List;
import java.util.ArrayList;
public class FileHeaderAdder {
    public static void addHeadersToDirectory(String directory) throws IOException {
        File dir = new File(directory);
        File[] files = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));
        
        if (files == null) return;
        
        System.out.printf("Adding headers to %d files...\n", files.length);
        
        for (File file : files) {
            addHeaderToFile(file);
        }
        
        System.out.println("✓ Headers added");
    }
    
    private static void addHeaderToFile(File file) throws IOException {
        // Read existing content
        BufferedReader reader = new BufferedReader(new FileReader(file));
        List<String> lines = new ArrayList<>();
        String line;
        while ((line = reader.readLine()) != null) {
            lines.add(line);
        }
        reader.close();
        
        // Skip if already has header
        if (!lines.isEmpty() && lines.get(0).startsWith("#")) {
            return;
        }
        
        // Create header
        Header header = new Header();
        header.addMetadata("filename", file.getName());
        header.addMetadata("rows", String.valueOf(lines.size()));
        header.addMetadata("cols", String.valueOf(lines.isEmpty() ? 0 : lines.get(0).split("\t").length));
        header.addMetadata("type", "original");
        header.addMetadata("timestamp", new java.util.Date().toString());
        
        // Write back with header
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        writer.write(header.toCSVHeader());
        writer.newLine();
        for (String l : lines) {
            writer.write(l);
            writer.newLine();
        }
        writer.close();
    }
}
