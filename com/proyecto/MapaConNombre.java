package com.proyecto;

/**
 * A simple container class (DTO) to temporarily hold a MapaParticular
 * and its original source filename together during processing.
 */
public class MapaConNombre {

    public final MapaParticular mapa;
    public final String originalFilename;

    public MapaConNombre(MapaParticular mapa, String originalFilename) {
        this.mapa = mapa;
        this.originalFilename = originalFilename;
    }
}
