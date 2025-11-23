package com.proyecto;
public class SelectorSimple implements SelectorDeMapas{
    private String programa;
    public SelectorSimple(String programa){
	this.programa = programa;
	
    };
    @Override
    public ConjuntoDeMapas seleccionar(ConjuntoDeMapas original, int[] listaDeIndices){
	System.out.printf("selecc map %d, wif %d in the third index \n", original.getMapaWithIndex(listaDeIndices[0]).datos.getVecIntensidades().getIntensidadAtIndex(3), listaDeIndices[0]);
	return original;
    }
}
