package com.proyecto;

public class VecNumeroDeOnda{
    private final double[] numeroDeOnda;
    

    public VecNumeroDeOnda(double[] numOndaVec){
	numeroDeOnda = numOndaVec.clone();
    }
    public double[] getVecNumeroDeOnda(){
	return this.numeroDeOnda;
    };
    public double getVecNumeroDeOndaAtIndex(int indice){
	return this.numeroDeOnda[indice];
    };

}
