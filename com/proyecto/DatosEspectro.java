package com.proyecto;
/**
 * Una clase que representa los datos un espectro (el conjunto de duplas (k[i],I(k[i]))) o los datos de un mapa entero (k[i], I(k[i])[j]) donde j se encuentra entre 0 y 900-1 = [0,29]x[0,29].
 * <p>
 * Esta clase es el contenedor fundamental de computo, tiene dos vectores (que tienen
 * que tener la misma longitud) el vector numero de onda (eje X) representa la 
 * variable independiente. El eje Y es el vector de vectores de intensidades. Este 
 * vector puede tener un solo elementro (cuando los datos son de un espectro singular)
 * pero en general es un arreglo de 900 elementos. Cada entrada de el arreglo
 * representa una posicion (x,y) en el microscopio.
 * el mappeo entre una entrada en el vector de intensidades I[m] y una posicion
 * geometrica en el espacio de microscopio es similar a un scanline
 * I[m] = (m % 30 , floor(m/30)) // porque? porque m%30 nos da la proyeccion a renglon
 * y floor(m/30) nos dice cuantas veces hemos sobrepasado 30 (que es el ancho de
 * muestreo)
 * @author uwu
 * @version 0.9
 */

public class DatosEspectro{
    /**
     * vector de numeros de onda del espectro
     */
    public VecNumeroDeOnda ejeX;
    /**
     * Vector de vectores intensidad del espectro. Contiene 900 instancias de intensidades de espectros (1600 valores) por mapa. (cuando se carga un mapa comleto)
     */
    public VecIntensidades[] ejeY;

    /**
     * Constructor cuando solo se tiene un conjunto de intensidades (un espectro)
     */
    public DatosEspectro(VecNumeroDeOnda x, VecIntensidades y){
	this.ejeX=x;
	this.ejeY=new VecIntensidades[]{y};
    }
    /**
     * Constructor cuando se tiene un arreglo de vectores intensidad (muchos espectros)
     */
    public DatosEspectro(VecNumeroDeOnda x, VecIntensidades[] y){
	this.ejeX=x;
	this.ejeY=y;
    }
    /**
     * getter de vector de numero de onda
     */
    public VecNumeroDeOnda getVecNumeroOnda(){
	return this.ejeX;
    }
    /**
     * getter de arreglo de vectores intensidad
     */
    public VecIntensidades[] getVecVecIntensidades(){
	return this.ejeY;
    }
    /**
     * getter de arreglo de vectores cuando se tiene solo un vector de intensidades (regresa el primero en el arreglo de vectores intensidad).
     */   
    public VecIntensidades getVecIntensidades(){
	return this.ejeY[0];
    }
}
