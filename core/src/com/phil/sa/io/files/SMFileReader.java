package com.phil.sa.io.files;

import java.io.IOException;
import java.io.InputStreamReader;

import com.badlogic.gdx.files.FileHandle;

public class SMFileReader extends InputStreamReader {
	public SMFileReader(FileHandle smFile) {
		super(smFile.read());
	}
	
	public String readKey() {
		readTo('#');
		return readTo(':');
	}
	public String readTo(char target) { 
		return readTo(target, false);
	}
	public String readTo(char target, boolean includeTarget) {
		StringBuilder sb = new StringBuilder();
		int c;
		try {
			while ((c = read()) > 0) {
				if (c == target) {
					if(includeTarget) sb.append(c);
					return sb.toString();
				}
				sb.append((char) c);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	public static String readUntilChar(InputStreamReader inStream, char target) { 
		return readUntilChar(inStream, target, false);
	}
	public static String readUntilChar(InputStreamReader inStream, char target, boolean includeTargetChar) {
		StringBuilder sb = new StringBuilder();
		int c;
		try {
			while ((c = inStream.read()) > 0) {
				if (c == target) {
					if(includeTargetChar) sb.append(c);
					return sb.toString();
				}
				sb.append((char) c);
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return null;
	}
	
	public static String readNextKey(InputStreamReader isr) {
		readUntilChar(isr, '#');
		return readUntilChar(isr, ':');
	}

	public String readValue() {
		return readTo(';');
	}
	
//	public static String readNextValue
}
