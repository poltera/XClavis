/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.commons.InputBlock;
import ch.hsr.xclavis.commons.InputBlocks;
import ch.hsr.xclavis.commons.SessionKey;
import ch.hsr.xclavis.ui.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class CodeReaderController implements Initializable {

    private String pattern = "[23456789abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ]";

    private MainApp mainApp;
    private final int blockLength = 5;
    private final int blockChecksumSize = 1;

    @FXML
    private HBox hbInputSelecter;
    @FXML
    private HBox hbInputBlocks1;
    @FXML
    private HBox hbInputBlocks2;
    @FXML
    private HBox hbInputBlocks3;
    @FXML
    private HBox hbInputBlocks4;
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
        InputBlock inputSelecter = new InputBlock(blockLength, blockChecksumSize, pattern);
        inputSelecter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (inputSelecter.isValid()) {
                String type = newValue.substring(0, 1);
                addBlocks(type);
            }
        });
        hbInputSelecter.getChildren().add(inputSelecter);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    private void addBlocks(String type) {
        int blocksSize = 7;
        int overallChecksumSize = 2;
        if (type.equals(SessionKey.SESSION_KEY_128)) {
            blocksSize = 7;
            overallChecksumSize = 2;
        } else if (type.equals(SessionKey.SESSION_KEY_256)) {
            blocksSize = 14;
            overallChecksumSize = 4;
        } else if ((type.equals(SessionKey.ECDH_REQ_128)) || (type.equals(SessionKey.ECDH_RES_128))) {
            blocksSize = 14;
            overallChecksumSize = 3;
        } else if ((type.equals(SessionKey.ECDH_REQ_256)) || (type.equals(SessionKey.ECDH_RES_256))) {
            blocksSize = 27;
            overallChecksumSize = 4;
        }

        InputBlocks inputBlocks = new InputBlocks(blocksSize, overallChecksumSize);

        for (int i = 0; i < blocksSize; i++) {
            InputBlock inputBlock = new InputBlock(blockLength, blockChecksumSize, pattern);
            inputBlock.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                inputBlocks.areValid();
            });
            inputBlocks.addBlock(inputBlock);
            if (i < 7) {
                hbInputBlocks1.getChildren().add(inputBlock);
                hbInputBlocks1.setVisible(true);
            } else if (i < 14) {
                hbInputBlocks2.getChildren().add(inputBlock);
                hbInputBlocks2.setVisible(true);
            } else if (i < 21) {
                hbInputBlocks3.getChildren().add(inputBlock);
                hbInputBlocks3.setVisible(true);
            } else if (i < 28) {
                hbInputBlocks4.getChildren().add(inputBlock);
                hbInputBlocks4.setVisible(true);
            }
        }
    }
}
