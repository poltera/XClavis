/*
 * Copyright (c) 2015, Gian Poltéra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1.	Redistributions of source code must retain the above copyright notice,
 *   	this list of conditions and the following disclaimer.
 * 2.	Redistributions in binary form must reproduce the above copyright 
 *   	notice, this list of conditions and the following disclaimer in the 
 *   	documentation and/or other materials provided with the distribution.
 * 3.	Neither the name of HSR University of Applied Sciences Rapperswil nor 
 * 	the names of its contributors may be used to endorse or promote products
 * 	derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.files.FileCrypter;
import ch.hsr.xclavis.helpers.Logfile;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.ui.MainApp;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class Shows the state of a encryption or a decryption.
 *
 * @author Gian Poltéra
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
    @FXML
    private VBox cryptionState;
    @FXML
    private Button btnShowLog;

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

    /**
     * Sets the parameters for the cryption.
     *
     * @param sessionKey the SessionKey for the cryption
     * @param encryption true for encryption or false for decryption
     * @param output the output path for the files
     */
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
    Logfile logfile = new Logfile();
    @FXML
    private void showLog(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("XClavis");
        alert.setHeaderText("Logfile der ent/verschlüsselung");
        alert.setContentText(logfile.getLogfile());
        logfile.addLogEntry("hmm");
        
        alert.showAndWait();
    }
}
