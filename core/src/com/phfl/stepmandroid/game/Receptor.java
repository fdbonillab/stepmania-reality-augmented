package com.phfl.stepmandroid.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.phfl.stepmandroid.global.constants.Direction;

public class Receptor {
	public static final Receptor
		LEFT 	= new Receptor(Direction.LEFT,	new Vector2(-1.5f, 0f)),
		UP 		= new Receptor(Direction.UP,	new Vector2(-0.5f, 0f)),
		DOWN 	= new Receptor(Direction.DOWN,	new Vector2( 0.5f, 0f)),
		RIGHT	= new Receptor(Direction.RIGHT,	new Vector2( 1.5f, 0f));
	public static TextureRegion texture;
	
	public final Vector2 pos;
	public final Direction dir;
	private Receptor(Direction dir, Vector2 pos) {
		this.dir = dir;
		this.pos = pos;
	}
}
