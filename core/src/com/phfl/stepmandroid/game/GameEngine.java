package com.phfl.stepmandroid.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.phfl.stepmandroid.SMAssets;
import com.phfl.stepmandroid.global.Config;
import com.phfl.stepmandroid.global.Constants;
import com.phfl.stepmandroid.global.constants.Direction;
import com.phfl.stepmandroid.global.constants.GameCommand;
import com.phfl.stepmandroid.util.ControllableSquareCamera;
import com.phil.sa.io.files.Chart;
import com.phil.sa.io.files.Measure;
import com.phil.sa.io.files.Note;
import com.phil.sa.io.files.SMFile;

// ONLY GAME LOGIC
public class GameEngine {
	public ControllableSquareCamera camera;
	public SMFile smFile;
	public Chart chart;
	public SongPlayer songPlayer;

	private int[] input;

	public GameEngine() {
		this.input = new int[4];
		this.camera = new ControllableSquareCamera(Constants.ARROW_SIZE);
		play(new SMFile(Gdx.files.internal("SILVER DREAM.sm")), "Hard");
	}

	public void play(SMFile file, String difficulty) {
		this.smFile = file;
		this.chart = file.charts.get(difficulty);
		this.songPlayer = new SongPlayer((Music) SMAssets.context.get(file.musicFile), file.offset);
		songPlayer.start();
	}

	// events
	public void inputDown(Direction dir) {
		input[dir.ordinal()] = 1;
	}

	public void inputUp(Direction aDir) {
//		input[aDir.ordinal()] = 2;
	}

	// update logic
	public void step(float dt) {
		// update song player
		songPlayer.step(dt);

		// judge inputs
		for (int dir = 0; dir < 4; dir++) {
			if (input[dir] == 1) {
    			judgeTap(dir);
				input[dir] = 0;
			}
		}
	}

	// judges the input whether it "hit" an arrow and how accurate the hit was
	private void judgeTap(int dir) {
		// 1. find closest beat to current time
		float sBeat = smFile.timingData.getBeat(songPlayer.time - Config.TIMING_WINDOW);
		float eBeat = smFile.timingData.getBeat(songPlayer.time + Config.TIMING_WINDOW);
		for(Measure m : this.chart.noteData.measures) {
			if(sBeat <= m.beat() + 4 && m.beat() <= eBeat ) {
				for(Note n : m.notes) {
					if(n.data[dir] != '0') {
						n.data[dir] = '0';
						input[dir] = 0;
						System.out.println("hit:" + m.getBeatForNote(n));
						return;
					}
				}
			}
		}
		// 2. compare inputs to beat expectation
	}

	public void command(GameCommand command, boolean activate) {
		if(command == GameCommand.LEFT) {
			if(activate) {
				inputDown(Direction.LEFT);
			} else {
				inputUp(Direction.LEFT);
			}
		}
		if(command == GameCommand.DOWN) {
			if(activate) {
				inputDown(Direction.DOWN);
			} else {
				inputUp(Direction.DOWN);
			}
		}
		if(command == GameCommand.UP) {
			if(activate) {
				inputDown(Direction.UP);
			} else {
				inputUp(Direction.UP);
			}
		}
		if(command == GameCommand.RIGHT) {
			if(activate) {
				inputDown(Direction.RIGHT);
			} else {
				inputUp(Direction.RIGHT);
			}
		}
		

	}
}
