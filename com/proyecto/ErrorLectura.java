
package com.proyecto;
/**
 * Clase de error de lectura
 */
public class ErrorLectura extends ErrorProcesamiento{
    private String nombreArchivo;
    
    public ErrorLectura(String mensaje, String archivo) {
        super(mensaje);
        this.nombreArchivo = archivo;
    }
    
    public ErrorLectura(String mensaje, String archivo, Throwable causa) {
        super(mensaje, causa);
        this.nombreArchivo = archivo;
    }
    
    public String getNombreArchivo() {
        return this.nombreArchivo;
    }
}
