package com.phil.sa.io.files;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import com.badlogic.gdx.files.FileHandle;

public class SMFile {
	
	
	public Map<String, String> header;
	public Map<String, Chart> charts;
	public TimingData timingData;
	
	public String title, subtitle, artist, musicFile;
	public float offset;
	public SortedSet<Integer> keyBeats;
	private Map<Integer, Float> bpmChanges;
	private Map<Integer, Float> stops;
	
	public SMFile(FileHandle fileHandle) {
		this.header = new HashMap<String, String>();
		this.charts = new HashMap<String, Chart>();
		
		// input stream
		SMFileReader input = new SMFileReader(fileHandle);
		
		// tags
		String key;
		while((key = input.readKey()) != null) {
			if(key.equals("TITLE")) {
				this.title = input.readValue();
			}
			if(key.equals("SUBTITLE")) {
				this.subtitle = input.readValue();
			}
			if(key.equals("MUSIC")) {
				this.musicFile = input.readValue();
			}
			if(key.equals("OFFSET")) {
				this.offset = Float.parseFloat(input.readValue());
			}
			if(key.equals("BPMS")) {
				this.bpmChanges = parseBPMs(input.readValue());
			}
			if(key.equals("STOPS")) {
				this.stops = parseStops(input.readValue());
			}
			if(key.equals("NOTES")) {
				//parseNotes(input);
				Chart c = new Chart(input.readValue());
				this.charts.put(c.difficulty, c);
			}
		}
		
		// calculate timings
		this.timingData = new TimingData(0, bpmChanges, stops);
	}


	private Map<Integer, Float> parseStops(String input) {
		HashMap<Integer, Float> stops = new HashMap<Integer, Float>();
		for(String stopTimesString : input.split(",")) {
			String[] stopTimes = stopTimesString.split("="); 
			stops.put((int)Float.parseFloat(stopTimes[0]), Float.parseFloat(stopTimes[1]));
		}
		return stops;
	}


	private HashMap<Integer, Float> parseBPMs(String input) {
		HashMap<Integer, Float> bpmChanges = new HashMap<Integer, Float>();
		for(String bpmChangeString : input.split(",")) {
			String[] bpmChange = bpmChangeString.split("="); 
			bpmChanges.put((int)Float.parseFloat(bpmChange[0]), Float.parseFloat(bpmChange[1]));
		}
		return bpmChanges;
	}
}
