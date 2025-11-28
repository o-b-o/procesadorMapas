package com.proyecto;

/**
 * Clase que representa un vector de numeros de onda. Se puede pensar como la primera coordenada en la dupla (k, I(k)).
 * <p>
 * Esta clase se utiliza para crear el objeto que representa la variable dependiente en el experimento.
 * Contiene numeros de onda de los fotones de Raman (en teoria es identico en todos los mapas.. eso queda por verse..)
 * 
 *
 * @author uwu
 */
public class VecNumeroDeOnda{
    /**
     * Este miembro contiene los numeros de onda en un arreglo, (varian entre -300.0 a 3000.0+ ). En un conjunto de datos bien formado, tiene la misma cardinalidad que cada elemento del arreglo de vectores de intensidad VecIntensidad
     * @see VecIntensidad
     */
    private final double[] numeroDeOnda;
    

    /**
     * el constructor clona el arreglo de entrada para evitar problemas
     * de aliasing.
     */
    public VecNumeroDeOnda(double[] numOndaVec){
	numeroDeOnda = numOndaVec.clone();
    }

    /**
     * adquiere el numero de onda en un indice dado del vector numero de onda
     */
    public double getVecNumeroDeOndaEnIndice(int indice){
	return this.numeroDeOnda[indice];
    }
    /**
     *adquiere el vector numero de onda
     */	
    public double[] getVecNumeroDeOnda(){
	return this.numeroDeOnda;
    };
}
