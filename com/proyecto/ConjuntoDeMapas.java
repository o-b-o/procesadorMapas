package com.proyecto;
/**
 * Esta clase representa un conjunto de mapas. Puede ser todos los datos en una base de datos, o un subconjunto elegido bajo cualquier criterio.
 * <p>
 * Cada objeto ConjuntoDeMapas tiene un arreglo de MapaParticular.
 * 
 *
 *
 * @author uwu
 * @version 0.9
 */

public class ConjuntoDeMapas{
    /**
     * arreglo de mapas .. sencillo
     */
    public MapaParticular[] arregloDeMapas;

    /**
     * el constructor requiere un arreglo de mapas particulares
     */
    public ConjuntoDeMapas(MapaParticular[] arregloDeMapas){
	this.arregloDeMapas=arregloDeMapas;

    }
    /**
     * regresa el mapa con el indice pedido
     */
    public MapaParticular getMapaConIndice(int indx){
	return arregloDeMapas[indx];
    }

}
