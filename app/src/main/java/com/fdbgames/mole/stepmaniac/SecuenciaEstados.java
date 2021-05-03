package com.fdbgames.mole.stepmaniac;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecuenciaEstados {
    private static final String TAG = "ecnecia_Estados";
    Estado estadoActual;
    List listAnimacionNormal;
    Estado desaparece;
    Estado sinSalir;
    Estado saliendo;
    Estado afuera;
    Estado golpeado;
    Estado estrellas;
    int idSecuencia;
    public static final int NORMAL =1;
    public static final int GOLPE = 2;
    int[] arrayEstadosNormal = {Estado.DESAPARECE,Estado.SIN_SALIR,Estado.SALIENDO,Estado.AFUERA,Estado.SALIENDO,Estado.SIN_SALIR,Estado.DESAPARECE,Estado.DESAPARECE};
    int[] arrayEstadosGolpe = {Estado.DESAPARECE,Estado.GOLPEADO,Estado.ESTRELLAS,Estado.DESAPARECE,Estado.DESAPARECE};/// toco hacer la chambonada del doble estado al ultimo para que mostrara
    /// la secuencia completa
    int indiceArray;
    boolean finalizada = false;
    boolean regresarANormal = false;
    Map<Integer,Estado> mapEstados;

    public Map<Integer,Estado> getMapEstados() {
        return mapEstados;
    }


    public void setMapEstados(Map<Integer,Estado> mapEstados) {
        if(mapEstados != null ){
           desaparece = mapEstados.get(Estado.DESAPARECE);
           sinSalir = mapEstados.get(Estado.SIN_SALIR );
           saliendo = mapEstados.get(Estado.SALIENDO);
           afuera =  mapEstados.get(Estado.AFUERA );
           golpeado =  mapEstados.get(Estado.GOLPEADO);
           estrellas =  mapEstados.get(Estado.ESTRELLAS);
        }
        this.mapEstados = mapEstados;
    }

    public Estado getEstadoActual() {
        return estadoActual;
    }
    public void cambiarASiguienteEstado(){
        Log.i(TAG, "****** idSecuencia "+idSecuencia+" indiceArray "+indiceArray);
        switch (idSecuencia){
            case NORMAL: indiceArray = ( (indiceArray+1) >= arrayEstadosNormal.length)?0:indiceArray+1;
                estadoActual = getEstadoById(arrayEstadosNormal[indiceArray]) ;
                if (indiceArray == arrayEstadosNormal.length-1 ) finalizada = true;
                //if (regresarANormal) regresarANormal = false;
            break;
            case GOLPE: indiceArray = ( (indiceArray+1) >= arrayEstadosGolpe.length)?0: indiceArray+1;
                estadoActual = getEstadoById(arrayEstadosGolpe[indiceArray]);
                if (indiceArray == arrayEstadosGolpe.length-1 ) {
                  idSecuencia = NORMAL;
                  indiceArray = 0;
                  finalizada = true;
                }
            break;
        }
        Log.i(TAG, "****** 5 estadoActual "+((estadoActual != null)?estadoActual.idEstado:"")+" idSecuencia "+idSecuencia+" indiceArray "+indiceArray+"  finalizada "+finalizada );
    }
    public SecuenciaEstados copiarSecuencia(SecuenciaEstados secuenciaOriginal ){
        SecuenciaEstados copiaSecuencia = new SecuenciaEstados();
        ///copiaSecuencia.setEstadoActual();
        return copiaSecuencia;
    }
    public SecuenciaEstados copiarSecuenciaEnCero(SecuenciaEstados secuenciaOriginal ){
        SecuenciaEstados copiaSecuencia = new SecuenciaEstados();
        copiaSecuencia.setIdSecuencia(NORMAL);
        copiaSecuencia.setEstadoActual(getEstadoById(arrayEstadosNormal[indiceArray])); ///todoo el resto que en el valor inicial de la clase por defecto
        copiaSecuencia.setMapEstados(secuenciaOriginal.getMapEstados());
        return copiaSecuencia;
    }
    public Estado getEstadoById(int idEstado){
        return getMapEstados().get( new Integer(idEstado));
    }
    public void setEstadoActual(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }

    final public Estado getDesaparece() {
        return desaparece;
    }

    public void setDesaparece(Estado desaparece) {
        this.desaparece = desaparece;
    }

    public Estado getSinSalir() {
        return sinSalir;
    }

    public void setSinSalir(Estado sinSalir) {
        this.sinSalir = sinSalir;
    }

    public Estado getSaliendo() {
        return saliendo;
    }

    public void setSaliendo(Estado saliendo) {
        this.saliendo = saliendo;
    }

    public Estado getAfuera() {
        return afuera;
    }

    public void setAfuera(Estado afuera) {
        this.afuera = afuera;
    }

    public Estado getGolpeado() {
        return golpeado;
    }

    public void setGolpeado(Estado golpeado) {
        this.golpeado = golpeado;
    }

    public Estado getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(Estado estrellas) {
        this.estrellas = estrellas;
    }

    public SecuenciaEstados(int idSecuencia) {
        this.idSecuencia = idSecuencia;
    }

    public SecuenciaEstados() {
    }

    public SecuenciaEstados(Estado estadoActual) {
        this.estadoActual = estadoActual;
    }


    public int getIdSecuencia() {
        return idSecuencia;
    }

    public void setIdSecuencia(int idSecuencia) {
        this.idSecuencia = idSecuencia;
        this.indiceArray = 0;
    }
    public boolean isFinalizada() {
        return finalizada;
    }

    public void setFinalizada(boolean finalizada) {
        this.finalizada = finalizada;
    }

    public boolean isRegresarANormal() {
        return regresarANormal;
    }

    public void setRegresarANormal(boolean regresarANormal) {
        this.regresarANormal = regresarANormal;
    }

    public int getIndiceArray() {
        return indiceArray;
    }

    public void setIndiceArray(int indiceArray) {
        this.indiceArray = indiceArray;
    }
}
