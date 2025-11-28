package com.proyecto;
/**
 * Una  clase contenedora  para guardar los parametros de analisis
 * ingresados por el usuario. Esto nos permite regresar multiples valores
 * desde un unico metodo.
 */
public class FiltroConfig {
    public final String filtroLineaCelular;
    public final String filtroCalidad;
    public final double numeroDeOnda;
    public final double anchoDeBanda;

    public FiltroConfig(String cellLine, String quality, double wavenumber, double anchoDeBanda) {
        this.filtroLineaCelular = cellLine;
        this.filtroCalidad = quality;
        this.numeroDeOnda = wavenumber;
        this.anchoDeBanda = anchoDeBanda;
    }
}
