package com.proyecto;

import java.util.Scanner;

/**
 * clase de utilidad para leer datos del usuario por stdin con proteccion de ingreso invalido
 * <p>
 * Menu es un envoltorio de scanner con protecciones para entradas invalidas del usuario
 * 
 * 
 *
 * @author uwu

 */

public class Menu {
    /**
     * clase de utilidad a base de metodos estaticos, tiene constructor privado (y vacio)
     */
    private Menu(){	
    }

    /**
     * funcion para leer entrada de usuario (strings)... bastante inutil..
     */
    public static String getUserInput(Scanner scanner){
	String opcion = scanner.nextLine();

	return opcion;
    }
    /**
     * funcion para leer enteros desde la entrada de usuario
     * necesaria porque algunos usuarios son picaros y les gusta no seguir instrucciones
     * esta funcion se encarga de que el programa no explote si el usuario mete i en vez de 1
     * ... bastante util
     */
    public static int getUserInt(Scanner scanner) {
    while (true) { // repetir hasta tener numero valido
        String line = scanner.nextLine(); // leer toda la linea
        try {
            return Integer.parseInt(line); // intentar convertir a int
        } catch (NumberFormatException e) {
            System.out.print("Entrada invalida. Por favor ingrese un numero: ");
        }
    }}
    /**
     * identica a getUserInt pero con dobles
     */
    public static double getUserDouble(Scanner scanner) {
    while (true) {
        String line = scanner.nextLine();
        try {
            return Double.parseDouble(line); 
        } catch (NumberFormatException e) {
            System.out.print("Entrada invalida. Por favor ingrese un numero: ");
        }
    }
    }

    public static double getUserDouble(Scanner scanner, double defaultv) {
 String line = scanner.nextLine(); // Lee la entrada del usuario una vez.

  
	try {
        // Intenta convertir la línea a un double.
        // El trim() es importante para manejar espacios en blanco.
        // Si la línea está vacía, esto lanzará una excepción.
        return Double.parseDouble(line.trim());
    } catch (NumberFormatException e) {
        // La conversión falló. Esto ocurre si la línea es texto,
        // está vacía, o tiene un formato numérico incorrecto.
        
        System.out.printf("Entrada inválida. Usando el valor por defecto: %.2f\n", defaultv);
        return defaultv; // Asigna el valor por defecto y continúa.
    }
    }
    

}
