package com.phfl.stepmandroid;

import java.util.Arrays;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector2;
import com.phfl.stepmandroid.game.GameEngine;
import com.phfl.stepmandroid.global.Config;
import com.phfl.stepmandroid.global.Config.InputBindings;
import com.phfl.stepmandroid.global.constants.Direction;
import com.phfl.stepmandroid.global.constants.GameCommand;
import com.phil.sa.io.files.Note;
import com.phil.sa.io.files.Measure;
import com.phil.sa.io.files.SMFile;

// bridge from game -> gdx framework is renderer and input handler
public class SMApplication extends ApplicationAdapter {

	int touchType, xLast, yLast;
	SpriteBatch batch;
	SpriteBatch uiBatch;
	ShapeRenderer uiShapes;
	SMAssets assets;
	
	GameEngine game;

	BitmapFont font;

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
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		batch.setProjectionMatrix(game.camera.combined);
		uiShapes.setProjectionMatrix(game.camera.combined);
		batch.begin();
		uiShapes.begin(ShapeType.Line);

		char note;
		Vector2 arrowPos = new Vector2(0f, 0f);/// esto es de libgdx
		for (Measure measure : game.chart.noteData.measures) {
			for(int i = 0; i < measure.notes.size(); i++) {
				arrowPos.y = measure.getBeatForNote(i);
				arrowPos.y -= game.smFile.timingData.getBeat(game.songPlayer.time + game.smFile.offset);
				arrowPos.y *= Config.SPEED;
				
				for(Direction dir : Direction.values()) {
					arrowPos.x = dir.laneXPos();
					note = measure.notes.get(i).data[dir.ordinal()];
					if(note == '1' || note == '2') {
						batch.draw(assets.arrow[measure.getOrder(i)], arrowPos.x - 0.5f, arrowPos.y - 0.5f, 0.5f, 0.5f, 1, 1, 1, 1, dir.rotation());
						uiShapes.rectLine(arrowPos.cpy().sub(0.5f, 0f), arrowPos.cpy().add(0.5f, 0f), 0f);///pinta una raya
					}
				}
			}
			if(arrowPos.y > Config.DRAW_DISTANCE) break;
		}
		for(Direction dir : Direction.values()) {
			batch.draw(
					assets.receptor, 
					dir.laneXPos() - 0.5f,  -0.5f, 
					0.5f, 0.5f, 
					1, 1, 
					1, 1, 
					dir.rotation());
			
			uiShapes.rectLine(
					dir.laneXPos() - 0.5f, 0, 
					dir.laneXPos() + 0.5f, 0, 
					0f);
		}
		batch.end();
		uiShapes.end();
	}
	
	public void resize(int width, int height) {
		game.camera.recalculateViewport();
	}
	
	public GameCommand getCommand(int input) {
		if(Arrays.asList(InputBindings.LEFT).contains(input)) {
			return GameCommand.LEFT;
		}
		if(Arrays.asList(InputBindings.RIGHT).contains(input)) {
			return GameCommand.RIGHT;
		}
		if(Arrays.asList(InputBindings.UP).contains(input)) {
			return GameCommand.UP;
		}
		if(Arrays.asList(InputBindings.DOWN).contains(input)) {
			return GameCommand.DOWN;
		}
		return null;
	}

	@Override
	public void dispose() {
		batch.dispose();
		uiBatch.dispose();
		uiShapes.dispose();
	}
}
