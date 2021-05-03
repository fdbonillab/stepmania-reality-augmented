package com.fdbgames.mole.game;

import com.badlogic.gdx.audio.Music;

public class SongPlayer {
	public float time;
	
	private Music music;
	private float lastMusicPosition;
	
	public SongPlayer(Music music, float offset) {
		this.music = music;
		this.music.setVolume(0.25f);
		this.time = offset;
	}
	public SongPlayer(float offset) {
		//this.music.setVolume(0.25f);
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
	public boolean isMusicPlaying(){
		return  music.isPlaying();
	}
}
