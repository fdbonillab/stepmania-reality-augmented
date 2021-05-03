package com.fdbgames.mole.sa.io.files;

public class TimingSegment {
	public int sBeat, eBeat;
	public float bps, sTime, eTime;
	
	public TimingSegment(int keyBeat, int nextKeyBeat, float bps, float startTime) {
		this.bps = bps;
		this.sBeat = keyBeat;
		this.eBeat = nextKeyBeat;
		this.sTime = startTime;
		this.eTime = sTime + (eBeat - sBeat) / this.bps;
		System.out.println("Created timing segment: " + this.toString());
	}

	public TimingSegment(int beat, float startTime, float stopDuration) {
		this.bps = 0;
		this.sBeat = beat;
		this.eBeat = beat;
		this.sTime = startTime;
		this.eTime = startTime + stopDuration;
		System.out.println("Created stop: " + this.toString());
	}

	public float getTime(float beat) {
		return sTime + ((beat - sBeat) / bps);
	}

	public float getBeat(float time) {
		// returns the closest beat to this time
		return sBeat + ((time - sTime) * bps);
	}
	
	public String toString() {
		return String.format("{TimingSegment|bps:%f|bDuration:%d-%d|tDuration:%.3f-%.3f}", bps, sBeat, eBeat, sTime, eTime);
	}
}
