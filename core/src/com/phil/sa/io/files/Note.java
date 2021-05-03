package com.phil.sa.io.files;

import java.util.regex.Pattern;

/**
 * Represents a single row of arrows
 * @author Phil
 *
 */
public class Note {
	public static final Pattern pattern = Pattern.compile("((\\d){4})[\\r\\n]+");
	public char[] data;
	public Note(String noteString) {
		this.data = noteString.toCharArray();
	}
	public float getTime() {
		return 0;
	}
	public String toString() {
		return new String(data);
	}
}
