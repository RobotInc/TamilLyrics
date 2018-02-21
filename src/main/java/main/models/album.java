package main.models;

public class album {
    int album_id;
    String album_name;
    int artist_id;
    int hero_id;
    int heroin_id;
    int year;
    String image_link;

    public album(int album_id, String album_name, int artist_id, int hero_id, int heroin_id, int year, String image_link) {
        this.album_id = album_id;
        this.album_name = album_name;
        this.artist_id = artist_id;
        this.hero_id = hero_id;
        this.heroin_id = heroin_id;
        this.year = year;
        this.image_link = image_link;
    }

    public int getAlbum_id() {
        return album_id;
    }

    public void setAlbum_id(int album_id) {
        this.album_id = album_id;
    }

    public String getAlbum_name() {
        return album_name;
    }

    public void setAlbum_name(String album_name) {
        this.album_name = album_name;
    }

    public int getArtist_id() {
        return artist_id;
    }

    public void setArtist_id(int artist_id) {
        this.artist_id = artist_id;
    }

    public int getHero_id() {
        return hero_id;
    }

    public void setHero_id(int hero_id) {
        this.hero_id = hero_id;
    }

    public int getHeroin_id() {
        return heroin_id;
    }

    public void setHeroin_id(int heroin_id) {
        this.heroin_id = heroin_id;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getImage_link() {
        return image_link;
    }

    public void setImage_link(String image_link) {
        this.image_link = image_link;
    }
}
