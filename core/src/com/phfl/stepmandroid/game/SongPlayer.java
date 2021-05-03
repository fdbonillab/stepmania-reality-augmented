package com.phfl.stepmandroid.game;

import com.badlogic.gdx.audio.Music;
import com.phil.sa.io.files.TimingData;

public class SongPlayer {
	public float time;
	
	private Music music;
	private float lastMusicPosition;
	
	public SongPlayer(Music music, float offset) {
		this.music = music;
		this.music.setVolume(0.25f);
		this.time = offset;
	}
	
	public void start() {
		time = 0;
		lastMusicPosition = 0;
		music.play();
	}
	
	public void step(float dt) {
		if(!music.isPlaying()) return;
		time += dt;
		float musicPosition = music.getPosition();
		if(lastMusicPosition != musicPosition) {
			lastMusicPosition = musicPosition;
			time = (time + musicPosition) / 2;
		}
	}
}
