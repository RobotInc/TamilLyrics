package main.untility;

import java.util.HashMap;

public class ObserveLyrics {

    static HashMap<String,String> lyrics = new HashMap<>();
    public static void init(){
        lyrics.put("englishone","");
        lyrics.put("englishtwo","");
        lyrics.put("tamilone","");
        lyrics.put("tamiltwo","");
        lyrics.put("edited","no");

    }

    public static HashMap<String, String> getLyrics() {
        return lyrics;
    }

    public static void setLyrics(HashMap<String, String> lyrics) {
        ObserveLyrics.lyrics = lyrics;
    }
}
