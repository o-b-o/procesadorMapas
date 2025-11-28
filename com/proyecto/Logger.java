package com.proyecto;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * clase encargada de loggear todo
 */
public class Logger {
    private static final String LOG_FILE = "analizador.log";
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    /**
     * constructor privado para evitar instanciacion
     */
    private Logger() {}

    /**
     * loggea un mensaje a la consola y a un archivo persistente
     * este metodo esta sincronizado para poder usarse con hilos
     *
     * @param nivel   nivel del mensaje
     * @param mensaje mensaje a loggear
     */
    public static synchronized void log(LogLevel nivel, String mensaje) {
        String tiempo = dateFormat.format(new Date());
        String nombreDeHilo = Thread.currentThread().getName();
        String entradaDeLog = String.format("[%s] [%s] [%s]: %s", tiempo, nombreDeHilo, nivel, mensaje);

        // siempre imprimir a consola
        if (nivel == LogLevel.ERROR) {
            System.err.println(entradaDeLog);
        } else {
            System.out.println(entradaDeLog);
        }

        // escribir al log
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(entradaDeLog);
            writer.newLine();
        } catch (IOException e) {
            // si el logging falla, gran popo
            // imprime el error de log ala consola
            System.err.println("TERRIVLE: no se pudo escribir en el log! uwu!");
            e.printStackTrace();
        }
    }
}
