package main;

import com.sun.corba.se.spi.ior.iiop.AlternateIIOPAddressComponent;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
import main.database.DatabaseHandler;
import main.ftp.FTPUploader;
import main.models.album;
import main.models.song;
import main.untility.Helper;
import main.untility.ObserveLyrics;
import main.untility.observablevalues;
import org.controlsfx.control.StatusBar;

import javax.imageio.ImageIO;


import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;
import java.util.regex.Pattern;

import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

public class Controller implements FTPUploader.updateProgress {

    @FXML
    TableView<song> songtable;

    boolean albumset = false;
    private PrintStream ps ;
    @FXML
    ImageView albumimage,uploaded;
    ObservableList<song> songtablelist = FXCollections.observableArrayList();
    ObservableList createcomboartistlist = FXCollections.observableArrayList();
    ObservableList createcomboherolist = FXCollections.observableArrayList();
    ObservableList createcomboheroinlist = FXCollections.observableArrayList();
    ObservableList<album> albumlistview = FXCollections.observableArrayList();
    ObservableList<String> albumnameList = FXCollections.observableArrayList();
    int presentSelectedAlbumId = 0;
    private static final Logger LOG = Logger.getLogger(String.valueOf(Controller.class));

    public ObservableList<String> lyricistlist = FXCollections.observableArrayList();
    public ObservableList<String> genrelist = FXCollections.observableArrayList();
    public ObservableList<String> lyricistlistTable = FXCollections.observableArrayList();
    public ObservableList<String> genrelistTable = FXCollections.observableArrayList();
    File imageFile;

    Image green = new Image(String.valueOf(getClass().getResource("/uploaded.png")));
    Image red = new Image(String.valueOf(getClass().getResource("/notuploaded.png")));
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
    Button newalbum,clearalbumfields,imageselect;

    @FXML
    Button add = new Button();

    FTPUploader ftpUploader;

