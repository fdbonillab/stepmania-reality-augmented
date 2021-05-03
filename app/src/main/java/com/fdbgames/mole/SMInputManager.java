package com.fdbgames.mole;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.fdbgames.mole.game.GameEngine;
import com.fdbgames.mole.global.Config;
import com.fdbgames.mole.global.Constants;
import com.fdbgames.mole.global.constants.GameCommand;
import com.fdbgames.mole.util.SMUtils;

// input processor for gdx framework
public class SMInputManager implements InputProcessor {
	private GameEngine game;
	private int touchType, xLast, yLast;

	public SMInputManager(GameEngine game) {
		this.game=game;
	}

	public GameCommand getCommand(int inputKey) {
		if(SMUtils.arrayContains(Config.InputBindings.LEFT, inputKey)) {
			return GameCommand.LEFT;
		}
		if(SMUtils.arrayContains(Config.InputBindings.RIGHT, inputKey)) {
			return GameCommand.RIGHT;
		}
		if(SMUtils.arrayContains(Config.InputBindings.UP, inputKey)) {
			return GameCommand.UP;
		}
		if(SMUtils.arrayContains(Config.InputBindings.DOWN, inputKey)) {
			return GameCommand.DOWN;
		}
		return null;
	}



	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		xLast = screenX;
		yLast = screenY;
		touchType = Constants.TouchType.TAP;
		return true;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		if(touchType == Constants.TouchType.PAN || touchType == Constants.TouchType.TAP) {
			game.camera.pan(screenX, screenY, xLast, yLast);
			xLast = screenX;
			yLast = screenY;
			touchType = Constants.TouchType.PAN;
		}
		return true;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		if(touchType == Constants.TouchType.TAP) {
			touchType = 0;
		}
		return true;
	}

	@Override
	public boolean scrolled(int amount) {
		game.camera.zoom(Gdx.input.getX(), Gdx.input.getY(), amount);
		return true;
	}


	@Override
	public boolean keyDown(int keycode) {
		game.command(getCommand(keycode), true);
		return true;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

}
