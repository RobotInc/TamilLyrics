package main.database;

import main.Controller;
import main.untility.observablevalues;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DatabaseHandler{

    private static final int DATABASE_VERSION = 1;
    private static Connection connect = null;
    private static Statement statement = null;
    private static PreparedStatement preparedStatement = null;
    private static ResultSet resultSet = null;

    // Database Name
    private static final String DATABASE_NAME = "beyonity_albums";
    private static final String TABLE_albums = "albums";
    private static final String TABLE_language = "language";
    private static final String TABLE_songs = "songs";
    private static final String TABLE_IMAGES = "images";
    private static final String TABLE_FAVORITE = "favorite";

    //album
    private static final String KEY_ALBUM_ID = "album_id";
    private static final String KEY_ALBUM_NAME = "album_name";
    private static final String KEY_ARTIST = "artist";
    private static final String KEY_HERO = "hero";
    private static final String KEY_HEROIN = "heroin";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_YEAR = "year";
    private static final String KEY_IMAGE_LINK = "image_link";

    //language
    private static final String KEY_LANGUAGE_ID = "id";
    private static final String KEY_LANGUAGE_NAME = "language_name";

    //songs
    private static final String KEY_SONG_ID = "song_id";
    private static final String KEY_SONG_TITLE = "song_title";
    private static final String KEY_DOWNLOAD_LINK = "DOWNLOAD_LINK";
    private static final String KEY_LYRICIST = "lyricist";
    private static final String KEY_TRACK_NO = "track_no";


    //images
    private static final String KEY_IMAGE_BLOB = "image";
    private static final String TAG = DatabaseHandler.class.getSimpleName();

    //favorites
    private static final String KEY_USER_ID = "user_id";





    static HashMap<String,Object> response = new HashMap();
    public static HashMap<String,Object> connect() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connect = DriverManager
                    .getConnection("jdbc:mysql://139.99.8.128/beyonity_tamillyrics?useUnicode=true&characterEncoding=utf-8&"
                            + "user=beyonity_admin&password=@Beyonity2017");
            System.out.println("connected");
            response.put("error",false);
            response.put("message","Successfully connected to database");
        } catch (ClassNotFoundException e) {
            response.put("error",true);

            response.put("error",e.getMessage());
            e.printStackTrace();
        } catch (SQLException e) {
            response.put("error",true);

            response.put("message",e.getMessage());
            e.printStackTrace();
        }
        return response;
    }


    public static HashMap<String,Object> addArtist(String name){
        String query = "INSERT INTO artist(artist_name)" +
                "VALUES (?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            response.put("error",false);
            response.put("message","sccessfully added");
            return response;
        } catch (SQLException e) {
            response.put("error",true);
            response.put("message",e.getMessage());
            return response;
        }
    }


    public static HashMap<String,Object> addHero(String name){
        String query = "INSERT INTO hero(hero_name)" +
                "VALUES (?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            response.put("error",false);
            response.put("message","sccessfully added");
            return response;
        } catch (SQLException e) {
            response.put("error",true);
            response.put("message",e.getMessage());
            return response;
        }
    }
    public static HashMap<String,Object> addHeroin(String name){
        String query = "INSERT INTO heroin(heroin_name)" +
                "VALUES (?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            response.put("error",false);
            response.put("message","sccessfully added");
            return response;
        } catch (SQLException e) {
            response.put("error",true);
            response.put("message",e.getMessage());
            return response;
        }
    }
    public static HashMap<String,Object> addLyricist(String name){
        String query = "INSERT INTO lyricist(lyricist_name)" +
                "VALUES (?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            response.put("error",false);
            response.put("message","sccessfully added");
            return response;
        } catch (SQLException e) {
            response.put("error",true);
            response.put("message",e.getMessage());
            return response;
        }
    }
    public static HashMap<String,Object> addGenre(String name){
        String query = "INSERT INTO genre(genre_name)" +
                "VALUES (?)";

        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connect.prepareStatement(query);
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            response.put("error",false);
            response.put("message","sccessfully added");
            return response;
        } catch (SQLException e) {
            response.put("error",true);
            response.put("message",e.getMessage());
            return response;
        }
    }

    public static void setListsFromDatabase(){
        String selectArtist = "Select * from artist";
        String selectHero = "Select * from hero";
        String selectHeroin = "Select * from heroin";
        String selectlyricist = "Select * from lyricist";
        String selectgenre = "Select * from genre";

       Statement artist;
       Statement hero;
       Statement heroin;
       Statement lyricist;
       Statement genre;

       ResultSet rs1;
       ResultSet rs2;
       ResultSet rs3;
       ResultSet rs4;
       ResultSet rs5;

       observablevalues.clearAll();

       try {
           artist = connect.createStatement();
           rs1 = artist.executeQuery(selectArtist);
           while (rs1.next()){

               observablevalues.setArtistlist(rs1.getString("artist_name"));
               observablevalues.setCreateArtistlist(rs1.getString("artist_name"));

           }
           observablevalues.setArtistlist("Select Artist (Default All)");

           hero = connect.createStatement();
           rs2 = hero.executeQuery(selectHero);
           while (rs2.next()){
               observablevalues.setHerolist(rs2.getString("hero_name"));
               observablevalues.setCreateHerolist(rs2.getString("hero_name"));
           }

           observablevalues.setHerolist("Select Hero (Default All)");
           heroin = connect.createStatement();
           rs3 = heroin.executeQuery(selectHeroin);
           while (rs3.next()){
               observablevalues.setHeroinlist(rs3.getString("heroin_name"));
               observablevalues.setCreateHeroinlist(rs3.getString("heroin_name"));
           }
           observablevalues.setHeroinlist("Select Heroin (Default All)");
           lyricist = connect.createStatement();
           rs4 = lyricist.executeQuery(selectlyricist);
           while (rs4.next()){
               observablevalues.setLyricistlist(rs4.getString("lyricist_name"));
           }
           observablevalues.setLyricistlist("Select Lyricist (Default All)");

           genre = connect.createStatement();
           rs5 = genre.executeQuery(selectgenre);
           while (rs5.next()){
               observablevalues.setGenrelist(rs5.getString("genre_name"));
           }
           observablevalues.setGenrelist("Select Genre (Default All)");


       }catch (SQLException e){


           e.printStackTrace();

       }





    }

}
