package com.proyecto;
import java.io.*;
public class SeqHandlr extends FileHandlr{
    public SeqHandlr(String inPath, String outPath){
	super(inPath, outPath);

    }

    public ConjuntoDeMapas leerDatosCompletos(int startIndex, int endIndex) throws ErrorLectura {
        System.out.printf("Leyendo archivos %d a %d desde %s\n", startIndex, endIndex, this.caminoEntrada);
        try {
            File directorio = new File(this.caminoEntrada);
            
            if (!directorio.exists() || !directorio.isDirectory()){
                throw new ErrorLectura("Directorio no existe o no es válido", this.caminoEntrada);
            }
            
            File[] archivos = directorio.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".csv"));
            
            if (archivos == null || archivos.length == 0) {
                throw new ErrorLectura("No se encontraron archivos CSV", this.caminoEntrada);
            }
            
            // Sort to ensure consistent ordering
            java.util.Arrays.sort(archivos);
            
            // Adjust indices to array bounds
            int actualEnd = Math.min(endIndex, archivos.length);
            int batchSize = actualEnd - startIndex;
            
            System.out.printf("Batch: %d archivos (indices %d-%d de %d totales)\n", batchSize, startIndex, actualEnd-1, archivos.length);
            
            MapaParticular[] mapas = new MapaParticular[batchSize];
            
            for (int i = 0; i < batchSize; i++) {
                int archivoIdx = startIndex + i;
                try {
                    mapas[i] = leerArchivoCSV(archivos[archivoIdx], archivoIdx);
                    System.out.printf("  Mapa %d leído\n", archivoIdx);
                } catch (IOException e) {
                    throw new ErrorLectura("Error leyendo archivo: " + archivos[archivoIdx].getName(), archivos[archivoIdx].getAbsolutePath(), e);
                }
            }
            
            return new ConjuntoDeMapas(mapas);
            
        } catch (ErrorLectura e){
            throw e;
        } catch (Exception e){
            throw new ErrorLectura("Error inesperado al leer archivos", this.caminoEntrada, e);
        }
    }
    
    @Override
    public ConjuntoDeMapas leerDatosCompletos() throws ErrorLectura{
	System.out.printf("intentando leer archivos desde%s\n", this.caminoEntrada);
	try {
	    // intentar abrir los archivos
	    File directorio = new File(this.caminoEntrada);
	    File directorio2 = new File(this.caminoSalida);
	    // tierra de errores
	    if (!directorio.exists()){
		throw new ErrorLectura( "Directorio no existe", this.caminoEntrada);
	    };
	    if (!directorio.isDirectory()){
		if (directorio.isFile()){
		    System.out.printf("me diste un solo archivo, intentando leerlo\n");
		}
	        else{
		    throw new ErrorLectura( "archivo de entrada Esta raro", this.caminoEntrada);
		};       
	    };

	    if (!directorio2.exists()){
                throw new ErrorLectura( "Directorio de salida no existe", this.caminoEntrada);
            };
            if (!directorio2.isDirectory()){
                if (!directorio2.isFile()){
                    System.out.printf("me diste un solo archivo de salida, escribire la salida en solo un archivo\n");
                };
            };

	    // usar expresion lambda para solo leer .csv en el directorio
	    File[] archivos = directorio.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".csv"));
	    
	    if (archivos == null || archivos.length == 0) {
		throw new ErrorLectura("no se encontraron archivos CSV", this.caminoEntrada);
	    }
	    
	    MapaParticular[] mapas= new MapaParticular[archivos.length];	    
	    for (int i = 0; i < archivos.length; i++) {
		try {
		    mapas[i]= leerArchivoCSV(archivos[i], i);
		    System.out.printf("mapa %d leido\n",i);
		} catch (IOException e) {
		    throw new ErrorLectura("Error leyendo archivo: " + archivos[i].getName(), archivos[i].getAbsolutePath(), e);
		}
	    }

	    return new ConjuntoDeMapas(mapas);
	    // 

