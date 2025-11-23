package com.tarea4;
import com.tarea2.tareea2.MiClaseUtil;
import java.util.Scanner;
public class Tarea4 {
    public static void main(String[] args){
	Scanner sc = new Scanner(System.in);
	System.out.println("cuantos numeros quiere promediar? (numero entero): ");
	int n = sc.nextInt();
	double[] arr = new double[n];
	System.out.println("arreglo de longitud " + n + " creado");
	System.out.println("el arreglo acepta doubles como entrada, se leeran linea por linea.");
	double scanVar;
	for (int i =0; i<n ; i++){
		System.out.println("siguiente elemento");
		scanVar=sc.nextDouble();
		arr[i] = scanVar;
	}
	System.out.println("fin lectura de elementos de arreglo, buen trabajo no alimentandole basura");	
	double prom = MiClaseUtil.promedio(arr);
	System.out.print("arreglo promediado: [");
	for (int i =0; i<n ; i++){
	    System.out.print(arr[i]+",");
	}
	System.out.println("]");
	System.out.println("promedio doubles: " + prom);
	sc.close();
    }
}
