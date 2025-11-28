package com.proyecto;
import java.io.*;
import java.util.*;
/**
 * Clase encargada de unir archivos en un solo mega archivo.
 * <p>
 * 
 * 
 * 
 *
 * @author uwu
 * @version 0.9
 */
public class FileMerger {
    /**
     * directorio de entrada
     */
    private String inputDir;
    /**
     * directorio de salida
     */
    private String outputFile;

    /**
     * el constructor solo necesita un directorio de entrada y uno de salida
     */
    public FileMerger(String inputDir, String outputFile) {
        this.inputDir = inputDir;
        this.outputFile = outputFile;
    }

    /**
     * metodo para unir varios archivos verticalmente (apilandolos como una columna)
     */
    public void unirVerticalmente() throws IOException {
        System.out.printf("uniendo archivos verticalmente\n");
        
        File dir = new File(inputDir);
        File[] archivos = dir.listFiles((d, name) -> name.toLowerCase().endsWith(".csv"));
        
        if (archivos == null || archivos.length == 0) {
            throw new IOException("no se encontraron CSVs");
        }
        
        Arrays.sort(archivos);
        System.out.printf("Se encontraron %d archivos para unir\n", archivos.length);
        
        // crear header para archivo unido
        Header headerUnido = new Header();
        headerUnido.aniadeMetadatos("tipo", "unidos_verticalmente");
        headerUnido.aniadeMetadatos("directorio_fuente", inputDir);
        headerUnido.aniadeMetadatos("num_archivos_originales", String.valueOf(archivos.length));
        headerUnido.aniadeMetadatos("hora", new Date().toString());
        
        BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile), 8192 * 4);
        writer.write(headerUnido.aHeaderCSV());
        writer.newLine();
        
	// escribir al lista de archivos originales como comentario
        writer.write("# archivos fuente: ");
        for (int i = 0; i < Math.min(5, archivos.length); i++) {
            writer.write(archivos[i].getName() + ", ");
        }
        if (archivos.length > 5) {
            writer.write("... (" + (archivos.length - 5) + " mas)");
        }
        writer.newLine();
        
        int renglonesTotales = 0;
        for (int i = 0; i < archivos.length; i++) {
            BufferedReader reader = new BufferedReader(new FileReader(archivos[i]));
	        String filename = archivos[i].getName(); 
            String line;


	        writer.write("# datos de: " + filename);
    writer.newLine();
            // saltarse el header si hay
            line = reader.readLine();
            if (line != null && !line.startsWith("#")) {
                writer.write(line);
                writer.newLine();
                renglonesTotales++;
            }
            
            // copiar el resto del archivo
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith("#")) {  // saltarse lineas de commenteario
                    writer.write(line);
                    writer.newLine();
                    renglonesTotales++;
                }
            }
            reader.close();
            
            if ((i + 1) % 10 == 0) {
                System.out.printf("  se han unido %d/%d archivos...\n", i + 1, archivos.length);
            }
        }
        
        writer.close();
        System.out.printf("Se unieron %d archivos -> %d renglones totales\n", archivos.length, renglonesTotales);
        System.out.printf("  Resultado: %s\n", outputFile);
    }
}
