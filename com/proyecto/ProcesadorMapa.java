package com.proyecto;

public interface ProcesadorMapa{
    // el metodo para procesar UN mapa
    public MapaParticular procesar(MapaParticular original);
    // metodo para procesar VARIOS mapas
    public ConjuntoDeMapas procesarConjunto(ConjuntoDeMapas original);
}
