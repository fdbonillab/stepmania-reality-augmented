package com.fdbgames.mole.game;
import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.fdbgames.mole.SMAssets;
import com.fdbgames.mole.global.Config;
import com.fdbgames.mole.global.Constants;
import com.fdbgames.mole.global.constants.Direction;
import com.fdbgames.mole.global.constants.GameCommand;
import com.fdbgames.mole.stepmaniac.PackSong;
import com.fdbgames.mole.stepmaniac.R;
import com.fdbgames.mole.sa.io.files.Chart;
import com.fdbgames.mole.sa.io.files.Measure;
import com.fdbgames.mole.sa.io.files.Note;
import com.fdbgames.mole.sa.io.files.SMFile;
import com.fdbgames.mole.util.ControllableSquareCamera;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.FileHandler;

// ONLY GAME LOGIC
public class GameEngine {
	public ControllableSquareCamera camera;
	public SMFile smFile;
	public Chart chart;
	public SongPlayer songPlayer;
	Context context;
	PackSong packSong;
	int idJuego;

	private int[] input;



	List<Float> lstPuntajes;

	static String TAG = "GameEngine";

	public GameEngine() {
		this.input = new int[4];
		//this.camera = new ControllableSquareCamera(Constants.ARROW_SIZE);
		if( Gdx.files == null){
			Log.i(TAG, "****** Gdx.files.internal es null ");
		}
        FileHandle fh =  Gdx.files.internal("SILVER DREAM.sm");
		if( fh == null){
            Log.i(TAG, "****** filehandle es null ");
        } else {
            Log.i(TAG, "****** filehandle no es null ");
        }
		//play(new SMFile(fh), "Hard");
		String fileName = "";
		play(new SMFile(null ), "Hard");
	}
	public GameEngine(Context context) {
		this.input = new int[4];
		this.context = context;
		//this.camera = new ControllableSquareCamera(Constants.ARROW_SIZE);
		if( Gdx.files == null){
			Log.i(TAG, "****** Gdx.files.internal es null ");
		}
		FileHandle fh =  Gdx.files.internal("Pacman.sm");
		if( fh == null){
			Log.i(TAG, "****** filehandle es null ");
		} else {
			Log.i(TAG, "****** filehandle no es null ");
		}
		play(new SMFile(fh), "Easy");
		Log.i(TAG, "****** otro constructor de GameEngine ");
		//play(new SMFile(fileName, context ), "Hard");
	}
	public GameEngine(Context context, int idJuego) {
		this.input = new int[4];
		this.context = context;
		this.idJuego = idJuego;
		//this.camera = new ControllableSquareCamera(Constants.ARROW_SIZE);
		if( Gdx.files == null){
			Log.i(TAG, "****** Gdx.files.internal es null ");
		}
		FileHandle fh =  Gdx.files.internal("Pacman.sm");
		if( fh == null){
			Log.i(TAG, "****** filehandle es null ");
		} else {
			Log.i(TAG, "****** filehandle no es null ");
		}
		play(new SMFile(fh), "Easy");
		Log.i(TAG, "****** otro constructor de GameEngine ");
		//play(new SMFile(fileName, context ), "Hard");
	}
	public GameEngine(Context context, PackSong packSong) {
		this.input = new int[4];
		this.context = context;
		this.idJuego = idJuego;
		String storageEmulated = "emulated";
		//this.camera = new ControllableSquareCamera(Constants.ARROW_SIZE);
		if( Gdx.files == null){
			Log.i(TAG, "****** Gdx.files.internal es null ");
		}
		FileHandle fh = null;
		if( packSong.getFileStepmania().contains(storageEmulated)){
			fh =  Gdx.files.internal(packSong.getFileStepmania());
			Log.d(TAG, "****** buscando file por internal ");
		} else {
			fh =  Gdx.files.external(packSong.getFileStepmania());
			Log.d(TAG, "****** buscando file por external ");
		}
		if( fh == null){
			Log.i(TAG, "****** filehandle es null ");
		} else {
			Log.i(TAG, "****** filehandle no es null ");
		}
		this.smFile = new SMFile(fh) ;
		this.packSong = packSong;
		//play( "Easy",packSong);
		Log.i(TAG, "****** otro constructor de GameEngine ");
	}

