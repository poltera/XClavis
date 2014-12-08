/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.crypto.Checksum;
import ch.hsr.xclavis.ui.InputBlock;
import ch.hsr.xclavis.ui.InputBlocks;
import ch.hsr.xclavis.ui.MainApp;
import java.awt.Toolkit;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
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
    private final int blocksSize = 7;
    private final int blocksChecksumSize = 2;
    private InputBlocks inputBlocks = new InputBlocks(blocksSize, blocksChecksumSize);

    @FXML
    private Button btnDecrypt;
    @FXML
    private HBox hbInputBlocks;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        for (int i = 0; i < blocksSize; i++) {
            InputBlock inputBlock = new InputBlock(blockLength, blockChecksumSize, pattern);
            inputBlock.setAlignment(Pos.CENTER);
            inputBlock.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                inputBlocks.areValid();
            });
            inputBlocks.addBlock(inputBlock);
            hbInputBlocks.getChildren().add(inputBlock);
        }
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
