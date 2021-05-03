package com.phfl.stepmandroid.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.phfl.stepmandroid.global.Config;
import com.phfl.stepmandroid.global.Constants;

public class ControllableSquareCamera extends OrthographicCamera {
	private float size;

	public ControllableSquareCamera(float arrowSize) {
		this.size = arrowSize;
		this.recalculateViewport();
		this.position.set(0f, 0f, 0f);
	}
	
	public void recalculateViewport() {
		setToOrtho(false, 
			Gdx.graphics.getWidth() / size,
			Gdx.graphics.getHeight() / size);
		this.position.set(0f, (this.viewportHeight/2f)*0.75f, 0f);
		this.update();
	}
	
	public void pan(int x, int y, int xLast, int yLast) {
		this.position.sub(unproject(new Vector3(x, y, 0f)).sub(unproject(new Vector3(xLast, yLast, 0f))));
	}
	
	public void zoom(int x, int y, int amount) {
		Vector3 screenPoint = new Vector3(x, y, 0f);
		Vector3 focusPoint = unproject(screenPoint.cpy());
		this.zoom(amount);
		this.update();
		this.position.sub(unproject(screenPoint).sub(focusPoint));
	}
	
	public void zoom(int amount) {
		this.zoom += Config.ZOOM_SPEED * amount;
		this.zoom = MathUtils.clamp(zoom, Config.ZOOM_MIN, Config.ZOOM_MAX);
	}

	public void resize(int width, int height) {
		// TODO Auto-generated method stub
		
	}

}
