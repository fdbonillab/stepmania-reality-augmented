package com.fdbgames.mole.util;

public class SMUtils {
	public static <T> boolean arrayContains(T[] array, T target) {
		for(int i = 0; i < array.length; i++)
			if(array[i] == target)
				return true;
		return false;
	}

	public static boolean arrayContains(int[] array, int target) {
		for(int i = 0; i < array.length; i++)
			if(array[i] == target)
				return true;
		return false;
	}
	

	public static float calculateBeat(int measure, int div, int beat) {
		return 4 * (measure + beat / (float)div);
	}

	public static int getOrder(int beat, int div) {
		if(div == 4) {
			return 0;
		}
		if(div == 8) {
			return beat % 2;
		}
		if(div == 12) {
			return ((beat % 3) * 2) % 3;
		}
		if(div == 16) {
			switch(beat % 4) {
			case 0: return 0;
			case 1: return 3;
			case 2: return 1;
			case 3: return 3;
			}
		}
		return 0;
	}
	
}
