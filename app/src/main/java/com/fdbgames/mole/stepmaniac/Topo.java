package com.fdbgames.mole.stepmaniac;

import org.opencv.core.Mat;

import java.util.List;

public class Topo {
    int estado;
    List<Mat> lstSecuencia;
    List<Mat> lstSecuenciaMask;
    List<Mat> lstSecuenciaGolpe;
    SecuenciaEstados secuenciaAnimacion;


    int estadoPrevio;
    boolean yaSalio;
    Mat imgPintar;


    Mat imgMaskPintar;
    int row;
    int col;
    Mat frameAnterior;
    boolean yaTocado;

    Estado elEstado;
    int duracionFrame;
    int idTopo;

    public int getIdTopo() {
        return idTopo;
    }

    public void setIdTopo(int idTopo) {
        this.idTopo = idTopo;
    }

    public int getDuracionFrame() {
        return duracionFrame;
    }

    public void setDuracionFrame(int duracionFrame) {
        this.duracionFrame = duracionFrame;
    }

    public Topo() {
    }

    public Topo(int estado){
        this.estado = estado;
    }
    public Topo( int estado, List lstSecuencia){
        this.estado = estado;
        this.lstSecuencia = lstSecuencia;
    }

    public Estado getElEstado() {
        return elEstado;
    }

    public void setElEstado(Estado elEstado) {
        this.elEstado = elEstado;
    }

    public Topo(int estado, List lstSecuencia, int estadoPrevio){
        this.estado = estado;

        this.lstSecuencia = lstSecuencia;
        this.estadoPrevio = estadoPrevio;
    }

    public List<Mat> getLstSecuenciaMask() {
        return lstSecuenciaMask;
    }

    public void setLstSecuenciaMask(List<Mat> lstSecuenciaMask) {
        this.lstSecuenciaMask = lstSecuenciaMask;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }


    public int getEstadoPrevio() {
        return estadoPrevio;
    }

    public void setEstadoPrevio(int estadoPrevio) {
        this.estadoPrevio = estadoPrevio;
    }
    public boolean isYaSalio() {
        return yaSalio;
    }

    public void setYaSalio(boolean yaSalio) {
        this.yaSalio = yaSalio;
    }
    public Topo copiar(Topo unTopo){
        Topo copiaTopo = new Topo();
        copiaTopo.setSecuenciaAnimacion(unTopo.getSecuenciaAnimacion().copiarSecuenciaEnCero(unTopo.getSecuenciaAnimacion()));
        copiaTopo.setCol(unTopo.getCol());

        //copiaTopo.setM
                /*
                     secuenciaEstadosNormal = new SecuenciaEstados(SecuenciaEstados.NORMAL);
                    secuenciaEstadosNormal.setMapEstados(elMapEstados);
                    secuenciaEstadosNormal.setEstadoActual(secuenciaEstadosNormal.getDesaparece());
                    secuenciaEstadosGolpe = new SecuenciaEstados(SecuenciaEstados.GOLPE);
                    secuenciaEstadosGolpe.setMapEstados(elMapEstados);
                    secuenciaEstadosGolpe.setEstadoActual(secuenciaEstadosGolpe.getDesaparece());

                    lstSecuencia.add(img.clone());
                    unTopo = new Topo( Estado.DESAPARECE,lstSecuencia);
                    unTopo.setElEstado(secuenciaEstadosNormal.getDesaparece());
                    unTopo.setSecuenciaAnimacion(secuenciaEstadosNormal);
                    int row = 0; int col = rand.nextInt(screen.cols());
                    unTopo.setCol(col);
                    unTopo.setRow(row);
                    unTopo.setLstSecuenciaMask(ls
                 */
                return  copiaTopo;
    }
    public List<Mat> getLstSecuencia() {
        return lstSecuencia;
    }

    public void setLstSecuencia(List<Mat> lstSecuencia) {
        this.lstSecuencia = lstSecuencia;
    }

    public Mat getImgPintar() {
        return imgPintar;
    }

    public void setImgPintar(Mat imgPintar) {
        this.imgPintar = imgPintar;
    }

    public Mat getImgMaskPintar() {
        return imgMaskPintar;
    }

    public void setImgMaskPintar(Mat imgMaskPintar) {
        this.imgMaskPintar = imgMaskPintar;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCol() {
        return col;
    }

    public void setCol(int col) {
        this.col = col;
    }
    public Mat getFrameAnterior() {
        return frameAnterior;
    }

    public void setFrameAnterior(Mat frameAnterior) {
        this.frameAnterior = frameAnterior;
    }
    public boolean isYaTocado() {
        return yaTocado;
    }

    public void setYaTocado(boolean yaTocado) {
        this.yaTocado = yaTocado;
    }

    public SecuenciaEstados getSecuenciaAnimacion() {
        return secuenciaAnimacion;
    }

    public void setSecuenciaAnimacion(SecuenciaEstados secuenciaAnimacion) {
        this.secuenciaAnimacion = secuenciaAnimacion;
        this.secuenciaAnimacion.setIndiceArray(0);
    }
}
