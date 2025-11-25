package com.proyecto;
public class ProcesoConcurrente implements ProcesadorMapa{
    private String programa;
    public ProcesoConcurrente(String programa){
	this.programa = programa;
	
    };
     public ConjuntoDeMapas procesarConjunto(ConjuntoDeMapas original){
	System.out.printf("concurrent Multimap uwu %d \n", original.getMapaWithIndex(0).datos.getVecIntensidades().getIntensidadAtIndex(3));
	return original;
    }
    @Override
public MapaParticular procesar(MapaParticular original) {
    if (this.programa.startsWith("extraer_espectro:")) {
        int espectroIdx = Integer.parseInt(this.programa.split(":")[1]);
        return extraerEspectro(original, espectroIdx);
        
    } else if (this.programa.startsWith("filtrar:")) {
        String[] range = this.programa.split(":")[1].split("-");
        double min = Double.parseDouble(range[0]);
        double max = Double.parseDouble(range[1]);
        return filtrarPorOnda(original, min, max);
    }
    
    return original;
}

private MapaParticular extraerEspectro(MapaParticular mapa, int idx) {
    if (idx >= mapa.datos.ejeY.length) {
        System.err.printf("Índice %d fuera de rango\n", idx);
        return mapa;
    }
    
    VecIntensidades espectro = mapa.datos.ejeY[idx];
    DatosEspectro datos = new DatosEspectro(mapa.datos.ejeX, espectro);
    return new MapaParticular(mapa.indice, datos);
}

private MapaParticular filtrarPorOnda(MapaParticular mapa, double min, double max) {
    // Filter wavenumbers
    VecNumeroDeOnda ondas = mapa.datos.ejeX;
    java.util.List<Integer> indices = new java.util.ArrayList<>();
    
    for (int i = 0; i < ondas.getVecNumeroDeOnda().length; i++) {
        double onda = ondas.getVecNumeroDeOndaAtIndex(i);
        if (onda >= min && onda <= max) {
            indices.add(i);
        }
    }
    
    // Extract filtered data
    double[] ondasFiltradas = new double[indices.size()];
    for (int i = 0; i < indices.size(); i++) {
        ondasFiltradas[i] = ondas.getVecNumeroDeOndaAtIndex(indices.get(i));
    }
    
    VecIntensidades[] intensidadesFiltradas = new VecIntensidades[mapa.datos.ejeY.length];
    for (int j = 0; j < mapa.datos.ejeY.length; j++) {
        int[] valoresFiltrados = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            valoresFiltrados[i] = mapa.datos.ejeY[j].getIntensidadAtIndex(indices.get(i));
        }
        intensidadesFiltradas[j] = new VecIntensidades(valoresFiltrados);
    }
    
    DatosEspectro datosFiltrados = new DatosEspectro(
        new VecNumeroDeOnda(ondasFiltradas),
        intensidadesFiltradas
    );
    
    return new MapaParticular(mapa.indice, datosFiltrados);
}
}
