// excepcion base
package com.proyecto;

public class ErrorProcesamiento extends Exception {
    public ErrorProcesamiento(String mensaje) {
        super(mensaje);
    }
    
    public ErrorProcesamiento(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
