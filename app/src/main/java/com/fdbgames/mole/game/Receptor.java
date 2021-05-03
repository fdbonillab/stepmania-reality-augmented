package com.fdbgames.mole.game;


import com.fdbgames.mole.global.constants.Direction;

import org.opencv.core.MatOfPoint;
import org.opencv.core.Scalar;

import java.util.List;

public class Receptor {
	public static final Receptor
		/*LEFT 	= new Receptor(Direction.LEFT,	new Vector2(-1.5f, 0f)),
		UP 		= new Receptor(Direction.UP,	new Vector2(-0.5f, 0f)),
		DOWN 	= new Receptor(Direction.DOWN,	new Vector2( 0.5f, 0f)),
		RIGHT	= new Receptor(Direction.RIGHT,	new Vector2( 1.5f, 0f));*/
	LEFT 	= new Receptor(Direction.LEFT),
	UP 		= new Receptor(Direction.UP),
	DOWN 	= new Receptor(Direction.DOWN),
	RIGHT	= new Receptor(Direction.RIGHT);
	//public static TextureRegion texture
	
	//public final Vector2 pos;
	public Direction dir= null;

	List<MatOfPoint> listaPoligono;
	Integer ubicacion;
	boolean acertado;
	boolean tocado;
	Scalar color;
	int gradoAcierto;


	public Receptor(List<MatOfPoint> listaPoligono, Integer ubicacion, Scalar color) {
		this.listaPoligono = listaPoligono;
		this.ubicacion = ubicacion;
		this.color = color;
	}

	public boolean isTocado() {
		return tocado;
	}

	public void setTocado(boolean tocado) {
		this.tocado = tocado;
	}

	public Scalar getColor() {
		return color;
	}

	public void setColor(Scalar color) {
		this.color = color;
	}

	public List<MatOfPoint> getListaPoligono() {
		return listaPoligono;
	}

	public void setListaPoligono(List<MatOfPoint> listaPoligono) {
		this.listaPoligono = listaPoligono;
	}

	public Integer getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(Integer ubicacion) {
		this.ubicacion = ubicacion;
	}

	public boolean isAcertado() {
		return acertado;
	}

	public void setAcertado(boolean acertado) {
		this.acertado = acertado;
	}


	/*private Receptor(Direction dir, Vector2 pos) {
		this.dir = dir;
		this.pos = pos;
	}*/
	private Receptor(Direction dir ) {
		this.dir = dir;
	}

	public Receptor(List<MatOfPoint> listaPoligono, Integer ubicacion, boolean acertado, Scalar color) {
		this.listaPoligono = listaPoligono;
		this.ubicacion = ubicacion;
		this.acertado = acertado;
		this.color = color;
	}

	public Receptor(List<MatOfPoint> listaPoligono, Integer ubicacion, boolean acertado) {
		this.listaPoligono = listaPoligono;
		this.ubicacion = ubicacion;
		this.acertado = acertado;
	}
	public int getGradoAcierto() {
		return gradoAcierto;
	}

	public void setGradoAcierto(int gradoAcierto) {
		this.gradoAcierto = gradoAcierto;
	}

}
