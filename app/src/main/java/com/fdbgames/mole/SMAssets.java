package com.fdbgames.mole;


import android.util.Log;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.fdbgames.mole.global.Constants;

public class SMAssets extends AssetManager {
	public static SMAssets context;

	public TextureRegion[] arrow;
	public Texture arrowTexture;
	public TextureRegion receptor;
	public Texture receptorTexture;
	public Music music;
	public static final String TAG = "SMAssets";

	public SMAssets() {
		super();
		context = this;
		loadSyncAssets();
		constructRegions();
	}
	public void loadSyncAssets() {
		this.load("receptor.png", Texture.class);
		this.finishLoading();
		Log.d(TAG, "******** se carga receptor por SMAssets");
		/*this.load("silvedream.mp3", Music.class);
		/*this.load("arrow.png", Texture.class);
		this.load("receptor.png", Texture.class);
		this.load("silvedream.mp3", Music.class);
		this.finishLoading();
		arrowTexture = this.get("arrow.png", Texture.class);
		receptorTexture = this.get("receptor.png", Texture.class);
		music = this.get("silvedream.mp3", Music.class);*/
	}

	public void constructRegions() {
		receptor = new TextureRegion(receptorTexture, 0, 0, 128, 128);
		/*arrow = new TextureRegion[8];
		for(int i = 0; i < 8; i++) {
			arrow[i] = new TextureRegion(arrowTexture, 0, (int)(i * Constants.ARROW_SIZE), (int) Constants.ARROW_SIZE, (int)Constants.ARROW_SIZE);
		}*/

	}
}
