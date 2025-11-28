package com.proyecto;
/**
 * Esta clase originalmente contenia la informacion de los datos originales, pero evoluciono para contener la configuracion general del programa provista por el usuario.
 * <p>
 * La informacion de esta clase es utilizada por el resto de las clases durante el
 * tiempo de ejecucion como referencia para el tamanio de sus arreglos y demas
 * 
 * 
 *
 * @author uwu
 * @version 0.9
 */

public class DataGeometry{
    /**
     * cuantas columnas hay en el el dataset de un mapa singular (900 + 1 valor de numero de onda) [no es crucial tenerlo bien]
     */
    private final int longitudEjeX;
    /**
     * cuantos renglones hay en el dataset de un mapa singular (1600) (una intensidad por numero de onda muestreado)
     */ 
    private final int longitudEjeY;
    /**
     *
     * cuantos espectros hay en un lado del cuadrado de muestreo (900 = 30x30)
     */

    public final int especPorLadoMapa = 30;
    /**
     * directorio de entrada, donde se encuentran los datos a procesar
     */
    public final String inputDir;
    /**
     * directorio de salida, donde se escribe el resultado de los procesos
     */
    public final String outputDir;
    /**
     * constructor de compatibilidad, asigna vacio al directorio de entrada y salida
     */
    public DataGeometry(int x, int y){
	longitudEjeX = x;
	longitudEjeY = y;
	inputDir = "";
	outputDir = "";
    }
    /**
     * constructor atual, toma las dimensiones de entrada de los datos originales y los directorios de entrada y salida.
     */
    public DataGeometry(int x, int y, String inDir, String outDir){
	longitudEjeX = x;
	longitudEjeY = y;
	inputDir = inDir;
	outputDir = outDir;
    }
    /**
     * adquiere el numero de columnas de los datos originales (900)
     */
    public int getLongitudX(){
	return this.longitudEjeX;
    };
    /**
     * adquiere el numero de renglones de los datos originales (1600)
     */
    public int getLongitudY(){
	return this.longitudEjeY;
    };
}
