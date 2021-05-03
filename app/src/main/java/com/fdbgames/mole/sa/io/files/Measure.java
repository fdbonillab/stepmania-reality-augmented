package com.fdbgames.mole.sa.io.files;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Represents a single measure of the song.<br/>
 * The length of each measure is {@link Measure.BEATS}.<br/> 
 * Default BEATS of 4 means 1 beat = 1/4 measure<br/>
 * @author Phil
 *
 */
public class Measure {
	public static final Pattern PATTERN = Pattern.compile("((.*?\\d{4}.*?[\\r\\n]+){4,})[,;].*?[\\r\\n]+");
	public static final int BEATS = 4;

	public final int id; // measure # in song
	public final ArrayList<Note> notes;
	public int noteType;


    public boolean allNotesMostradas = false;

    public static Pattern getPATTERN() {
        return PATTERN;
    }

    public Measure(int id, String measure) {
		this.id = id;
		this.notes = new ArrayList<Note>();
		// match rows from pattern. 
		// n = (number of rows) = noteType = (nth note) 
		Matcher noteRowMatcher = Note.pattern.matcher(measure);
		while(noteRowMatcher.find()) {
			notes.add(new Note(noteRowMatcher.group(1)));
			this.noteType++;
		}
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(Note beat : notes) {
			sb.append(beat.toString());
			sb.append('\n');
		}
		return sb.toString();
	}

	public float getBeatForNote(int i) {
		return Measure.BEATS * (id + (float)i/noteType);
	}
	
	public int beat() {
		return this.id * Measure.BEATS;
	}
	
	public int getOrder(int i) {
		if(notes.size() == 4) {
			return 0;
		}
		if(notes.size() == 8) {
			return i % 2;
		}
		if(notes.size() == 12) {
			return ((i % 3) * 2) % 3;
		}
		if(notes.size() == 16) {
			switch(i % 4) {
			case 0: return 0;
			case 1: return 3;
			case 2: return 1;
			case 3: return 3;
			}
		}
		return 0;
	}

    public boolean isAllNotesMostradas() {
        return allNotesMostradas;
    }

    public void setAllNotesMostradas(boolean allNotesMostradas) {
        this.allNotesMostradas = allNotesMostradas;
    }

	public float getBeatForNote(Note n) {
		return getBeatForNote(notes.indexOf(n));
	}
}
