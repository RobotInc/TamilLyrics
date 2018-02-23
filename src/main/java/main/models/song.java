package main.models;

import main.untility.allLyrics;

public class song {

    int song_id;
    int album_id;
    String song_title;
    String genre;
    String lyricist;
    Number trackNo;
    allLyrics lyrics;
    String download_link;

    public song() {
    }


    public Number getTrackNo() {
        return trackNo;
    }

    public void setTrackNo(Number trackNo) {
        this.trackNo = trackNo;
    }

    public allLyrics getLyrics() {
        return lyrics;
    }

    public void setLyrics(allLyrics lyrics) {
        this.lyrics = lyrics;
    }

    public int getSong_id() {
        return song_id;
    }

    public void setSong_id(int song_id) {
        this.song_id = song_id;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
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



    public String getDownload_link() {
        return download_link;
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }
}
