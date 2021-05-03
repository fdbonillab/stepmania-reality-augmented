package com.fdbgames.mole;


import com.fdbgames.mole.global.constants.Direction;
import com.fdbgames.mole.sa.io.files.Measure;

// represents a single arrow that must be tapped to get points hehe
public class Arrow {
	//public final Rectangle rect;
	//public final Vector3 pos; // game position
	public Direction dir; // note type - possibly NoteData pointer in future ? ? ?
	public int measureID;
	public int beat;
	public int div;
	
	public Arrow(Measure measure, int beat, Direction dir) {
		this.div = measure.notes.size();
		this.beat = beat;
		this.dir = dir;
		//this.pos = new Vector3(dir.laneXPos(), Config.MEASURE_HEIGHT * (float)measureID + (float)beat*4/(float)div, 0f);
		//this.rect = new Rectangle(this.pos.x, this.pos.y, 1f, 1f);
	}
	
	//public static Texture noteTextures;
	//public static TextureRegion NOTE;
	
	public void grade(float timingDiff) {
		// TODO Auto-generated method stub
		
	}
	public String toString() {
		return String.format("A%d|%d.%d", dir, measureID, beat);
	}
	
	public static float calculateBeat(int measure, int div, int beat) {
		return 4 * (measure + beat / (float)div);
	}

	
}