	public void playOld(SMFile file, String difficulty) {
		this.smFile = file;
		this.chart = file.charts.get(difficulty);
		Log.i(TAG, "****** file.chats size "+file.charts.size());
		this.songPlayer = new SongPlayer((Music) SMAssets.context.get(file.musicFile), file.offset);
		songPlayer.start();
	}
	public void play(SMFile file, String difficulty) {
		this.smFile = file;
		Log.i(TAG, "****** trayendo cancion por defecto pacman... ");
		this.chart = file.charts.get(difficulty);
		if( chart == null ){
			difficulty = difficulty.toLowerCase();
			this.chart = smFile.charts.get(difficulty);
			if( chart == null ){/// si no hay un nivel facil se toma el primero que haya mientra se hace una gui para navegar los niveles
				/// aunque no creo porque no van a poder logra tanta velocidad como para usar las mismas dificultades del tapete o del teclado
				Log.i(TAG, "****** mod no se encuentra la dificultad "+difficulty+" en el archivo descargado toco tomar el primero que exista ");
				chart = smFile.charts.entrySet().iterator().next().getValue();
			}
		}
		Log.i(TAG, "****** file.chats size "+smFile.charts.size());
		Log.i(TAG, "****** file.offset "+smFile.offset);
		Log.i(TAG, "****** file.tittle "+smFile.title);
		Log.i(TAG, "****** file.musicFile "+smFile.musicFile);
		Log.i(TAG, "****** file.timingData "+smFile.timingData);
		Log.i(TAG, "****** file.timingData "+smFile.header);
		FileHandle fhMusic =  Gdx.files.internal("Pacman.mp3");
		this.songPlayer = new SongPlayer( Gdx.audio.newMusic(fhMusic), file.offset);
        //this.songPlayer = new SongPlayer( file.offset);
		songPlayer.start();
		Log.i(TAG, "****** time song "+songPlayer.time);
		//MediaPlayer mp = MediaPlayer.create(context, R.raw.silvedream);
		//mp.start();
	}
	public void play(String difficulty) {
		this.chart = smFile.charts.get(difficulty);
		Log.i(TAG, "****** file.chats size "+smFile.charts.size());
		Log.i(TAG, "****** file.offset "+smFile.offset);
		FileHandle fhMusic =  Gdx.files.internal("SILVER DREAM.mp3");
		this.songPlayer = new SongPlayer( Gdx.audio.newMusic(fhMusic), smFile.offset);
		//this.songPlayer = new SongPlayer( file.offset);
		songPlayer.start();
		//MediaPlayer mp = MediaPlayer.create(context, R.raw.silvedream);
		//mp.start();
	}
	public void play(){
		play( "Easy",packSong);
	}
	public void play(String difficulty, PackSong packSong) {
		String separador = "/";
		this.chart = smFile.charts.get(difficulty);
		UtilFile.mostrarCharts(smFile.charts);
		if( chart == null ){
			difficulty = difficulty.toLowerCase();
			this.chart = smFile.charts.get(difficulty);
			if( chart == null ){/// si no hay un nivel facil se toma el primero que haya mientra se hace una gui para navegar los niveles
				/// aunque no creo porque no van a poder logra tanta velocidad como usar las mismas dificultades del tapete o del teclado
				Log.i(TAG, "****** mod no se encuentra la dificultad "+difficulty+" en el archivo descargado toco tomar el primero que exista ");
				chart = smFile.charts.entrySet().iterator().next().getValue();
			}
		}
		Log.i(TAG, "****** file.chats size "+smFile.charts.size());
		Log.i(TAG, "****** file.offset "+smFile.offset);
		Log.i(TAG, "****** file.tittle "+smFile.title);
		Log.i(TAG, "****** file.musicFile "+smFile.musicFile);
		Log.i(TAG, "****** file.timingData "+smFile.timingData);
		Log.i(TAG, "****** path file antes de extraer "+packSong.getFileStepmania());
		/**String fullPath = "C:\\Hello\\AnotherFolder\\The File Name.PDF";
		 int index = fullPath.lastIndexOf("\\");
		 String fileName = fullPath.substring(index + 1);
		 * **/
		int index = packSong.getFileStepmania().lastIndexOf(separador);
		//String pathFile = packSong.getFileStepmania().replace(file.title+".sm","");
		String pathFile = packSong.getFileStepmania().substring(0, index+1 );
		Log.i(TAG, "****** path file extraido "+pathFile);
		FileHandle fhMusic =  Gdx.files.external(pathFile+smFile.musicFile);
		Log.i(TAG, "****** 2 path file song "+pathFile+smFile.musicFile);
		//this.songPlayer = new SongPlayer((Music) SMAssets.context.get(file.musicFile), file.offset);
		//this.songPlayer = new SongPlayer((Music) SMAssets.context.get(file.musicFile), file.offset);
		this.songPlayer = new SongPlayer( Gdx.audio.newMusic(fhMusic), smFile.offset);
		//this.songPlayer = new SongPlayer( file.offset);
		songPlayer.start();
		//MediaPlayer mp = MediaPlayer.create(context, R.raw.silvedream);
		//mp.start();
	}
	// events
	public void inputDown(Direction dir) {
		input[dir.ordinal()] = 1;
	}

