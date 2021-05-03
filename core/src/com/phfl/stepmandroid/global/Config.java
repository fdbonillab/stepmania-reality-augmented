package com.phfl.stepmandroid.global;

import com.badlogic.gdx.Input.Keys;

public class Config {
	public static final int MEASURE_HEIGHT = 4;
	public static final int LANE_WIDTH = 1;
	public static final float ZOOM_MIN = 0.1f;
	public static final float ZOOM_MAX = 5f;
	public static final int BEAT_HEIGHT = 1;
	public static final float CAMERA_SPEED = 1f;
	public static final float SPEED = 2;
	public static final float DRAW_DISTANCE = 20f;
	public static final float TIMING_WINDOW = 0.25f;
	public static float MOVE_DELAY = 0.25f; // s / unit
	public static float ZOOM_SPEED = 0.35f;
	
	public static class InputBindings {
		public static final int[] 
				LEFT =	{	Keys.LEFT,
							Keys.D},
				
				DOWN =	{	Keys.DOWN,
							Keys.F},
				
				UP =	{	Keys.UP,
							Keys.J},
				
				RIGHT = {	Keys.RIGHT,
							Keys.K};

		public static final int[][] values = {
				LEFT,
				DOWN,
				UP,
				RIGHT
		};
	}
}
