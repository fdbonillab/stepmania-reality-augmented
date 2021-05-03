package com.phfl.stepmandroid.global.constants;

public enum Direction {
	LEFT,
	DOWN,
	UP,
	RIGHT;
	
	public float rotation() {
		switch(this) {
		case LEFT: 	return 270f;
		case DOWN: 	return 0f;
		case UP: 	return 180f;
		case RIGHT:	return 90f;
		default: 	return -1f;
		}
	}
	
	public float laneXPos() {
		switch(this) {
		case LEFT: 	return -1.5f;
		case DOWN: 	return -0.5f;
		case UP: 	return  0.5f;
		case RIGHT:	return  1.5f;
		default: 	return 0f;
		}
	}
}
