package com.fdbgames.mole.intentoopencv;

public class Nivel {
    int velocidad;
    int cantidadTopps;
    int toposSimultaneos;
    int idNivel = 1;

    public int getIdNivel() {
        return idNivel;
    }

    public void setIdNivel(int idNivel) {
        this.idNivel = idNivel;
    }

    public int getToposSimultaneos() {
        return toposSimultaneos;
    }

    public void setToposSimultaneos(int toposSimultaneos) {
        this.toposSimultaneos = toposSimultaneos;
    }

    public Nivel(int velocidad, int cantidadTopps, int toposSimultaneos) {

        this.velocidad = velocidad;
        this.cantidadTopps = cantidadTopps;
        this.toposSimultaneos = toposSimultaneos;
    }

    public Nivel(int velocidad, int cantidadTopps) {
        this.velocidad = velocidad;
        this.cantidadTopps = cantidadTopps;
    }

    public int getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(int velocidad) {
        this.velocidad = velocidad;
    }

    public int getCantidadTopps() {
        return cantidadTopps;
    }

    public void setCantidadTopps(int cantidadTopps) {
        this.cantidadTopps = cantidadTopps;
    }
}
