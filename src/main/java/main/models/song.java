package main.models;

import main.untility.allLyrics;

public class song {

    String song_id;
    String album_id;
    String song_title;
    String genre;
    String lyricist;
    String trackNo;
    allLyrics lyrics;
    String download_link;

    public song(String song_id, String album_id, String song_title, String genre, String lyricist, String trackNo, String download_link) {
        this.song_id = song_id;
        this.album_id = album_id;
        this.song_title = song_title;
        this.genre = genre;
        this.lyricist = lyricist;
        this.trackNo = trackNo;
        this.download_link = download_link;
    }

    public song() {
    }

    public String getSong_id() {
        return song_id;
    }

    public void setSong_id(String song_id) {
        this.song_id = song_id;
    }

    public String getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(String album_id) {
        this.album_id = album_id;
    }

    public String getSong_title() {
        return song_title;
    }

    public void setSong_title(String song_title) {
        this.song_title = song_title;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLyricist() {
        return lyricist;
    }

    public void setLyricist(String lyricist) {
        this.lyricist = lyricist;
    }

    public String getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(String trackNo) {
        this.trackNo = trackNo;
    }

    public allLyrics getLyrics() {
        return lyrics;
    }

    public void setLyrics(allLyrics lyrics) {
        this.lyrics = lyrics;
    }

    public String getDownload_link() {
        return download_link;
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }

    public void setDefaultToAll(){
        this.setSong_id("0");
        this.setAlbum_id("0");
        this.setSong_title("none");
        this.setGenre("none");
        this.setLyricist("none");
        this.setTrackNo("0");
        this.setDownload_link("none");
    }
}
