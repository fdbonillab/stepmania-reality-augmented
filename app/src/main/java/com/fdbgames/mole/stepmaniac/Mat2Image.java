package com.fdbgames.mole.stepmaniac;

import android.graphics.Color;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class Mat2Image {
    /*
    Mat mat = new Mat();
    private int argb = 0;
    Color c = new Color(argb, true);
    BufferedImage img;
    byte [] dat;

    Mat2Image(){
    }

    public Mat2Image(Mat mat) {
        getSpace(mat);
    }

    public void getSpace(Mat mat) {
        this.mat = mat;
        int w = mat.cols(), h = mat.rows();
        if (dat == null || dat.length != w * h * 3)
            dat = new byte[w * h * 3];
        if (img == null || img.getWidth() != w || img.getHeight() != h || img.getType() != BufferedImage.TYPE_3BYTE_BGR)
            img = new BufferedImage(w, h, BufferedImage.TYPE_3BYTE_BGR);

        int[] holder = new int[w * h];
        holder =  img.getData().getPixels(0, 0, w, h, (int[]) null);
        for (int i = 0; i < dat.length; i++) {

            int blueChannel = (holder[i] & 0xFF0000) >> 4;
            int greenChannel = (holder[i] & 0xFF00) >> 2;
            int redChannel = (holder[i] & 0xFF);
            int rgb = (blueChannel) & (greenChannel << 2) & (redChannel << 4);

            dat[i] = (byte) rgb;
        }
    }

    BufferedImage getImage(Mat mat) {
        getSpace(mat);
        mat.get(0, 0, dat);
        img.getRaster().setDataElements(0, 0, mat.cols(), mat.rows(), dat);
        return img;
    }
    static {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }*/
}