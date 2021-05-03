package com.fdbgames.mole.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.fdbgames.mole.SMAssets;
import com.fdbgames.mole.SMInputManager;
import com.fdbgames.mole.global.constants.GameCommand;

// bridge from game -> gdx framework is renderer and input handler
public class SMApplication extends ApplicationAdapter {

	int touchType, xLast, yLast;
	SpriteBatch batch;
	SpriteBatch uiBatch;
	ShapeRenderer uiShapes;
	SMAssets assets;
	
	GameEngine game;

	BitmapFont font;

	public SMApplication(int old) {
		// load assets
		assets = new SMAssets();

		// load game
		game = new GameEngine();

	}
	public SMApplication() {


	}
	@Override
	public void create() {
		// load assets
		assets = new SMAssets();
		
		// load rendering tools
		batch = new SpriteBatch();
		uiBatch = new SpriteBatch();
		uiShapes = new ShapeRenderer();
		font = new BitmapFont();
		
		// load game
		game = new GameEngine();
		
		// setup input
		Gdx.input.setInputProcessor(new SMInputManager(game));
	}

	@Override
	public void render() {
		// do game logic
		game.step(Gdx.graphics.getDeltaTime());
		
		// draw game state
		Gdx.gl.glClearColor(0, 0, 0, 1);

	}
	
	public void resize(int width, int height) {
		game.camera.recalculateViewport();
	}
	
	public GameCommand getCommand(int input) {

		return null;
	}

	@Override
	public void dispose() {
		batch.dispose();
		uiBatch.dispose();
		uiShapes.dispose();
	}

    public GameEngine getGame() {
        return game;
    }

    public void setGame(GameEngine game) {
        this.game = game;
    }
}
