package com.proyecto;

public class ConjuntoDeMapas{
    public MapaParticular[] arregloDeMapas;
        
    public ConjuntoDeMapas(MapaParticular[] arregloDeMapas){
	this.arregloDeMapas=arregloDeMapas;

    }
    public MapaParticular getMapaWithIndex(int indx){
	return arregloDeMapas[indx];
    }

}