    @FXML
    private void initialize(){

       reponse = DatabaseHandler.connect();


        ps = new PrintStream(new Console(logarea));
        ObserveLyrics.init();
        System.setOut(ps);
        System.setErr(ps);
        add.setOnAction(new EventHandler<ActionEvent>() {
           public void handle(ActionEvent actionEvent) {
               try {

                   if(albumview.getSelectionModel().getSelectedItem()==null){
                       System.out.println("non selected");
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
                               setTableCombo();
                           }
                       });
                   }else {
                       ButtonType continue_add = new ButtonType("Continue Adding", ButtonBar.ButtonData.OK_DONE);
                       ButtonType close = new ButtonType("go back", ButtonBar.ButtonData.CANCEL_CLOSE);

                       Text text = new Text("Looks like u are working with an album and its song, adding new field in the middle of editing an album or song might delete ur changes and refreshes the page, please make sure ur changes were updated otherwise go back and update the chages to database and come back later");
                       text.setWrappingWidth(250);
                       //String content = "Looks like u are working with an album and its song\n, adding new field in the middle of editing an album \nor song might delete ur changes and refreshes the page\n, please make sure ur changes were updated otherwise go back \nand update the chages to database and come back later";
                       Alert alert = new Alert(Alert.AlertType.WARNING,"",continue_add,close);
                       alert.getDialogPane().setContent(text);
                       alert.setTitle("please be aware you might Lose Data");
                       Optional<ButtonType> result = alert.showAndWait();
                       if(result.isPresent() && result.get()==continue_add){
                           album  a = DatabaseHandler.getAlbum(albumview.getSelectionModel().getSelectedItem());
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
                                   setTableCombo();
                                   String artistName = DatabaseHandler.getArtistName(a.getArtist_id());
                                   for(Object artist : comboartist.getItems()){
                                       if(artist.toString().equals(artistName)){
                                           comboartist.getSelectionModel().select(artist);
                                       }
                                   }
                                   String heroName = DatabaseHandler.getHeroName(a.getHero_id());
                                   for(Object hero : combohero.getItems()){
                                       if(hero.toString().equals(heroName)){
                                           combohero.getSelectionModel().select(hero);
                                       }
                                   }
                                   String heroinName = DatabaseHandler.getHeroinName(a.getHeroin_id());
                                   for(Object heroin : comboheroin.getItems()){
                                       if(heroin.toString().equals(heroinName)){
                                           comboheroin.getSelectionModel().select(heroin);
                                       }
                                   }

                               }
                           });
                       }else {
                           return;
                       }
                      alert.setOnCloseRequest(new EventHandler<DialogEvent>() {
                          @Override
                          public void handle(DialogEvent dialogEvent) {

                          }
                      });

                   }


               } catch (IOException e) {
                   e.printStackTrace();
               }



           }
       });

        if(!(Boolean) reponse.get("error")){

            System.out.println("connected to database\n");
            DatabaseHandler.setListsFromDatabase();
            DatabaseHandler.getAlbums();
            setAll();
            initTable();
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
                }else if(imageFile==null){
                    status.setText("please select image!");
                }else {
                   if(year.getText().length()==4&&albumname.getText().length()>0){
                       if(albumid.getText().length()==0) {
                           System.out.print(albumname.getText());
                           System.out.print(comboartist.getSelectionModel().getSelectedItem().toString());
                           System.out.print(combohero.getSelectionModel().getSelectedItem().toString());
                           System.out.print(comboheroin.getSelectionModel().getSelectedItem().toString());

                           System.out.print(year.getText().toString()+"\n");
                           reponse = DatabaseHandler.insertAlbum(Helper.FirstLetterCaps(albumname.getText().toString()), comboartist.getSelectionModel().getSelectedItem().toString(), combohero.getSelectionModel().getSelectedItem().toString(), comboheroin.getSelectionModel().getSelectedItem().toString(), Integer.parseInt(year.getText().toLowerCase()));

                           if (!(Boolean) reponse.get("error")) {
                               status.setText((String) reponse.get("message"));
                               albumid.setText(String.valueOf(reponse.get("album_id")));
                               DatabaseHandler.getAlbums();
                               albumview.getItems().clear();
                               albumview.setItems(observablevalues.getAlbumNameList());
                               if(imageFile !=null){
                                   Image image = new Image(imageFile.toURI().toString(), 205, 200, false, false);
                                   try {
                                       File remoteFile = new File(albumid.getText().toString() + ".png");

                                       ImageIO.write(
                                               SwingFXUtils.fromFXImage(
                                                       image,
                                                       null),
                                               "png",
                                               remoteFile);
                                       try {
                                           ftpUploader.uploadFile(remoteFile.getAbsolutePath(), "/" + remoteFile.getName(), "/img",remoteFile);
                                           DatabaseHandler.setImageLink(Integer.parseInt(albumid.getText()));
                                           uploaded.setImage(green);
                                           DatabaseHandler.getAlbums();
                                           album  a = DatabaseHandler.getAlbum(albumname.getText());
                                           albumview.getSelectionModel().select(a.getAlbum_name());
                                           setSelectedAlbum(a);
                                       } catch (Exception e) {
                                           e.printStackTrace();
                                       }

                                   } catch (IOException e) {
                                       e.printStackTrace();
                                   }
                               }

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
                setSelectedAlbum(a);

            }
        });


        imageselect.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                FileChooser fileChooser = new FileChooser();

                //Set extension filter
                FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
                FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
                fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG);


                //Show open file dialog
                if (albumid.getText().length()>1) {
                    File file = fileChooser.showOpenDialog(null);


                    //BufferedImage bufferedImage = ImageIO.read(file);
                    //Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    Image image = new Image(file.toURI().toString(), 205, 200, false, false);
                    albumimage.setImage(image);

                    try {
                        File remoteFile = new File(albumid.getText().toString() + ".png");
                        ImageIO.write(
                                SwingFXUtils.fromFXImage(
                                        image,
                                        null),
                                "png",
                                remoteFile);
                        try {
                            ftpUploader.uploadFile(remoteFile.getAbsolutePath(), "/" + remoteFile.getName(), "/img",remoteFile);
                            DatabaseHandler.setImageLink(Integer.parseInt(albumid.getText()));
                            uploaded.setImage(green);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    FileChooser chooser = new FileChooser();

                    //Set extension filter
                    FileChooser.ExtensionFilter jpg = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.jpg");
                    FileChooser.ExtensionFilter png = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
                    chooser.getExtensionFilters().addAll(jpg, png);
                   imageFile = chooser.showOpenDialog(null);
                    Image image = new Image(imageFile.toURI().toString(), 205, 200, false, false);
                    albumimage.setImage(image);
                    uploaded.setImage(red);
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

                songtable.getItems().clear();
            }
        });
        try {
            ftpUploader = new FTPUploader("139.99.2.52", "tamillyrics@beyonitysoftwares.cf", "mohanravi1990");
            //ftpUploader = new FTPUploader("mohanravi.space", "mohan", "ravi");
        } catch (Exception e) {

            e.printStackTrace();
        }

        //setTableCombo();
        //setSongTable();





    }


    private void initTable(){
        TableColumn<song,String> song_id = new TableColumn("Song Id");
        song_id.setCellValueFactory(new PropertyValueFactory<song,String>("song_id"));
        TableColumn<song,String> album_id = new TableColumn("Album Id");
        album_id.setCellValueFactory(new PropertyValueFactory<song,String>("album_id"));
        TableColumn<song,String> song_title = new TableColumn("Song Title");
        song_title.setCellValueFactory(new PropertyValueFactory<song,String>("song_title"));
        song_title.setCellFactory(TextFieldTableCell.<song> forTableColumn());
        song_title.setStyle("-fx-alignment: CENTER;");
        TableColumn<song,String> genre = new TableColumn("Genre");
        genre.setCellValueFactory(new PropertyValueFactory<song,String>("genre"));
        genre.setCellFactory(ComboBoxTableCell.forTableColumn(genrelistTable));
        genre.setStyle("-fx-alignment: CENTER;");
        TableColumn<song,String> lyricist = new TableColumn("Lyricist");
        lyricist.setCellFactory(ComboBoxTableCell.forTableColumn(lyricistlistTable));
        lyricist.setCellValueFactory(new PropertyValueFactory<song,String>("lyricist"));
        lyricist.setStyle("-fx-alignment: CENTER;");
        TableColumn<song,String> Lyrics = new TableColumn("Lyrics");
        Lyrics.setStyle("-fx-alignment: CENTER;");
        TableColumn<song,String> trackNo = new TableColumn("Track No");
        trackNo.setCellValueFactory(new PropertyValueFactory<song,String>("trackNo"));
        trackNo.setCellFactory(TextFieldTableCell.<song> forTableColumn());
        trackNo.setStyle("-fx-alignment: CENTER;");
        TableColumn<song,String> download_link = new TableColumn("Download Link");
        download_link.setCellValueFactory(new PropertyValueFactory<song,String>("download_link"));
        TableColumn upload = new TableColumn("Upload Song");


        song_title.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<song, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<song, String> event) {
                TablePosition<song, String> pos = event.getTablePosition();
                String songtitle = event.getNewValue();
                int row = pos.getRow();
                song i = event.getTableView().getItems().get(row);
                i.setSong_title(songtitle);
                //print(i);


                if(Integer.parseInt(i.getSong_id())>0){
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            DatabaseHandler.updateSongTitle(Integer.parseInt(i.getAlbum_id()), Integer.parseInt(i.getSong_id()), songtitle);
                            songtablelist.clear();
                            ObservableList<song> songlist = DatabaseHandler.getSongs(Integer.parseInt(i.getAlbum_id()));
                            for (song s : songlist) {
                                songtablelist.add(s);
                            }
                            setTableCombo();
                            presentSelectedAlbumId = Integer.parseInt(i.getAlbum_id());
                            setSongTable(Integer.parseInt(i.getAlbum_id()));
                        }});
                }else {

                    InserSong(i);
                }
            }
        });



        trackNo.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<song, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<song, String> event) {
                TablePosition<song, String> pos = event.getTablePosition();

                String TrackNo = event.getNewValue();
                int row = pos.getRow();
                song i = event.getTableView().getItems().get(row);
                i.setTrackNo(TrackNo);

                if(Integer.parseInt(i.getSong_id())>0){

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            DatabaseHandler.updateTrackNoInSong(Integer.parseInt(i.getAlbum_id()), Integer.parseInt(i.getSong_id()), Integer.parseInt(TrackNo));
                            songtablelist.clear();
                            ObservableList<song> songlist = DatabaseHandler.getSongs(Integer.parseInt(i.getAlbum_id()));
                            for (song s : songlist) {
                                songtablelist.add(s);
                            }
                            setTableCombo();
                            presentSelectedAlbumId = Integer.parseInt(i.getAlbum_id());
                            setSongTable(Integer.parseInt(i.getAlbum_id()));
                        }});
                }else {

                    InserSong(i);
                }
            }
        });


        lyricist.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<song, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<song, String> event) {
                TablePosition<song, String> pos = event.getTablePosition();

                String lyricistname = event.getNewValue();
                int row = pos.getRow();
                song i = event.getTableView().getItems().get(row);

                i.setLyricist(lyricistname);

                if(Integer.parseInt(i.getSong_id())>0){
                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            DatabaseHandler.updateLyricistInSong(Integer.parseInt(i.getAlbum_id()), Integer.parseInt(i.getSong_id()), lyricistname);
                            songtablelist.clear();
                            ObservableList<song> songlist = DatabaseHandler.getSongs(Integer.parseInt(i.getAlbum_id()));
                            for (song s : songlist) {
                                songtablelist.add(s);
                            }
                            setTableCombo();
                            presentSelectedAlbumId = Integer.parseInt(i.getAlbum_id());
                            setSongTable(Integer.parseInt(i.getAlbum_id()));

                        }});
                }else {

                   InserSong(i);
                }
            }
        });
        genre.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<song, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<song, String> event) {
                TablePosition<song, String> pos = event.getTablePosition();

                String genrename = event.getNewValue();
                int row = pos.getRow();
                song i = event.getTableView().getItems().get(row);

                i.setGenre(genrename);


                if(Integer.parseInt(i.getSong_id())>0){

                    Platform.runLater(new Runnable() {
                        @Override public void run() {
                            DatabaseHandler.updateGenreInSong(Integer.parseInt(i.getAlbum_id()),Integer.parseInt(i.getSong_id()),genrename);
                            songtablelist.clear();
                            ObservableList<song> songlist = DatabaseHandler.getSongs(Integer.parseInt(i.getAlbum_id()));
                            for(song s : songlist){
                                songtablelist.add(s);
                            }
                            setTableCombo();
                            presentSelectedAlbumId = Integer.parseInt(i.getAlbum_id());
                            setSongTable(Integer.parseInt(i.getAlbum_id()));
                        }});

                }else {
                    InserSong(i);
                }
            }
        });
        Callback<TableColumn<song, String>, TableCell<song, String>> cellFactory
                = //
                new Callback<TableColumn<song, String>, TableCell<song, String>>() {
                    @Override
                    public TableCell call(final TableColumn<song, String> param) {
                        final TableCell<song, String> cell = new TableCell<song, String>() {

                            final Button btn = new Button("Add/Edit Lyrics");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        try {
                                            System.out.println("row index for lyrics "+getTableRow().getIndex());
                                            HashMap<String,String> lyrics = new HashMap<>();
                                            song s = (song)getTableRow().getItem();
                                            int i = 0;
                                            if(s.getLyrics().getEnglish_one()!=null){
                                                lyrics.put("englishone",s.getLyrics().getEnglish_one());
                                            }else {
                                                lyrics.put("englishone","");
                                            }

                                            if(s.getLyrics().getEnglish_two()!=null){
                                                lyrics.put("englishtwo",s.getLyrics().getEnglish_two());
                                            }else {
                                                lyrics.put("englishtwo","");
                                            }

                                            if(s.getLyrics().getTamil_one()!=null){
                                                lyrics.put("tamilone",s.getLyrics().getTamil_one());
                                            }else {
                                                lyrics.put("tamilone","");
                                            }

                                            if(s.getLyrics().getTamil_two()!=null){
                                                lyrics.put("tamiltwo",s.getLyrics().getTamil_two());
                                            }else {
                                                lyrics.put("tamiltwo","");
                                            }

                                            lyrics.put("edited","no");


                                            ObserveLyrics.setLyrics(lyrics);
                                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lyrics.fxml"));

                                            Parent root1 = null;
                                            root1 = (Parent) fxmlLoader.load();
                                            Stage stage = new Stage();
                                            stage.initModality(Modality.APPLICATION_MODAL);
                                            stage.initStyle(StageStyle.DECORATED);
                                            stage.setTitle("Set Lyrics");
                                            stage.setScene(new Scene(root1));
                                            stage.setMaximized(true);

                                            stage.show();
                                            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                                public void handle(WindowEvent windowEvent) {


                                                    Platform.runLater(new Runnable() {
                                                        @Override public void run() {
                                                            HashMap<String, String> editedlyrics = ObserveLyrics.getLyrics();
                                                            if (editedlyrics.get("edited").equals("yes")) {
                                                                song s = (song) getTableRow().getItem();
                                                                s.getLyrics().setEnglish_one(editedlyrics.get("englishone"));
                                                                s.getLyrics().setEnglish_two(editedlyrics.get("englishtwo"));
                                                                s.getLyrics().setTamil_one(editedlyrics.get("tamilone"));
                                                                s.getLyrics().setTamil_two(editedlyrics.get("tamiltwo"));

                                                                if (Integer.parseInt(s.getSong_id()) > 0) {

                                                                    DatabaseHandler.updateLyrics(Integer.parseInt(s.getAlbum_id()), Integer.parseInt(s.getSong_id()), s.getLyrics());
                                                                    songtablelist.clear();
                                                                    ObservableList<song> songlist = DatabaseHandler.getSongs(Integer.parseInt(s.getAlbum_id()));
                                                                    for (song i : songlist) {
                                                                        songtablelist.add(i);
                                                                    }
                                                                    setTableCombo();
                                                                    presentSelectedAlbumId = Integer.parseInt(s.getAlbum_id());
                                                                    setSongTable(Integer.parseInt(s.getAlbum_id()));


                                                                } else {

                                                                    InserSong(s);
                                                                }
                                                            }
                                                        }});



                                                }
                                            });
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };


        Callback<TableColumn<song, String>, TableCell<song, String>> selectSong
                = //
                new Callback<TableColumn<song, String>, TableCell<song, String>>() {
                    @Override
                    public TableCell call(final TableColumn<song, String> param) {
                        final TableCell<song, String> cell = new TableCell<song, String>() {

                            final Button btn = new Button("Select Song to upload");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction((ActionEvent event) -> {
                                        song s = (song) getTableRow().getItem();
                                        if(Integer.parseInt(s.getSong_id())!=0){
                                            FileChooser fileChooser = new FileChooser();

                                            //Set extension filter
                                            FileChooser.ExtensionFilter extFilterOGG = new FileChooser.ExtensionFilter("OGG files (*.ogg)", "*.ogg");
                                            fileChooser.getExtensionFilters().addAll(extFilterOGG);

                                            //Show open file dialog
                                            File file = fileChooser.showOpenDialog(null);
                                            File remoteFile = new File(s.getSong_id()+".ogg");
                                            Thread t = new Thread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if(file != null) {
                                                        s.setLocalSongPath(file.getAbsolutePath());
                                                        System.out.println(file.getAbsolutePath());

                                                        try {
                                                            if (ftpUploader.checkDirectoryExists(s.getAlbum_id())) {
                                                                System.out.println("album folder present");
                                                            } else {
                                                                ftpUploader.mkdir(s.getAlbum_id());
                                                            }
                                                                ftpUploader.uploadFile(s.getLocalSongPath(),"/"+remoteFile.getName(),"/"+s.getAlbum_id(),new File(s.getLocalSongPath()));
                                                                Timer t = new Timer();
                                                                TimerTask timerTasl = new TimerTask() {
                                                                    @Override
                                                                    public void run() {
                                                                        int progress = (int) FTPUploader.updateProgress.update();
                                                                        status.setProgress(progress);
                                                                        if(progress==100){
                                                                            t.cancel();
                                                                        }

                                                                    }
                                                                };

                                                                t.schedule(timerTasl,0,1000);
                                                                DatabaseHandler.setDownloadLink(Integer.parseInt(s.getAlbum_id()),Integer.parseInt(s.getSong_id()));

                                                                song a = DatabaseHandler.getSong(s.getSong_title());
                                                                albumset = true;

                                                                songtablelist.clear();
                                                                ObservableList<song> songlist = DatabaseHandler.getSongs(Integer.parseInt(s.getAlbum_id()));
                                                                for(song s : songlist){
                                                                    songtablelist.add(s);
                                                                }
                                                                setTableCombo();
                                                                presentSelectedAlbumId = Integer.parseInt(s.getAlbum_id());
                                                                setSongTable(Integer.parseInt(s.getAlbum_id()));






                                                        } catch (IOException e) {
                                                            e.printStackTrace();
                                                        } catch (Exception e) {
                                                            e.printStackTrace();
                                                        }
                                                    }else {
                                                        System.out.println("no file selected");
                                                    }
                                                }
                                            });
                                            t.start();


                                        }else {
                                            Alert a = new Alert(Alert.AlertType.ERROR);
                                            a.setTitle("Song id not present");
                                            a.setContentText("Please upload song details and lyrics before adding actual ogg file");
                                            a.showAndWait();
                                        }





                                        //BufferedImage bufferedImage = ImageIO.read(file);
                                        //Image image = SwingFXUtils.toFXImage(bufferedImage, null);


                                           /* try {
                                                ftpUploader.uploadFile(file.getAbsolutePath(),"/"+file.getName(),"/img");
                                                DatabaseHandler.setImageLink(Integer.parseInt(albumid.getText()));
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }*/


                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };

        upload.setCellFactory(selectSong);
        Lyrics.setCellFactory(cellFactory);

        songtable.getColumns().addAll(song_id,album_id,song_title,genre,lyricist,Lyrics,trackNo,download_link,upload);
        songtable.setColumnResizePolicy(javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY);
        songtable.setItems(songtablelist);
        songtable.setEditable(true);

        ContextMenu menu = new ContextMenu();
        MenuItem add = new MenuItem("Add Song Row");
        add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //addNewRow();

                song s = new song();
                s.setDefaultToAll();
                s.setAlbum_id(String.valueOf(presentSelectedAlbumId));
                songtablelist.add(s);
                songtable.refresh();
            }
        });
        MenuItem delete = new MenuItem("Delete This song from database");
        delete.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                songtablelist.remove(songtable.getSelectionModel().getSelectedIndex());
               songtable.refresh();
            }
        });


        menu.getItems().addAll(add,delete);

        songtable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent t) {
                if(t.getButton() == MouseButton.SECONDARY) {
                    if(!songtable.getItems().isEmpty()) {
                        if(songtable.getItems().size()<2){
                            delete.setDisable(true);
                            menu.show(songtable, t.getScreenX(), t.getScreenY());
                            System.out.println("Table row  selected "+((TableView<song>)t.getSource()).getSelectionModel().getSelectedIndex());
                        }else {
                            if(delete.isDisable()){
                                delete.setDisable(false);
                            }
                            menu.show(songtable, t.getScreenX(), t.getScreenY());
                            System.out.println("Table row  selected "+((TableView<song>)t.getSource()).getSelectionModel().getSelectedIndex());
                        }

                    }
                }else if(t.getButton() == MouseButton.PRIMARY){
                    if(menu.isShowing()){
                        menu.hide();
                    }
                }
            }
        });
       /* songtable.setRowFactory(new Callback<TableView<song>, TableRow<song>>() {
            @Override
            public TableRow<song> call(TableView<song> param) {

                final TableRow<song> row = new TableRow<>();
                ContextMenu menu = new ContextMenu();
                MenuItem add = new MenuItem("Add Item");
                add.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        //addNewRow();

                        song s = new song();
                        s.setDefaultToAll();
                        s.setAlbum_id(String.valueOf(presentSelectedAlbumId));
                        songtablelist.add(s);
                    }
                });
                MenuItem delete = new MenuItem("delete Item");
                delete.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        songtablelist.remove(row.getIndex());
                        songtable.refresh();
                    }
                });
                menu.getItems().addAll(add,delete);
                if (songtablelist.size()<2){
                    System.out.println("song table length :"+songtablelist.size());
                    delete.setDisable(true);
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu)null)
                                    .otherwise(menu)
                    );
                }else {
                    System.out.println("called this full");
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                                    .then((ContextMenu)null)
                                    .otherwise(menu)
                    );
                }

                return row;
            }
        });*/


    }


    private void setSongTable(int a_id) {

        if(songtablelist.isEmpty()){
            song s = new song();
            s.setDefaultToAll();
            s.setAlbum_id(String.valueOf(a_id));
            songtablelist.add(s);
        }





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


        lyricistcombo.getSelectionModel().selectLast();
        genrecombo.getSelectionModel().selectLast();

        comboartist.getSelectionModel().selectLast();
        combohero.getSelectionModel().selectLast();
        comboheroin.getSelectionModel().selectLast();

    }
    public void setTableCombo(){
        genrelistTable.clear();
        lyricistlistTable.clear();
        for(Object s : observablevalues.getGenrelist()){
            genrelistTable.add(String.valueOf(s));
        }
        for(Object s : observablevalues.getLyricistlist()){
            lyricistlistTable.add(String.valueOf(s));
        }

        lyricistlistTable.remove("Select Lyricist (Default All)");
        genrelistTable.remove("Select Genre (Default All)");
        genrelistTable.add("none");
        lyricistlistTable.add("none");



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




    public void print(song s){
        System.out.print(s.getAlbum_id()+" ");
        System.out.print(s.getSong_id()+" ");
        System.out.print(s.getSong_title()+" ");
        System.out.print(s.getGenre()+" ");
        System.out.print(s.getLyricist()+" ");
        System.out.print(s.getLyrics().getEnglish_one().length()+" ");
        System.out.print(s.getLyrics().getEnglish_two().length()+" ");
        System.out.print(s.getLyrics().getTamil_one().length()+" ");
        System.out.print(s.getLyrics().getTamil_two().length()+" ");

        System.out.print(s.getTrackNo());
        System.out.print(s.getLocalSongPath());
        System.out.println();



    }

    public void InserSong(song i){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                String english_one = i.getLyrics().getEnglish_one();
                String english_two = i.getLyrics().getEnglish_two();
                String tamil_one = i.getLyrics().getTamil_one();
                String tamil_two = i.getLyrics().getTamil_two();
                if((i.getSong_title().length()>0)&&(i.getLyricist() !="none")&&(i.getGenre()!="none")&&(english_one!="")&&(english_two!="")&&(tamil_one!="")&&(tamil_two!="")&&(i.getTrackNo()!="0")){
                    print(i);
                    reponse = DatabaseHandler.insertSong(i);
                    if(!(boolean)reponse.get("error")){
                        System.out.println("song id = "+reponse.get("song_id"));
                        i.setSong_id(String.valueOf(reponse.get("song_id")));
                        status.setText("successfully added song details...");
                        song a = DatabaseHandler.getSong(i.getSong_title());
                        albumset = true;

                        songtablelist.clear();
                        ObservableList<song> songlist = DatabaseHandler.getSongs(Integer.parseInt(i.getAlbum_id()));
                        for(song s : songlist){
                            songtablelist.add(s);
                        }
                        setTableCombo();
                        presentSelectedAlbumId = Integer.parseInt(i.getAlbum_id());
                        setSongTable(Integer.parseInt(i.getAlbum_id()));
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }else {
                    status.setText(String.valueOf(reponse.get("message")));
                    System.out.println("not passed");
                    print(i);
                }
            }
        });

        }


        private void setSelectedAlbum(album a){
            if(a!=null){
                System.out.print(a.getAlbum_id());
                System.out.print(a.getAlbum_name());
                System.out.print(a.getArtist_id());
                System.out.print(a.getHero_id());
                System.out.print(a.getHeroin_id());
                System.out.print(a.getYear());
                System.out.print(a.getImage_link());

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
                        if(a.getImage_link()!=null) {
                            Image i = new Image(a.getImage_link());
                            Timer it = new Timer("Timer");
                            TimerTask ts = new TimerTask() {
                                @Override
                                public void run() {
                                    status.setProgress(i.getProgress());
                                }
                            };

                            it.scheduleAtFixedRate(ts, 1000, 10);
                            albumimage.setImage(i);
                            uploaded.setImage(green);
                        }else {
                            uploaded.setImage(red);
                        }
                    }
                };
                Thread t = new Thread(r);
                t.start();
                year.setText(String.valueOf(a.getYear()));
                albumset = true;

                songtablelist.clear();
                ObservableList<song> songlist = DatabaseHandler.getSongs(a.getAlbum_id());
                for(song s : songlist){
                    songtablelist.add(s);
                }
                setTableCombo();
                presentSelectedAlbumId = a.getAlbum_id();
                setSongTable(a.getAlbum_id());


            }
        }

}


