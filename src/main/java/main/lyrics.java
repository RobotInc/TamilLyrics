package main;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import main.untility.ObserveLyrics;


public class lyrics {

    @FXML
    TextArea englishone,tamilone,englishtwo,tamiltwo;

    @FXML
    private void initialize() {

        englishone.setText(ObserveLyrics.getLyrics().get("englishone"));
        englishtwo.setText(ObserveLyrics.getLyrics().get("englishtwo"));
        tamilone.setText(ObserveLyrics.getLyrics().get("tamilone"));
        tamiltwo.setText(ObserveLyrics.getLyrics().get("tamiltwo"));

        englishone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObserveLyrics.getLyrics().put("englishone",newValue);
                ObserveLyrics.getLyrics().put("edited","yes");
            }
        });

        tamilone.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObserveLyrics.getLyrics().put("tamilone",newValue);
                ObserveLyrics.getLyrics().put("edited","yes");
            }
        });

        englishtwo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObserveLyrics.getLyrics().put("englishtwo",newValue);
                ObserveLyrics.getLyrics().put("edited","yes");
            }
        });

        tamiltwo.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObserveLyrics.getLyrics().put("tamiltwo",newValue);
                ObserveLyrics.getLyrics().put("edited","yes");
            }
        });
    }

}