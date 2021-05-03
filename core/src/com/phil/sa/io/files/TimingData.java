package com.phil.sa.io.files;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.TreeSet;

public class TimingData extends LinkedList<TimingSegment> {
	private static final long serialVersionUID = 1L;

	public TimingData(int beats, Map<Integer, Float> bpms, Map<Integer, Float> stops) {
		super();
		
		// create sorted set of key beats
		TreeSet<Integer> keyBeatSet = new TreeSet<Integer>();
		keyBeatSet.addAll(bpms.keySet());
		Integer[] keyBeats = keyBeatSet.toArray(new Integer[keyBeatSet.size()]);
		
		// create timing segments per pair of keyframes
		TimingSegment seg = null;
		float bps = 0f;
		float time = 0f;
		for(int i = 0; i < keyBeats.length; i++ ) {
			if(stops.containsKey(keyBeats[i])) { // stops
				seg = new TimingSegment(keyBeats[i], time, stops.get(keyBeats[i]));
				time = seg.eTime;
				this.add(seg);
			}
			if(bpms.containsKey(keyBeats[i])) { // bpm changes
				bps = bpms.get(keyBeats[i]) / 60f;
				seg = new TimingSegment(keyBeats[i], (i+1<keyBeats.length)?keyBeats[i+1]:Integer.MAX_VALUE, bps, time);
				time = seg.eTime;
				this.add(seg);
			}
		}
	}
	
	public float getTime(float beat) {
		for(TimingSegment t : this) {
			if(beat < t.eBeat) {
				return t.getTime(beat);
			}
		}
		return -1f;
	}
	
	public float getBeat(float time) {
		for(TimingSegment t : this) {
			if(time <= t.eTime) {
				return t.getBeat(time);
			}
		}
		return -1;
	}
	
	
}

