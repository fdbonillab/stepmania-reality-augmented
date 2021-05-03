package com.example.david.intentoopencv;

import org.opencv.core.Mat;

public class Estado {
    int idEstado;
    Mat matImagen;
    Mat matImagenMask;
    static final int DESAPARECE = -1;
    static final int INVISIBLE = 0;
    static final int SIN_SALIR = 1;
    static final int SALIENDO = 2;
    static final int AFUERA = 3;
    static final int INICIO = 4;
    static final int GOLPEADO = 5;
    static final int ESTRELLAS = 6;

    public Estado(int idEstado,Mat matImagen){
        this.idEstado = idEstado;
        this.matImagen = matImagen;
    }
    public Estado(int idEstado, Mat matImagen, Mat matImagenMask) {
        this.idEstado = idEstado;
        this.matImagen = matImagen;
        this.matImagenMask = matImagenMask;
    }

    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    public Mat getMatImagen() {
        return matImagen;
    }

    public void setMatImagen(Mat matImagen) {
        this.matImagen = matImagen;
    }

    public Mat getMatImagenMask() {
        return matImagenMask;
    }

    public void setMatImagenMask(Mat matImagenMask) {
        this.matImagenMask = matImagenMask;
    }
}
