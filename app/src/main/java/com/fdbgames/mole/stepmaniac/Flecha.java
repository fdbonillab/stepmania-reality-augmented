package com.fdbgames.mole.stepmaniac;

import com.fdbgames.mole.sa.io.files.Measure;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

public class Flecha {
    int ubicacion;
    Mat imagen;
    Rect roi;
    public static int TOP_LEFT = 0;
    public static int TOP_RIGHT = 1;
    public static int BOTTOM_RIGHT = 2;
    public static int BOTTOM_LEFT = 3;
    Measure measure;

    public Measure getMeasure() {
        return measure;
    }

    public void setMeasure(Measure measure) {
        this.measure = measure;
    }

    public Flecha(Rect roi, Measure measure) {

        this.roi = roi;
        this.measure = measure;
    }

    public Flecha(int ubicacion, Mat imagen) {
        this.ubicacion = ubicacion;
        this.imagen = imagen;
    }
    public Flecha(int ubicacion, Mat imagen, Rect roi) {
        this.ubicacion = ubicacion;
        this.imagen = imagen;
        this.roi = roi;
    }
    public Flecha( Mat imagen, Rect roi) {
        this.imagen = imagen;
        this.roi = roi;
    }

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
    }

    public Mat getImagen() {
        return imagen;
    }

    public void setImagen(Mat imagen) {
        this.imagen = imagen;
    }

    public Rect getRoi() {
        return roi;
    }

    public void setRoi(Rect roi) {
        this.roi = roi;
    }
}

