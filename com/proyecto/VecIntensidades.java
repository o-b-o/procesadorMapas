package com.proyecto;
/**
 * Clase que representa un vector de intensidades para un espectro de Raman. (se puede pensar como la segunda coordenada de la dupla (k, I(k))
 * <p>
 * Esta clase permite crear objetos que representan intensidades medidas por un detector dentro de un rango de numeros de onda (1600 numeros de onda -> 1600 intensidades (k,I(k)). 
 * 
 * @author uwu
 * 
 */
public class VecIntensidades{
    /**
     * arreglo de intensidades de igual longitud que el arreglo de numeros de onda (en un conjunto de datos bien formado).
     */
    private final int[] intensidadVec;   
    /**
     * El constructor espera un arreglo de enteros, el unico miembro de la clase
     */
    public VecIntensidades(int[] intVec){
	intensidadVec = intVec.clone();
    }
    /**
     * Getter de intensidad, regresa todo el vector
     */
    public int[] getVecIntensidades(){
	return this.intensidadVec;
    };
    /**
     * Getter de intensidad en un indice particular, regresa solo una intensidad.
     */
    public int getIntensidadEnIndice(int indice){
	return this.intensidadVec[indice];
    };

}
