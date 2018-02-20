package main;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import main.database.DatabaseHandler;
import main.untility.Helper;
import org.controlsfx.control.StatusBar;

import java.util.HashMap;

public class Add {

    @FXML
    StatusBar status;

    HashMap<String,Object> response = new HashMap<String, Object>();
    @FXML
    Button addartist,addlyricist,addhero,addheroin,addgenre;
    @FXML
    TextField artisttext,lyricisttext,herotext,herointext,genretext;
    @FXML
    private void initialize(){

        addartist.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if(artisttext.getText().length()>0){
                    response = DatabaseHandler.addArtist((Helper.FirstLetterCaps(artisttext.getText())));
                    if(!Boolean.valueOf(String.valueOf(response.get("error")))){
                        status.setText((String) response.get("message"));
                        DatabaseHandler.setListsFromDatabase();
                        artisttext.clear();
                    }else {
                        status.setText((String) response.get("message"));
                    }

                }else {
                    status.setText("artist field cannot be empty");
                }
            }
        });

        addhero.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if(herotext.getText().length()>0){
                    response = DatabaseHandler.addHero((Helper.FirstLetterCaps(herotext.getText())));
                    if(!Boolean.valueOf(String.valueOf(response.get("error")))){
                        status.setText((String) response.get("message"));
                        DatabaseHandler.setListsFromDatabase();
                        herotext.clear();
                    }else {
                        status.setText((String) response.get("message"));
                    }

                }else {
                    status.setText("Hero field cannot be empty");
                }
            }
        });
        addheroin.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if(herointext.getText().length()>0){
                    response = DatabaseHandler.addHeroin((Helper.FirstLetterCaps(herointext.getText())));
                    if(!Boolean.valueOf(String.valueOf(response.get("error")))){
                        status.setText((String) response.get("message"));
                        DatabaseHandler.setListsFromDatabase();
                        herointext.clear();
                    }else {
                        status.setText((String) response.get("message"));
                    }

                }else {
                    status.setText("Heroin field cannot be empty");
                }
            }
        });
        addlyricist.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if(lyricisttext.getText().length()>0){
                    response = DatabaseHandler.addLyricist((Helper.FirstLetterCaps(lyricisttext.getText())));
                    if(!Boolean.valueOf(String.valueOf(response.get("error")))){
                        status.setText((String) response.get("message"));
                        DatabaseHandler.setListsFromDatabase();
                        lyricisttext.clear();
                    }else {
                        status.setText((String) response.get("message"));
                    }

                }else {
                    status.setText("Lyricist field cannot be empty");
                }
            }
        });

        addgenre.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent actionEvent) {
                if(genretext.getText().length()>0){
                    response = DatabaseHandler.addGenre((Helper.FirstLetterCaps(genretext.getText())));
                    if(!Boolean.valueOf(String.valueOf(response.get("error")))){
                        status.setText((String) response.get("message"));
                        DatabaseHandler.setListsFromDatabase();
                        genretext.clear();
                    }else {
                        status.setText((String) response.get("message"));
                    }

                }else {
                    status.setText("Genre field cannot be empty");
                }
            }
        });
    }

}
