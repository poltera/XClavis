/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.ui.InputBlock;
import ch.hsr.xclavis.ui.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.InputEvent;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class CodeReaderController implements Initializable {

    private MainApp mainApp;
    private int maxLength = 5;

    @FXML
    private HBox inputBlocks;
    @FXML
    private Button btnDecrypt;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {        
        for (int i = 0; i < 7; i++) {
            InputBlock inputBlock = new InputBlock();
            inputBlock.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (newValue.length() == maxLength) {
                    inputBlock.setDisable(true);
                }
            });
            inputBlocks.getChildren().add(inputBlock);
        }
//        textField.lengthProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
//            System.out.println(newValue);
//        });
//    }

//        textField.addEventFilter(InputEvent.ANY, (Event event) -> {
//            if (event.getEventType().equals(KeyEvent.KEY_TYPED)) {
//                if (textField.getLength() <= 6) {
//                    event.
//                }
//            } else {
//                //event.consume();
//            }
//        });
//        textField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
//            //Check the maximal allowed characters per textfield
//            String allowedCharacters = "23456789ABCDEFGHJKLMNPQRSTUVWXYZ";
//            newValue = newValue.toUpperCase();
//            if (newValue.length() <= 5 && !newValue.contains(allowedCharacters)) {
//                //Convert the lower case characters to upper case characters
//                //newValue = newValue.toUpperCase();
//                textField.setText(newValue);
//                
//            } else {
//                return;
//            }
//        });
//        textField = new TextField() {
//            @Override
//            public void replaceText(int start, int end, String text) {
//        // If the replaced text would end up being invalid, then simply
//                // ignore this call!
//                if (!text.matches("[a-z]")) {
//                    super.replaceText(start, end, text);
//                }
//            }
//
//            @Override
//            public void replaceSelection(String text) {
//                if (!text.matches("[a-z]")) {
//                    super.replaceSelection(text);
//                }
//            }
//        };
    }

    //textField.addEventFilter(filter);

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
