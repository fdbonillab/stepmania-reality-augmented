package com.fdbgames.mole.sa.io.files;

import java.util.ArrayList;
import java.util.regex.Matcher;

public class NoteData {
	public final ArrayList<Measure> measures;
	public String rawData; 
	
	public NoteData() {
		this(null);
	}
	
	public NoteData(String noteData) {
		this.measures = new ArrayList<Measure>();
		this.rawData = noteData;
		if(noteData == null) return;
		
		// match measure strings and create measures
		Matcher measureMatcher = Measure.PATTERN.matcher(noteData);
		while(measureMatcher.find()) {
			measures.add(new Measure(measures.size(), measureMatcher.group(1)));
		}
	}
}