//	    return new ConjuntoDeMapas(new MapaParticular[] {new MapaParticular(30, new DatosEspectro(new VecNumeroDeOnda(new double[] {2.0,2.0,2.0,2.0}),new VecIntensidades( new int[]{3,2,4,5})))});
	} catch (ErrorLectura e){
	    throw e;
	} catch (Exception e){
	    throw new ErrorLectura("Error inesperado al leer archivos", this.caminoEntrada, e);
	}
    

    }
    private MapaParticular leerArchivoCSV(File archivo, int indice) throws IOException{
	// crear un lector buffereado (mas eficiente acceso a la memoria)
	BufferedReader br = new BufferedReader(new FileReader(archivo));
	// leer todas las lineas a memoria y parsearlas

 	java.util.List<String[]> filasParseadas = new java.util.ArrayList<>();
	String linea;
	while ((linea = br.readLine()) != null) {
	    filasParseadas.add(linea.split("\t"));
	}
	br.close();
	    
	if (filasParseadas.isEmpty()) {
	    throw new IOException("Archivo vacio: " + archivo.getName());
	}
    
	int numRenglones = filasParseadas.size();
	int numColumnas = filasParseadas.get(0).length;

	System.out.printf("  Archivo %s: %d filas leidas\n",archivo.getName(), numRenglones);

	// Parse first line to get number of columns
	
	// Column 0 = wave numbers (1600 values)
	double[] ondas = new double[numRenglones];
	for (int fila = 0; fila < numRenglones; fila++) {
	    ondas[fila] = Double.parseDouble(filasParseadas.get(fila)[0].trim());
	}
	VecNumeroDeOnda vecOnda = new VecNumeroDeOnda(ondas);
    
	// Columns 1-900 = intensity spectra (one MapaParticular per column)
	int numEspectros = numColumnas - 1;  // 900
	VecIntensidades[] vectoresIntensidad = new VecIntensidades[numEspectros];
    

	
	for (int col = 1; col < numColumnas; col++) {
	    int[] intensidades = new int[numRenglones];
	    
	    for (int fila = 0; fila < numRenglones; fila++) {
		intensidades[fila] = (int) Double.parseDouble(filasParseadas.get(fila)[col].trim());
	    }
	    vectoresIntensidad[col - 1] = new VecIntensidades(intensidades);

       if (col % 400 == 0) {
            System.out.printf("    [%d%%] Procesando columna %d/%d%n", 
                              (col * 100 / numColumnas), col, numColumnas);
        }
	}
	DatosEspectro espectro = new DatosEspectro(vecOnda, vectoresIntensidad);
	MapaParticular mapa = new MapaParticular(indice, espectro);

	
	System.out.printf("Mapa creado con   %d  espectros\n", espectro.ejeY.length);
	
	return mapa;
    }
    // Helper to count files without reading them
    public int contarArchivos() {
        File directorio = new File(this.caminoEntrada);

	// Get ALL files first
	File[] todosLosArchivos = directorio.listFiles();
    
    // Then filter for CSVs
	File[] archivos = directorio.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".csv"));
    
System.out.println("\n=== DEBUG: Análisis de archivos ===");
    System.out.printf("Total de archivos en directorio: %d\n", 
                     todosLosArchivos != null ? todosLosArchivos.length : 0);
    System.out.printf("Archivos CSV encontrados: %d\n", 
                     archivos != null ? archivos.length : 0);
    
    // Print non-CSV files
    if (todosLosArchivos != null && archivos != null) {
        System.out.println("\nArchivos NO-CSV en el directorio:");
        for (File f : todosLosArchivos) {
            if (f.isFile() && !f.getName().toLowerCase().endsWith(".csv")) {
                System.out.printf("  - %s (tamaño: %d bytes)\n", f.getName(), f.length());
            }
        }
        
        // Print CSV files that might be problematic
        System.out.println("\nArchivos CSV (primeros 10):");
        int count = 0;
        for (File f : archivos) {
            if (count < 10) {
                System.out.printf("  - %s (tamaño: %.2f MB)\n", 
                                 f.getName(), f.length() / (1024.0 * 1024.0));
                count++;
            }
        }
        if (archivos.length > 10) {
            System.out.printf("  ... y %d más\n", archivos.length - 10);
        }
    }
    System.out.println("=====================================\n");
    
	//	File[] archivosNo = directorio.listFiles((dir, nombre) -> !nombre.endsWith(".csv"));
	//for (
        return (archivos != null) ? archivos.length : 0;
    }
    
}
