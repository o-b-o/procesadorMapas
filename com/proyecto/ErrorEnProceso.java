package com.proyecto;
public class ErrorEnProceso extends ErrorProcesamiento {
    private int mapaIndex;
    
    public ErrorEnProceso(String mensaje, int index) {
        super(mensaje);
        this.mapaIndex = index;
    }
    
    public ErrorEnProceso(String mensaje, int index, Throwable causa) {
        super(mensaje, causa);
        this.mapaIndex = index;
    }
    
    public int getMapaIndex() {
        return this.mapaIndex;
    }
}
