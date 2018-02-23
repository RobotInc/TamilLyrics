package main.untility;

import java.util.HashMap;

public class ObserveLyrics {

    static HashMap<String,String> lyrics = new HashMap<>();

    public static HashMap<String, String> getLyrics() {
        return lyrics;
    }

    public static void setLyrics(HashMap<String, String> lyrics) {
        ObserveLyrics.lyrics = lyrics;
    }
}
