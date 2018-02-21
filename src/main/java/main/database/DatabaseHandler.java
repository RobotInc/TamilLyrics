package main.database;

import com.mysql.cj.jdbc.interceptors.ResultSetScannerInterceptor;
import com.mysql.cj.xdevapi.SqlDataResult;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import main.Controller;
import main.models.album;
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


    final static  String imgLink = "https://beyonitysoftwares.cf/tamillyrics/img/";



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

    public static HashMap<String,Object> insertAlbum(String album_name,String artist_name,String hero_name,String heroin_name,int year){

        int artist_id = getArtistId(artist_name);
        int hero_id = getHeroId(hero_name);
        int heroin_id = getHeroinId(heroin_name);
        if(artist_id>0&&hero_id>0&&heroin_id>0){

            try {

                String query = "INSERT INTO albums(album_name,artist_id,hero_id,heroin_id,year)" +
                        "VALUES(?,?,?,?,?)";
                PreparedStatement ps = connect.prepareStatement(query);
                ps.setString(1,album_name);
                ps.setInt(2,artist_id);
                ps.setInt(3,hero_id);
                ps.setInt(4,heroin_id);
                ps.setInt(5,year);
                ps.executeUpdate();

                response.put("error",false);
                response.put("message","Successfully inserted the album");
                response.put("album_id",getAlbumId(album_name));

            } catch (SQLException e) {
                response.put("error",true);
                response.put("message",e.getMessage());
                e.printStackTrace();
            }

        }else {

            response.put("error",true);
            response.put("message","all fields required");
        }

        return response;

    }


    public  static int getArtistId(String artist_name){
        String query = "SELECT id from artist WHERE artist_name = '"+artist_name+"'";
        int value = 0;
        ResultSet resultSet;
        Statement st;
        try {
            st = connect.createStatement();
            resultSet = st.executeQuery(query);
            while (resultSet.next()){
                value = resultSet.getInt("id");

            }
            return value;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return value;
    }

    public  static String getArtistName(int artist_id){
        String query = "SELECT artist_name from artist WHERE id = '"+artist_id+"'";
        String value = "";
        ResultSet resultSet;
        Statement st;
        try {
            st = connect.createStatement();
            resultSet = st.executeQuery(query);
            while (resultSet.next()){
                value = resultSet.getString("artist_name");

            }
            return value;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return value;
    }

    public  static int getHeroId(String hero_name){
        String query = "SELECT id from hero WHERE hero_name = '"+hero_name+"'";
        int value = 0;
        ResultSet resultSet;
        Statement st;
        try {
            st = connect.createStatement();
            resultSet = st.executeQuery(query);
            while (resultSet.next()){
                value = resultSet.getInt("id");

            }
            return value;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return value;
    }

    public  static String getHeroName(int id){
        String query = "SELECT hero_name from hero WHERE id = '"+id+"'";
        String value = "";
        ResultSet resultSet;
        Statement st;
        try {
            st = connect.createStatement();
            resultSet = st.executeQuery(query);
            while (resultSet.next()){
                value = resultSet.getString("hero_name");

            }
            return value;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return value;
    }

    public  static int getHeroinId(String heroin_name){
        String query = "SELECT id from heroin WHERE heroin_name = '"+heroin_name+"'";
        int value = 0;
        ResultSet resultSet;
        Statement st;
        try {
            st = connect.createStatement();
            resultSet = st.executeQuery(query);
            while (resultSet.next()){
                value = resultSet.getInt("id");

            }
            return value;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return value;
    }
    public  static String getHeroinName(int id){
        String query = "SELECT heroin_name from heroin WHERE id = '"+id+"'";
        String value = "";
        ResultSet resultSet;
        Statement st;
        try {
            st = connect.createStatement();
            resultSet = st.executeQuery(query);
            while (resultSet.next()){
                value = resultSet.getString("heroin_name");

            }
            return value;
        }catch (SQLException e){
            e.printStackTrace();
        }
        return value;
    }

    public static int getAlbumId(String album_name){
        int id = 0;
        String query = "SELECT id FROM albums WHERE album_name = '"+album_name+"'";
        try {
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                    id = rs.getInt("id");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return id;
    }

    public static void getAlbums(){
        ObservableList<String> list =FXCollections.observableArrayList();
        String query = "Select album_name from albums";
        try{
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                //int album_id = rs.getInt("id");
                String album_name = rs.getString("album_name");
               /* int artist_id = rs.getInt("artist_id");
                int hero_id = rs.getInt("hero_id");
                int heroin_id = rs.getInt("heroin_id");
                int year = rs.getInt("year");
                String image_link = "";
                if(rs.getString("image_link")!=null){
                    image_link = rs.getString("image_link");
                }
                album a = new album(album_id,album_name,artist_id,hero_id,heroin_id,year,image_link);

                list.add(a);*/

                observablevalues.setAlbumNameList(album_name);
                }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public static album getAlbum(String album_name){

        album a = null;
        String query = "Select * from albums WHERE album_name = '"+album_name+"'";
        try{
            Statement st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                int album_id = rs.getInt("id");
                String name = rs.getString("album_name");
                int artist_id = rs.getInt("artist_id");
                int hero_id = rs.getInt("hero_id");
                int heroin_id = rs.getInt("heroin_id");
                int year = rs.getInt("year");
                String image_link = "";
                if(rs.getString("image_link")!=null){
                    image_link = rs.getString("image_link");
                }
                a = new album(album_id,name,artist_id,hero_id,heroin_id,year,image_link);




            }
        }catch (SQLException e){
            e.printStackTrace();
        }

        return a;
    }

    public static void setImageLink(int album_id){

            String query = "UPDATE albums set image_link = '" + imgLink + "" + album_id + ".png' WHERE id = '" + album_id + "'";
            System.out.println(query);

            PreparedStatement st = null;
            try {
                st = connect.prepareStatement(query);
                st.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }


    }

    public static boolean getImageNull(int album_id){
        boolean answer = false;
        String query = "Select image_link from albums where id = '"+album_id+"'";
        Statement st = null;
        try {
            st = connect.createStatement();
            ResultSet rs = st.executeQuery(query);
            while (rs.next()){
                String value = rs.getString("image_link");
                if(value==null){
                    answer = true;
                }else {
                    answer = false;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return answer;

    }
}
