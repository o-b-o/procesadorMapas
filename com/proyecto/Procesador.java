package com.proyecto;

public class Procesador<T extends ProcesadorMapa, U extends SelectorDeMapas>{
    private String params;
    private T procesadorMapa;
    private U selector;

    public Procesador(String params, T t, U u){
	this.params=params;
	this.procesadorMapa = t;
	this.selector=u;
    };
    public ConjuntoDeMapas ejecutarProceso(ConjuntoDeMapas entrada) throws ErrorEnProceso, ErrorProcesamiento{
	try{
	    System.out.printf("==================\n");
	    System.out.printf("====inicio Proceso\n");
	    
	    System.out.printf("==================\n");
	    System.out.printf("===indexando mapas\n");
	    int lenn= entrada.arregloDeMapas.length;
	    int [] indicesEntrada = new int[lenn];
	    
	    for (int i = 0; i < lenn; i++){
		indicesEntrada[i]=i;
		System.out.printf("%d", i);
	    }
	    System.out.printf("\n");
	    System.out.printf("==================\n");
	    System.out.printf("seleccionando mapas\n");
	    ConjuntoDeMapas seleccionado = selector.seleccionar(entrada, indicesEntrada);
	    int lenns = seleccionado.arregloDeMapas.length;
	    System.out.printf("==================\n");
	    System.out.printf("procesando mapas seleccionados\n");
	    MapaParticular[] procesados = new MapaParticular[lenns];
	    
	    for (int i = 0; i < lenns; i++){
		System.out.printf("i:%d", i);
		procesados[i] = procesadorMapa.procesar(seleccionado.arregloDeMapas[i]);
	    }
	    System.out.printf("==================\n");
	    System.out.printf("fin proceso\n");
	    ConjuntoDeMapas resultado = new ConjuntoDeMapas(procesados);
	    System.out.printf("==================\n");
	    System.out.printf("se procesaron: %d mapas\n", lenns);
	    if (resultado == null){
		throw new ErrorEnProceso("no c pudo uwu",1);
	    
	    }
	    return resultado;
	}catch(ErrorEnProceso e){
	    throw e;
	}
	catch (Exception e){
	    throw new ErrorProcesamiento("Error inesperado al procesar mapa", e);
	}
    }
}
