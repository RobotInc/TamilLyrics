package main;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.*;
import main.database.DatabaseHandler;
import main.ftp.FTPUploader;
import main.models.album;
import main.models.song;
import main.untility.Helper;
import main.untility.observablevalues;
import org.controlsfx.control.StatusBar;

import javax.imageio.ImageIO;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Controller {

    @FXML
    TableView songtable;

    boolean albumset = false;
    private PrintStream ps ;
    @FXML
    ImageView albumimage;

    ObservableList createcomboartistlist = FXCollections.observableArrayList();
    ObservableList createcomboherolist = FXCollections.observableArrayList();
    ObservableList createcomboheroinlist = FXCollections.observableArrayList();
    ObservableList<album> albumlistview = FXCollections.observableArrayList();
    ObservableList<String> albumnameList = FXCollections.observableArrayList();

    private static final Logger LOG = Logger.getLogger(String.valueOf(Controller.class));



    @FXML
    TextField albumname,year,albumid;

    @FXML
    private TextArea logarea;
    @FXML
    ListView<String> albumview;

    @FXML
    ComboBox artistcombo,herocombo,lyricistcombo,heroincombo,genrecombo,comboartist,combohero,comboheroin;
    @FXML
    StatusBar status;
    @FXML
    GridPane contentgrid;
    HashMap<String,Object> reponse = new HashMap<String, Object>();

    @FXML
    Button newalbum,clearalbumfields;

    @FXML
    Button add = new Button();

    FTPUploader ftpUploader;

    @FXML
    private void initialize(){

       reponse = DatabaseHandler.connect();


        ps = new PrintStream(new Console(logarea));
        System.setOut(ps);
        System.setErr(ps);
        add.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent actionEvent) {
               try {

                   FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/add.fxml"));

                   Parent root1 = null;
                   root1 = (Parent) fxmlLoader.load();
                   Stage stage = new Stage();
                   stage.initModality(Modality.APPLICATION_MODAL);
                   stage.initStyle(StageStyle.DECORATED);
                   stage.setTitle("Add");
                   stage.setScene(new Scene(root1));
                   stage.setMaximized(false);
                   stage.setResizable(false);
                   stage.show();
                   stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                       public void handle(WindowEvent windowEvent) {
                          setAll();
                       }
                   });
               } catch (IOException e) {
                   e.printStackTrace();
               }



           }
       });

        if(!(Boolean) reponse.get("error")){

            System.out.println("connec to database\n");
            DatabaseHandler.setListsFromDatabase();
            DatabaseHandler.getAlbums();
            setAll();

        }else {
            status.setText("Status: Error Connecting");

        }

        year.textProperty().addListener(new ChangeListener<String>() {
                                             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                                                 if (!newValue.matches("\\d*")) {
                                                     year.setText(newValue.replaceAll("[^\\d]", ""));
                                                 }if(newValue.length()>4){
                                                     year.setText(oldValue);
                                                 }
                                             }
                                         });

        newalbum.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(comboartist.getSelectionModel().getSelectedItem().equals("None")||combohero.getSelectionModel().getSelectedItem().equals("None")||comboheroin.getSelectionModel().getSelectedItem().equals("None")){
                    status.setText("Fields cannot be none");
                }else {
                   if(year.getText().length()==4&&albumname.getText().length()>0){
                       if(albumid.getText().length()==0) {
                           System.out.println(albumname.getText());
                           System.out.println(comboartist.getSelectionModel().getSelectedItem().toString());
                           System.out.println(combohero.getSelectionModel().getSelectedItem().toString());
                           System.out.println(comboheroin.getSelectionModel().getSelectedItem().toString());

                           System.out.println(year.getText().toString());
                           reponse = DatabaseHandler.insertAlbum(Helper.FirstLetterCaps(albumname.getText().toString()), comboartist.getSelectionModel().getSelectedItem().toString(), combohero.getSelectionModel().getSelectedItem().toString(), comboheroin.getSelectionModel().getSelectedItem().toString(), Integer.parseInt(year.getText().toLowerCase()));

                           if (!(Boolean) reponse.get("error")) {
                               status.setText((String) reponse.get("message"));
                               albumid.setText(String.valueOf(reponse.get("album_id")));
                           } else {
                               status.setText((String) reponse.get("message"));
                           }
                       }else {
                           status.setText("Album Id "+albumid.getText()+" is already set to the movie "+albumname.getText()+" please click to update the album if u want to update or click clear button to add new album");
                       }
                   }else {
                       status.setText("All fields required");
                   }
                }
            }
        });


        albumview.setItems(observablevalues.getAlbumNameList());
        albumview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {

                album  a = DatabaseHandler.getAlbum(newValue);
                if(a!=null){
                    System.out.println(a.getAlbum_id());
                    System.out.println(a.getAlbum_name());
                    System.out.println(a.getArtist_id());
                    System.out.println(a.getHero_id());
                    System.out.println(a.getHeroin_id());
                    System.out.println(a.getYear());
                    System.out.println(a.getImage_link());

                    albumid.setText(getInt(a.getAlbum_id()));
                    albumname.setText(a.getAlbum_name());
                    /*int artistindex = 0;
                    int heroindex = 0;
                    int heroinindex = 0;
                    String artistName = DatabaseHandler.getArtistName(a.getArtist_id());
                    String heroname =   DatabaseHandler.getHeroName(a.getHero_id());
                    String heroinname = DatabaseHandler.getHeroinName(a.getHeroin_id());

                    for(Object name : observablevalues.getCreateArtistlist()){
                        if(name.equals(artistName)){
                            artistindex = observablevalues.getCreateArtistlist().indexOf(name);
                        }
                    }

                    for(Object name : observablevalues.getCreateHerolist()){
                        if(name.equals(heroname)){
                            heroindex = observablevalues.getCreateHerolist().indexOf(name);
                        }
                    }
                      for(Object name : observablevalues.getHeroinlist()){
                        if(name.equals(heroinname)){
                            heroinindex = observablevalues.getCreateHeroinlist().indexOf(name);
                        }
                    }*/

                    comboartist.getSelectionModel().select(DatabaseHandler.getArtistName(a.getArtist_id()));
                    combohero.getSelectionModel().select(DatabaseHandler.getHeroName(a.getHero_id()));
                    comboheroin.getSelectionModel().select(DatabaseHandler.getHeroinName(a.getHeroin_id()));

                    Runnable r = new Runnable() {
                        @Override
                        public void run() {
                            Image i = new Image(a.getImage_link());
                            Timer it = new Timer("Timer");
                            TimerTask ts = new TimerTask() {
                                @Override
                                public void run() {
                                    status.setProgress(i.getProgress());
                                }
                            };

                            it.scheduleAtFixedRate(ts,1000,10);
                            albumimage.setImage(i);
                        }
                    };
                    Thread t = new Thread(r);
                    t.start();
                    year.setText(String.valueOf(a.getYear()));
                    albumset = true;
                }

            }
        });


        albumimage.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(albumid.getText().length()<1){
                    status.setText("please create or select a album to upload a image");
                    return;
                }
                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);

                //Show open file dialog
                File file = fileChooser.showOpenDialog(null);


                    //BufferedImage bufferedImage = ImageIO.read(file);
                    //Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                   Image image = new Image(file.toURI().toString(),205,200,false,false);
                    albumimage.setImage(image);

                try {
                    File remoteFile = new File(albumid.getText().toString()+".png");
                    ImageIO.write(
                            SwingFXUtils.fromFXImage(
                                    image,
                                    null),
                            "png",
                           remoteFile);
                    try {
                        ftpUploader.uploadFile(remoteFile.getAbsolutePath(),"/"+remoteFile.getName(),"/img");
                        DatabaseHandler.setImageLink(Integer.parseInt(albumid.getText()));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });

        clearalbumfields.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Image i = new Image(String.valueOf(getClass().getResource("/ic_launcher.png")));
                albumimage.setImage(i);
                albumid.clear();
                albumname.clear();
                year.clear();
                comboartist.getSelectionModel().selectLast();
                combohero.getSelectionModel().selectLast();
                comboheroin.getSelectionModel().selectLast();
                albumset = false;
                albumview.getSelectionModel().clearSelection();
            }
        });
        try {
            ftpUploader = new FTPUploader("139.99.2.52", "tamillyrics@beyonitysoftwares.cf", "mohanravi1990");
        } catch (Exception e) {

            e.printStackTrace();
        }

        setSongTable();
    }

    private void setSongTable() {
        ObservableList<song> songtablelist = FXCollections.observableArrayList();
        songtablelist.add(new song());

        TableColumn<song,Integer> song_id = new TableColumn("Song Id");
        song_id.setCellValueFactory(new PropertyValueFactory<song,Integer>("song_id"));
        TableColumn<song,Integer> album_id = new TableColumn("Album Id");
        album_id.setCellValueFactory(new PropertyValueFactory<song,Integer>("album_id"));
        TableColumn<song,String> song_title = new TableColumn("Song Title");
        song_title.setCellValueFactory(new PropertyValueFactory<song,String>("song_title"));
        TableColumn<song,String> genre = new TableColumn("Genre");
        genre.setCellValueFactory(new PropertyValueFactory<song,String>("genre"));
        TableColumn<song,String> lyricist = new TableColumn("Lyricist");
        lyricist.setCellValueFactory(new PropertyValueFactory<song,String>("lyricist"));
        TableColumn<song,String> english_one = new TableColumn("English One");
        english_one.setCellValueFactory(new PropertyValueFactory<song,String>("english_one"));
        TableColumn<song,String> english_two = new TableColumn("English Two");
        english_two.setCellValueFactory(new PropertyValueFactory<song,String>("english_two"));
        TableColumn<song,String> tamil_one = new TableColumn("Tamil One");
        tamil_one.setCellValueFactory(new PropertyValueFactory<song,String>("tamil_one"));
        TableColumn<song,String> tamil_two = new TableColumn("Tamil Two");
        tamil_two.setCellValueFactory(new PropertyValueFactory<song,String>("tamil_two"));
        TableColumn<song,String> download_link = new TableColumn("Download Link");
        download_link.setCellValueFactory(new PropertyValueFactory<song,String>("download_link"));
        TableColumn upload = new TableColumn("Upload Song");


        songtable.getColumns().addAll(song_id,album_id,song_title,genre,lyricist,english_one,english_two,tamil_one,tamil_two,download_link,upload);
        songtable.setItems(songtablelist);

    }

    public void setStatus(String message){
        status.setText("Status: "+message);
    }


    public void setAll(){

        artistcombo.setItems(observablevalues.getArtistlist());
        herocombo.setItems(observablevalues.getHerolist());
        heroincombo.setItems(observablevalues.getHeroinlist());
        lyricistcombo.setItems(observablevalues.getLyricistlist());
        genrecombo.setItems(observablevalues.getGenrelist());


        comboartist.setItems(observablevalues.getCreateArtistlist());
        comboartist.getItems().add("None");
        combohero.setItems(observablevalues.getCreateHerolist());
        combohero.getItems().add("None");
        comboheroin.setItems(observablevalues.getCreateHeroinlist());
        comboheroin.getItems().add("None");


        artistcombo.getSelectionModel().selectLast();
        herocombo.getSelectionModel().selectLast();
        heroincombo.getSelectionModel().selectLast();
        lyricistcombo.getSelectionModel().selectLast();
        genrecombo.getSelectionModel().selectLast();

        comboartist.getSelectionModel().selectLast();
        combohero.getSelectionModel().selectLast();
        comboheroin.getSelectionModel().selectLast();

    }

    private String getInt(int value){
        return String.valueOf(value);
    }
    public class Console extends OutputStream {
        private TextArea console;

        public Console(TextArea console) {
            this.console = console;
        }

        public void appendText(String valueOf) {
            Platform.runLater(() -> console.appendText(valueOf));
        }

        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }
}


