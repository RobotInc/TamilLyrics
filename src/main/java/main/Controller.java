package main;

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
import javafx.stage.*;
import javafx.util.Callback;
import javafx.util.converter.DateTimeStringConverter;
import javafx.util.converter.NumberStringConverter;
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
import java.util.regex.Pattern;

import static javafx.scene.control.cell.TextFieldTableCell.forTableColumn;

public class Controller {

    @FXML
    TableView<song> songtable;

    boolean albumset = false;
    private PrintStream ps ;
    @FXML
    ImageView albumimage;
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

                songtable.getItems().clear();
            }
        });
        try {
            ftpUploader = new FTPUploader("139.99.2.52", "tamillyrics@beyonitysoftwares.cf", "mohanravi1990");
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
                print(i);
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
                print(i);
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

                print(i);
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

                print(i);
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

                                            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/lyrics.fxml"));

                                            Parent root1 = null;
                                            root1 = (Parent) fxmlLoader.load();
                                            Stage stage = new Stage();
                                            stage.initModality(Modality.APPLICATION_MODAL);
                                            stage.initStyle(StageStyle.DECORATED);
                                            stage.setTitle("Set Lyrics");
                                            stage.setScene(new Scene(root1));
                                            stage.setMaximized(false);
                                            stage.setResizable(false);
                                            stage.show();
                                            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                                                public void handle(WindowEvent windowEvent) {
                                                    print((song) getTableRow().getItem());
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

        Lyrics.setCellFactory(cellFactory);

        songtable.getColumns().addAll(song_id,album_id,song_title,genre,lyricist,Lyrics,trackNo,download_link,upload);
        songtable.setColumnResizePolicy(javafx.scene.control.TableView.CONSTRAINED_RESIZE_POLICY);
        songtable.setItems(songtablelist);
        songtable.setEditable(true);

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
                songtable.refresh();
            }
        });
        MenuItem delete = new MenuItem("delete Item");
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
        herocombo.getSelectionModel().selectLast();
        heroincombo.getSelectionModel().selectLast();
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
        genrelistTable = observablevalues.getGenrelist();
        lyricistlistTable = observablevalues.getLyricistlist();
        genrelistTable.add("None");
        lyricistlistTable.add("None");


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
        System.out.print(s.getTrackNo());
        System.out.println();



    }
}


