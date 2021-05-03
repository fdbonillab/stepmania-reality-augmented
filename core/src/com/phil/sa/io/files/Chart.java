package com.phil.sa.io.files;

import java.util.Scanner;

public class Chart {
	public String type, 
				description, 
				difficulty,
				steps,
				radarValues;
	public NoteData noteData;
	
	
	
	public Chart(String chartString) {
		Scanner in = new Scanner(chartString);
		in.useDelimiter("[:;]");
		type = in.next().trim();
		description = in.next().trim();
		difficulty = in.next().trim();
		steps = in.next().trim();
		radarValues = in.next().trim();
		noteData = new NoteData(in.next() + ";");
		in.close();
	}



	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Chart");
		sb.append('|');
		sb.append(type);
		sb.append('|');
		sb.append(description);
		sb.append('|');
		sb.append(difficulty);
		sb.append('|');
		sb.append(steps);
		sb.append('|');
		sb.append(radarValues);
		sb.append('|');
		sb.append("Measures: " + noteData.measures.size());
		sb.append('|');
		sb.append("NDLen: " + noteData.toString().length());
		return sb.toString();
	}
	
}
