package com.android.fusicplayer.fm_module.util;

import com.google.gson.annotations.SerializedName;

public class Shoutcast {

    private String name = "";

    @SerializedName("stream")
    private String url = "";

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}
