package com.fdbgames.mole.stepmaniac;

public class PackSong {
    String fileStepmania;
    String fileSong;

    public PackSong(String fileStepmania) {
        this.fileStepmania = fileStepmania;
    }

    public PackSong(String fileStepmania, String fileSong) {
        this.fileStepmania = fileStepmania;
        this.fileSong = fileSong;
    }

    public String getFileStepmania() {
        return fileStepmania;
    }

    public void setFileStepmania(String fileStepmania) {
        this.fileStepmania = fileStepmania;
    }

    public String getFileSong() {
        return fileSong;
    }

    public void setFileSong(String fileSong) {
        this.fileSong = fileSong;
    }
}
