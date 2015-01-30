/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.files.FileCrypter;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.ui.MainApp;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class CryptionStateController implements Initializable {

    private MainApp mainApp;
    private ResourceBundle rb;
    private FileCrypter crypter;

    @FXML
    private ProgressIndicator progressIndicator;
    @FXML
    private Label lblCryptionState;
    @FXML
    private ImageView imgRemoveCryptionState;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        crypter = new FileCrypter();
        imgRemoveCryptionState.setOnMouseEntered((event) -> {
            imgRemoveCryptionState.setImage(new Image(getClass().getResourceAsStream("/images/delete2.png")));
        });
        imgRemoveCryptionState.setOnMouseExited((event) -> {
            imgRemoveCryptionState.setImage(new Image(getClass().getResourceAsStream("/images/delete1.png")));
        });
        imgRemoveCryptionState.setOnMouseClicked((event) -> {
            mainApp.removeCryptionState();
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

    public void setParameters(SessionKey sessionKey, boolean encryption, String output) {
        if (encryption) {
            lblCryptionState.setText(rb.getString("encryption_state"));
            List<File> files = new ArrayList<>();
            mainApp.getFiles().getObservableFileList().forEach((selectedFile) -> {
                files.add(selectedFile.getFile());
            });
            progressIndicator.progressProperty().bind(crypter.encrypt(sessionKey, files, output));
            mainApp.getFiles().removeAll();
        } else {
            
            File file = new File(mainApp.getFiles().getObservableFileList().get(0).getFile().getPath());
            lblCryptionState.setText(rb.getString("decryption_state"));
            progressIndicator.progressProperty().bind(crypter.decrypt(sessionKey, file, output));
            mainApp.getFiles().removeAll();
        }
    }
}
