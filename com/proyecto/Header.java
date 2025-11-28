package com.proyecto;
import java.util.*;

/**
 * Clase que contiene la logica de la informacion de los headers, desde parsearla a escribirla.
 * <p>
 *
 * @author uwu
 * @version 0.9
 */

public class Header {
    /**
     * un mapa entre una llave y un valor
     */
    private Map<String, String> metadata;

    /**
     * el constructor inicializa un hashmap vacio
     */
    public Header() {
        this.metadata = new HashMap<>();
    }

    /**
     * aniade al hashmap una entrada llave valor
     */
    public void aniadeMetadatos(String llave, String valor) {
        metadata.put(llave, valor);
    }

    /**
     * adquiere el valor asociado a una llave
     */
    public String getMetadatos(String llave) {
        return metadata.get(llave);
    }
    /**
     * escribe las entradas del mapa a un string y regresa el string resultante
     */
    public String aHeaderCSV() {
        // Format: # key1=value1; key2=value2; key3=value3
        StringBuilder sb = new StringBuilder("# ");
        for (Map.Entry<String, String> entrada : metadata.entrySet()) {
            sb.append(entrada.getKey()).append("=").append(entrada.getValue()).append("; ");
        }
        return sb.toString().trim();
    }
    /**
     * parsea un comentario en el csv y lo aniade como metadatos al header actual 
     */
    public static Header headerDeCSV(String linea) {
        Header h = new Header();
        if (linea.startsWith("# ")) {
            String contenido = linea.substring(2);
            String[] pares = contenido.split("; ");
            for (String par : pares) {
                String[] kv = par.split("=", 2);
                if (kv.length == 2) {
                    h.aniadeMetadatos(kv[0].trim(), kv[1].trim());
                }
            }
        }
        return h;
    }
}
