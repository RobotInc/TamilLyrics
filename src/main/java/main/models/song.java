package main.models;

public class song {

    int song_id;
    int album_id;
    String song_title;
    String genre;
    String lyricist;
    String english_one;
    String english_two;
    String tamil_one;
    String tamil_two;
    String download_link;

    public song() {
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

    public String getEnglish_one() {
        return english_one;
    }

    public void setEnglish_one(String english_one) {
        this.english_one = english_one;
    }

    public String getEnglish_two() {
        return english_two;
    }

    public void setEnglish_two(String english_two) {
        this.english_two = english_two;
    }

    public String getTamil_one() {
        return tamil_one;
    }

    public void setTamil_one(String tamil_one) {
        this.tamil_one = tamil_one;
    }

    public String getTamil_two() {
        return tamil_two;
    }

    public void setTamil_two(String tamil_two) {
        this.tamil_two = tamil_two;
    }

    public String getDownload_link() {
        return download_link;
    }

    public void setDownload_link(String download_link) {
        this.download_link = download_link;
    }
}
