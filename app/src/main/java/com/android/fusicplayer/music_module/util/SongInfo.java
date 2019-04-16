package com.android.fusicplayer.music_module.util;

public class SongInfo {

    private String songName;
    private String songURL;

    public SongInfo(String songName, String songURL) {
        this.songName = songName;
        this.songURL = songURL;
    }

    public String getSongName() {
        return songName;
    }

    public String getSongURL() {
        return songURL;
    }
}
