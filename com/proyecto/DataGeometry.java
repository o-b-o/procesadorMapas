package com.proyecto;

public class DataGeometry{
    private final int longitudEjeX;
    private final int longitudEjeY;
    public DataGeometry(int x, int y){
	longitudEjeX = x;
	longitudEjeY = y;
    }
    public int getLongitudX(){
	return this.longitudEjeX;
    };
    public int getLongitudY(){
	return this.longitudEjeY;
    };
}
