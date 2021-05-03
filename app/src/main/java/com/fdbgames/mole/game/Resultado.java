package com.fdbgames.mole.game;

public class Resultado {
    Receptor receptor;
    int gradoAcierto;

    public Resultado(Receptor receptor, int gradoAcierto) {
        this.receptor = receptor;
        this.gradoAcierto = gradoAcierto;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }

    public int getGradoAcierto() {
        return gradoAcierto;
    }

    public void setGradoAcierto(int gradoAcierto) {
        this.gradoAcierto = gradoAcierto;
    }
}
