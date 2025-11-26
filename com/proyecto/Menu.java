package com.proyecto;

import java.util.Scanner;
public class Menu {
    private Menu(){	
    }

    public static String getUserInput(Scanner scanner){
	String opcion = scanner.nextLine();

	return opcion;
    }
    public static int getUserInt(Scanner scanner) {
    while (true) { // Loop until we get a valid number
        String line = scanner.nextLine(); // Always read the whole line
        try {
            return Integer.parseInt(line); // Try to convert it
        } catch (NumberFormatException e) {
            System.out.print("Entrada inválida. Por favor ingrese un número: ");
        }
    }}
    public static double getUserDouble(Scanner scanner) {
    while (true) { // Loop until we get a valid number
        String line = scanner.nextLine(); // Always read the whole line
        try {
            return Double.parseDouble(line); // Try to convert it
        } catch (NumberFormatException e) {
            System.out.print("Entrada inválida. Por favor ingrese un número: ");
        }
    }
    }

    

}
