package main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import main.database.DatabaseHandler;
import main.untility.observablevalues;
import org.controlsfx.control.StatusBar;

import java.io.IOException;
import java.util.HashMap;

public class Controller {


    ObservableList createcomboartistlist = FXCollections.observableArrayList();
    ObservableList createcomboherolist = FXCollections.observableArrayList();
    ObservableList createcomboheroinlist = FXCollections.observableArrayList();


    @FXML
    ComboBox artistcombo,herocombo,lyricistcombo,heroincombo,genrecombo,comboartist,combohero,comboheroin;
    @FXML
    StatusBar status;
    @FXML
    GridPane contentgrid;
    HashMap<String,Object> reponse = new HashMap<String, Object>();

    @FXML
    Button add = new Button();

    @FXML
    private void initialize(){
       reponse = DatabaseHandler.connect();



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

            setStatus("Connected");
            DatabaseHandler.setListsFromDatabase();
            setAll();

        }else {
            status.setText("Status: Error Connecting");
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

}
