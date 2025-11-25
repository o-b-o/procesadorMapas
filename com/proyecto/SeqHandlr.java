// REFACTORED: SeqHandlr.java
package com.proyecto;
import java.io.*;
import java.util.*;

public class SeqHandlr extends FileHandlr {
    
    public SeqHandlr(String inPath, String outPath){
        super(inPath, outPath);
    }
    
    @Override
    public ConjuntoDeMapas leerDatosCompletos() throws ErrorLectura {
        return leerDatosCompletos(0, Integer.MAX_VALUE);
    }
    
    public ConjuntoDeMapas leerDatosCompletos(int startIndex, int endIndex) throws ErrorLectura {
        System.out.printf("Leyendo archivos %d-%d desde %s\n", startIndex, endIndex, this.caminoEntrada);
        
        try {
            File directorio = new File(this.caminoEntrada);
            
            if (!directorio.exists() || !directorio.isDirectory()) {
                throw new ErrorLectura("Directorio inválido", this.caminoEntrada);
            }
            
            File[] archivos = directorio.listFiles((dir, nombre) -> 
                nombre.toLowerCase().endsWith(".csv")
            );
            
            if (archivos == null || archivos.length == 0) {
                throw new ErrorLectura("No CSV files found", this.caminoEntrada);
            }
            
            Arrays.sort(archivos);
            
            int actualEnd = Math.min(endIndex, archivos.length);
            int batchSize = actualEnd - startIndex;
            
            MapaParticular[] mapas = new MapaParticular[batchSize];
            
            for (int i = 0; i < batchSize; i++) {
                int archivoIdx = startIndex + i;
                mapas[i] = leerArchivoCSV(archivos[archivoIdx], archivoIdx);
            }
            
            return new ConjuntoDeMapas(mapas);
            
        } catch (ErrorLectura e) {
            throw e;
        } catch (Exception e) {
            throw new ErrorLectura("Error inesperado", this.caminoEntrada, e);
        }
    }
    
    private MapaParticular leerArchivoCSV(File archivo, int indice) throws IOException {
        BufferedReader br = new BufferedReader(new FileReader(archivo));
        
        // Read header if present
        String firstLine = br.readLine();
        Header header = null;
        if (firstLine != null && firstLine.startsWith("#")) {
            header = Header.fromCSVHeader(firstLine);
            firstLine = br.readLine();  // Read actual first data line
        }
        
        List<String[]> filasParseadas = new ArrayList<>();
        if (firstLine != null && !firstLine.startsWith("#")) {
            filasParseadas.add(firstLine.split("\t"));
        }
        
        String linea;
        while ((linea = br.readLine()) != null) {
            if (!linea.startsWith("#")) {
                filasParseadas.add(linea.split("\t"));
            }
        }
        br.close();
        
        // ... rest of parsing logic (unchanged)
        
        int numRenglones = filasParseadas.size();
        int numColumnas = filasParseadas.get(0).length;
        
        double[] ondas = new double[numRenglones];
        for (int fila = 0; fila < numRenglones; fila++) {
            ondas[fila] = Double.parseDouble(filasParseadas.get(fila)[0].trim());
        }
        VecNumeroDeOnda vecOnda = new VecNumeroDeOnda(ondas);
        
        int numEspectros = numColumnas - 1;
        VecIntensidades[] vectoresIntensidad = new VecIntensidades[numEspectros];
        
        for (int col = 1; col < numColumnas; col++) {
            int[] intensidades = new int[numRenglones];
            for (int fila = 0; fila < numRenglones; fila++) {
                intensidades[fila] = (int) Double.parseDouble(filasParseadas.get(fila)[col].trim());
            }
            vectoresIntensidad[col - 1] = new VecIntensidades(intensidades);
        }
        
        DatosEspectro espectro = new DatosEspectro(vecOnda, vectoresIntensidad);
        return new MapaParticular(indice, espectro);
    }
    
    public int contarArchivos() {
        File directorio = new File(this.caminoEntrada);
        File[] archivos = directorio.listFiles((dir, nombre) -> 
            nombre.toLowerCase().endsWith(".csv")
        );
        return (archivos != null) ? archivos.length : 0;
    }
    
    // REMOVED: All splitting/merging methods (moved to utilities)
}
