package com.proyecto;

public class VecIntensidades{
    private final int[] intensidadVec;   

    public VecIntensidades(int[] intVec){
	intensidadVec = intVec.clone();
    }
    public int[] getVecIntensidades(){
	return this.intensidadVec;
    };
    public int getIntensidadAtIndex(int indice){
	return this.intensidadVec[indice];
    };

}
