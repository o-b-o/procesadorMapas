package com.proyecto;

/**
 * Una clase envoltorio de MapaParticular que sirve para guardar el nombre asociado al mapa (el filename).
 * <p>
 * Un envoltorio de datos. Util para evitar refactorizar todas las clases que usan al original.... y rediseniar toda la logica de una seccion del procesador..
 * simple, una clase nueva con el miembro que necesitamos porque sufrir??? porrqueeeE???
 * @see MapaParticular
 * @author uwu
 * @version 0.9
 */
public class MapaConNombre {
    /**
     * Mapa especifico que contiene los datos
     */
    public final MapaParticular mapa;
    /**
     * Nombre del mapa (filename)
     */
    public final String originalFilename;
    /**
     * el constructor requiere un mapa y un nombre
     */
    public MapaConNombre(MapaParticular mapa, String originalFilename) {
        this.mapa = mapa;
        this.originalFilename = originalFilename;
    }
}