	public void inputUp(Direction aDir) {
//		input[aDir.ordinal()] = 2;
	}
	public void loadSyncAssets() {
		TextureAtlas atlas;
		final AssetManager assetManager =  new AssetManager();
		new Thread(new Runnable() {
			@Override
			public void run() {
				// do something important here, asynchronously to the rendering thread
				// post a Runnable to the rendering thread that processes the result
				Gdx.app.postRunnable(new Runnable() {
					@Override
					public void run() {
						Log.d(TAG, "********  se carga receptor assetmanager con postrunnable post");
						// create icons here, com.badlogic.gdx.graphics.GL20 won't be null here
						//assetManager = new AssetManager();
						assetManager.load("receptor.png", Texture.class);
						assetManager.finishLoading();
						Log.d(TAG, "******** se carga receptor por GameEngine");
						//atlas = manager.get(urlFileAtlas,TextureAtlas.class);
					}
				});
			}
		}).start();
		/*this.load("silvedream.mp3", Music.class);
		/*this.load("arrow.png", Texture.class);
		this.load("receptor.png", Texture.class);
		this.load("silvedream.mp3", Music.class);
		this.finishLoading();
		arrowTexture = this.get("arrow.png", Texture.class);
		receptorTexture = this.get("receptor.png", Texture.class);
		music = this.get("silvedream.mp3", Music.class);*/
	}


	// update logic
	public void step(float dt) {
		// update song player
		songPlayer.step(dt);
		//Log.i(TAG, "****** gameEngine step ");
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
		Log.i(TAG, "****** judgeTap ");
		float sBeat = smFile.timingData.getBeat(songPlayer.time - Config.TIMING_WINDOW);
		float eBeat = smFile.timingData.getBeat(songPlayer.time + Config.TIMING_WINDOW);
		for(Measure m : this.chart.noteData.measures) {
			if(sBeat <= m.beat() + 4 && m.beat() <= eBeat ) {
				for(Note n : m.notes) {
					if(n.data[dir] != '0') {
						n.data[dir] = '0';
						input[dir] = 0;
						//System.out.println("hit:" + m.getBeatForNote(n));
						Float puntaje = m.getBeatForNote(n);
						Log.i(TAG, "****** hit "+puntaje);
						if ( lstPuntajes == null ){
							lstPuntajes = new ArrayList<Float>();
						}
						lstPuntajes.add(puntaje);
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

	public int getIdJuego() {
		return idJuego;
	}

	public void setIdJuego(int idJuego) {
		this.idJuego = idJuego;
	}

	public List<Float> getLstPuntajes() {
		return lstPuntajes;
	}
	public void agregarPuntajeGradoAcierto(Float plusGradoAcierto){
		if ( lstPuntajes == null ){
			lstPuntajes = new ArrayList<Float>();
		}
        Log.i(TAG, "******** sumando plus grado acierto "+plusGradoAcierto);
	    lstPuntajes.add(plusGradoAcierto);
    }
	public void disposeCosas(){
	    //this.smFile.
    }
    public boolean measuresCompletados(){
		/*if(this.chart.noteData.measures.){

		}*/
		return false;
	}
}
