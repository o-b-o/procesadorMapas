package com.proyecto;

public class DatosEspectro{
    public VecNumeroDeOnda ejeX;
    public VecIntensidades[] ejeY;
    
    public DatosEspectro(VecNumeroDeOnda x, VecIntensidades y){
	this.ejeX=x;
	this.ejeY=new VecIntensidades[]{y};
    }
    public DatosEspectro(VecNumeroDeOnda x, VecIntensidades[] y){
	this.ejeX=x;
	this.ejeY=y;
    }

    public VecNumeroDeOnda getVecNumeroOnda(){
	return this.ejeX;
    }
    
    public VecIntensidades[] getVecVecIntensidades(){
	return this.ejeY;
    }
    
    public VecIntensidades getVecIntensidades(){
	return this.ejeY[0];
    }
}
