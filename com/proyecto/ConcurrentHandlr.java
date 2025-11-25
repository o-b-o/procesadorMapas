package com.proyecto;
import java.io.*;
public class ConcurrentHandlr extends FileHandlr {
    public ConcurrentHandlr(String inPath, String outPath){
	super(inPath,outPath);
    }
    @Override
    public ConjuntoDeMapas leerDatosCompletos() throws ErrorLectura{
	System.out.printf("concurrenr file handling lol\n");
	try {
            File directorio = new File(this.caminoEntrada);
	    File directorio2 = new File(this.caminoSalida);
            if (!directorio.exists()){
		throw new ErrorLectura( "Directorio de entrada no existe", this.caminoEntrada);
            };
            if (!directorio.isDirectory()){
                if (directorio.isFile()){
                    System.out.printf("me diste un solo archivo de entrada, intentando leerlo\n");
		}else{
		    throw new ErrorLectura( "archivo de entrada esta raro", this.caminoEntrada);
		}
	    };
            if (!directorio2.exists()){
		throw new ErrorLectura( "Directorio de salida no existe", this.caminoEntrada);
            };
            if (!directorio2.isDirectory()){
                if (!directorio2.isFile()){
                    System.out.printf("me diste un solo archivo de salida, escribire la salida en solo un archivo\n");
		};
	    };
		
	    
	    return new ConjuntoDeMapas(new MapaParticular[] {new MapaParticular(30, new DatosEspectro(new VecNumeroDeOnda(new double[] {2.0,2.0,2.0,2.0}),new VecIntensidades( new int[]{3,2,4,5})))});
	} catch(ErrorLectura e){
	    throw e;}
	catch (Exception e){
	    throw new ErrorLectura("Error inesperado al leer archivos", this.caminoEntrada, e);
	}
    }
    

}
