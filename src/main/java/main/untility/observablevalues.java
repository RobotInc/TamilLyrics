package main.untility;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class observablevalues {

    public static ObservableList<String> artistlist = FXCollections.observableArrayList();
    public static ObservableList<String> createArtistlist = FXCollections.observableArrayList();
    public static ObservableList<String> herolist = FXCollections.observableArrayList();
    public static ObservableList<String> createHerolist = FXCollections.observableArrayList();
    public static ObservableList<String> heroinlist = FXCollections.observableArrayList();
    public static ObservableList<String> createHeroinlist = FXCollections.observableArrayList();
    public static ObservableList<String> lyricistlist = FXCollections.observableArrayList();
    public static ObservableList<String> genrelist = FXCollections.observableArrayList();
    public static ObservableList<String> albumNameList = FXCollections.observableArrayList();

    public static ObservableList<String> getAlbumNameList() {
        return albumNameList;
    }

    public static void setAlbumNameList(String albumName) {
        albumNameList.add(albumName);
    }

    public static ObservableList getCreateArtistlist() {
        return createArtistlist;
    }

    public static void setCreateArtistlist(String createArtist) {
       createArtistlist.add(createArtist);
    }

    public static ObservableList getCreateHerolist() {
        return createHerolist;
    }

    public static void setCreateHerolist(String createHero) {
        createHerolist.add(createHero);
    }

    public static ObservableList getCreateHeroinlist() {
        return createHeroinlist;
    }

    public static void setCreateHeroinlist(String createHeroin) {
       createHeroinlist.add(createHeroin);
    }

    public static ObservableList getArtistlist() {
        return artistlist;
    }

    public static void setArtistlist(String artist) {
        artistlist.add(artist);
    }

    public static ObservableList getHerolist() {
        return herolist;
    }

    public static void setHerolist(String hero) {
        herolist.add(hero);
    }

    public static ObservableList getHeroinlist() {
        return heroinlist;
    }

    public static void setHeroinlist(String heroin) {
        heroinlist.add(heroin);
    }

    public static ObservableList getLyricistlist() {
        return lyricistlist;
    }

    public static void setLyricistlist(String lyricist) {
        lyricistlist.add(lyricist);
    }

    public static ObservableList getGenrelist() {
        return genrelist;
    }

    public static void setGenrelist(String genre) {
        genrelist.add(genre);
    }

    public static void clearAll(){
        artistlist.clear();
        lyricistlist.clear();
        heroinlist.clear();
        herolist.clear();
        genrelist.clear();
        createArtistlist.clear();
        createHerolist.clear();
        createHeroinlist.clear();
    }
}
