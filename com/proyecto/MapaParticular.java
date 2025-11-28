package com.proyecto;
/**
 * Una clase que representa el mapa de una celula en particular
 * <p>
 * Contiene un indice y un DatosEspectro que a su vez tiene un vector numero de onda
 * en conjunto con un vector de vectores intensidad, un espectro por punto del mapa. 
 * 
 *
 * @author uwu
 * @version 0.9
 */
public class MapaParticular{
    /**
     * indice identificador unico del mapa
     */
    public int indice;
    /**
     * datos espectrales del mapa
     */
    public DatosEspectro datos;

    /**
     * el constructor requiere un indice y unos datos para funcionar
     */
    public MapaParticular(int indx, DatosEspectro datos){
	this.indice=indx;
	this.datos=datos;
    }

}
