/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.helpers.QRModel;
import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.keys.Key;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.qrcode.QRCodeGenerator;
import ch.hsr.xclavis.ui.MainApp;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class CodeOutputController implements Initializable {

    private MainApp mainApp;

    @FXML
    private ImageView imageView;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
    
    public void setKeys(List<Key> keys) {
        QRModel qrModell = new QRModel();
        
        keys.stream().forEach((key) -> {
            if (key.getSessionID().isSessionKey()) {
                qrModell.addSessionKey((SessionKey) key);
            } else if (key.getSessionID().isECDH()) {
                qrModell.addECDHKey((ECDHKey) key);
            }
        });
        
        imageView.setImage(new QRCodeGenerator().createQR(qrModell.getModell(), 500));
    }
}
