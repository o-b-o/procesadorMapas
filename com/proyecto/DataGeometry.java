package com.proyecto;

public class DataGeometry{
    private final int longitudEjeX;
    private final int longitudEjeY;
    public final int especPorLadoMapa = 30;
    public final String inputDir;
    public final String outputDir;
    public DataGeometry(int x, int y){
	longitudEjeX = x;
	longitudEjeY = y;
	inputDir = "";
	outputDir = "";
    }
    public DataGeometry(int x, int y, String inDir, String outDir){
	longitudEjeX = x;
	longitudEjeY = y;
	inputDir = inDir;
	outputDir = outDir;
    }
    public int getLongitudX(){
	return this.longitudEjeX;
    };
    public int getLongitudY(){
	return this.longitudEjeY;
    };
}
