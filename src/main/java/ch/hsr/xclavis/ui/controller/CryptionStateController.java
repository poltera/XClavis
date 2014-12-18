/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.webcam.DetectedWebcam;
import ch.hsr.xclavis.crypter.Crypter;
import ch.hsr.xclavis.ui.MainApp;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class CryptionStateController implements Initializable {

    private MainApp mainApp;
    private ResourceBundle rb;
    private Crypter crypter;
    private String output;
    private boolean encryption;
    
    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label lblCryptionState;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        crypter = new Crypter();
        Platform.runLater(() -> {
            if (encryption) {
                lblCryptionState.setText(rb.getString("encryption_state"));
                progressIndicator.progressProperty().bind(crypter.encrypt(mainApp.getFiles().getObservableFileList(), output));
            } else {

            }         
        });
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void setOutputPath(String output) {
        this.output = output;
    }

    public void setEncryption(boolean encryption) {
        this.encryption = encryption;
    }
}
