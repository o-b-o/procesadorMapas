package com.proyecto;

/**
 * Clase excepcion de procesamiento base
 *
 */
public class ErrorProcesamiento extends Exception {
    public ErrorProcesamiento(String mensaje) {
        super(mensaje);
    }
    
    public ErrorProcesamiento(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
