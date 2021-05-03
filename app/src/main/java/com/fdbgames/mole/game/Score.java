package com.fdbgames.mole.game;

import org.opencv.core.Mat;

public class Score {
    Mat img;
    Mat imgMask;
    int idScore;
    int tiempoVisibleRestante;
    Receptor receptor;
    Resultado resultado;
    public static final int BAD = 0;
    public static final int GOOD = 1;
    public static final int PERFECT = 2;
    public static final int MARVELOUS = 3;
    public static final int BOO = 4;
    public static final int NOGOOD = 5;
    public static final int OK = 6;
    public static final int MISS = 7;
    public static final int GREAT = 8;
    public static float BAD_SUMAR = 50;
    public static float GOOD_SUMAR = 100;
    public static float GREAT_SUMAR = 200;
    public static float PERFECT_SUMAR = 300;
    public static float MARVELOUS_SUMAR = 400;
    /**  case 6: return Score.BOO;
     case 5: return Score.BAD;
     case 4: return Score.GOOD;
     case 3: return Score.GREAT;
     case 2: return Score.PERFECT;
     case 1: return Score.MARVELOUS;
     default: return Score.MISS;
     * **/

    public static String BAD_FILE = "bad.png";
    public static String GOOD_FILE = "good2.png";
    public static String PERFECT_FILE = "perfect.png";
    public static String MARVLEOUS_FILE = "marvelous.png";
    public static String BOO_FILE = "boo.png";
    public static String NOGOOD_FILE = "nogood.png";
    public static String OK_FILE = "ok.png";
    public static String MISS_FILE = "miss.png";
    public static String GREAT_FILE = "great.png";


    public Score(Mat img, Mat imgMask, int idScore) {
        this.img = img;
        this.imgMask = imgMask;
        this.idScore = idScore;
    }

    public Score(int idScore, int tiempoVisibleRestante) {
        this.idScore = idScore;
        this.tiempoVisibleRestante = tiempoVisibleRestante;
    }
    public Score(Resultado resultado, int tiempoVisibleRestante) {
        this.idScore = resultado.getGradoAcierto();
        this.tiempoVisibleRestante = tiempoVisibleRestante;
        this.resultado = resultado;
        this.receptor = resultado.getReceptor();
    }

    public Mat getImg() {
        return img;
    }

    public void setImg(Mat img) {
        this.img = img;
    }

    public Mat getImgMask() {
        return imgMask;
    }

    public void setImgMask(Mat imgMask) {
        this.imgMask = imgMask;
    }

    public int getIdScore() {
        return idScore;
    }

    public void setIdScore(int idScore) {
        this.idScore = idScore;
    }

    public int getTiempoVisibleRestante() {
        return tiempoVisibleRestante;
    }

    public void setTiempoVisibleRestante(int tiempoVisibleRestante) {
        this.tiempoVisibleRestante = tiempoVisibleRestante;
    }

    public Receptor getReceptor() {
        return receptor;
    }

    public void setReceptor(Receptor receptor) {
        this.receptor = receptor;
    }
    public Resultado getResultado() {
        return resultado;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }
}
