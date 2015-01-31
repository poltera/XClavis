/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.commons.InputBlock;
import ch.hsr.xclavis.commons.InputBlocks;
import ch.hsr.xclavis.keys.Key;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.qrcode.QRModel;
import ch.hsr.xclavis.webcam.DetectedWebcam;
import ch.hsr.xclavis.ui.MainApp;
import ch.hsr.xclavis.webcam.WebcamHandler;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class CodeReaderController implements Initializable {

    private final static String PATTERN = "[23456789abcdefghjklmnpqrstuvwxyzABCDEFGHJKLMNPQRSTUVWXYZ]";

    private MainApp mainApp;
    private WebcamHandler webcamHandler;
    private final int blockLength = 5;
    private final int blockChecksumSize = 1;
    private SessionID manualSessionID;
    private List<Key> ecdhResponseKeys;
    private boolean webcamStarted;

    @FXML
    private VBox vbWebcamInput;
    @FXML
    private HBox hbWebcamSelecter;
    @FXML
    private HBox hbWebcamImage;
    @FXML
    private Label lblWebcamLoad;
    @FXML
    private VBox vbManualInput;
    @FXML
    private HBox hbInputSelecter;
    @FXML
    private VBox vbKeyInput;
    @FXML
    private HBox hbInputBlocks1;
    @FXML
    private HBox hbInputBlocks2;
    @FXML
    private HBox hbInputBlocks3;
    @FXML
    private HBox hbInputBlocks4;
    @FXML
    private ComboBox<DetectedWebcam> cbWebcamSelecter;
    @FXML
    private Label lblNoWebcam;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        webcamHandler = new WebcamHandler();
        ecdhResponseKeys = new ArrayList<>();
        webcamStarted = false;

        InputBlock inputSelecter = new InputBlock(blockLength, blockChecksumSize, PATTERN);
        inputSelecter.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (inputSelecter.isValid()) {
                String type = inputSelecter.getValue().substring(0, 1);
                String random = inputSelecter.getValue().substring(1);
                manualSessionID = new SessionID(type, random);
                addBlocks(type);
            }
        });
        hbInputSelecter.getChildren().add(inputSelecter);
        vbKeyInput.getChildren().removeAll(hbInputBlocks1, hbInputBlocks2, hbInputBlocks3, hbInputBlocks4);
        vbManualInput.getChildren().remove(vbKeyInput);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void startWebcam() {
        ImageView imageViewWebcam = new ImageView();
        imageViewWebcam.setFitWidth(480);
        imageViewWebcam.setPreserveRatio(true);

        // Show WebcamSelecter if more then one Webcam
        if (webcamHandler.getWebcamCount() > 1) {
            cbWebcamSelecter.setItems(webcamHandler.getWebcams());
            cbWebcamSelecter.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends DetectedWebcam> observable, DetectedWebcam oldValue, DetectedWebcam newValue) -> {
                if (newValue != null) {
                    webcamHandler.initWebcam(newValue.getWebcamIndex());
                    imageViewWebcam.imageProperty().bind(webcamHandler.getStream());
                }
            });
        } else {
            vbWebcamInput.getChildren().remove(hbWebcamSelecter);
        }

        // Show WebcamImage from first Webcam if one Webcam exists
        if (!webcamStarted) {
            if (webcamHandler.existsWebcam()) {
                // Start the Webcam
                webcamStarted = true;
                webcamHandler.initWebcam(0);
                imageViewWebcam.imageProperty().bind(webcamHandler.getStream());
                hbWebcamImage.getChildren().remove(lblWebcamLoad);
                vbWebcamInput.getChildren().remove(lblNoWebcam);
                hbWebcamImage.getChildren().add(imageViewWebcam);

            } else {
                vbWebcamInput.getChildren().remove(hbWebcamImage);
            }

            // Listener for QRCode
            webcamHandler.getScanedQRCode().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                String[][] keys = new QRModel().getKeys(newValue);
                if (keys.length > 0) {
                    ecdhResponseKeys.clear();
                    for (int i = 0; i < keys.length; i++) {
                        SessionID sessionID = new SessionID(keys[i][0].substring(0, 1), keys[i][0].substring(1));
                        String key = keys[i][1];
                        addKeyToKeyStore(sessionID, key);
                    }
                    if (ecdhResponseKeys.size() > 0) {
                        mainApp.showCodeOutput(ecdhResponseKeys);
                    } else {
                        mainApp.showKeyManagement();
                    }
                    // Stop the Webcam
                    imageViewWebcam.imageProperty().unbind();
                    hbWebcamImage.getChildren().remove(imageViewWebcam);
                    webcamHandler.stopWebcam();
                    webcamStarted = false;
                }
            });
        }
    }

    private void addBlocks(String type) {
        int blocksSize = 7;
        int overallChecksumSize = 2;
        switch (type) {
            case SessionID.SESSION_KEY_128:
                blocksSize = 7;
                overallChecksumSize = 2;
                vbKeyInput.getChildren().add(hbInputBlocks1);
                break;
            case SessionID.SESSION_KEY_256:
                blocksSize = 14;
                overallChecksumSize = 4;
                vbKeyInput.getChildren().add(hbInputBlocks1);
                vbKeyInput.getChildren().add(hbInputBlocks2);
                break;
            case SessionID.ECDH_REQ_256:
            case SessionID.ECDH_RES_256:
                blocksSize = 14;
                overallChecksumSize = 3;
                vbKeyInput.getChildren().add(hbInputBlocks1);
                vbKeyInput.getChildren().add(hbInputBlocks2);
                break;
            case SessionID.ECDH_REQ_512:
            case SessionID.ECDH_RES_512:
                blocksSize = 27;
                overallChecksumSize = 4;
                vbKeyInput.getChildren().add(hbInputBlocks1);
                vbKeyInput.getChildren().add(hbInputBlocks2);
                vbKeyInput.getChildren().add(hbInputBlocks3);
                vbKeyInput.getChildren().add(hbInputBlocks4);
                break;
        }

        InputBlocks inputBlocks = new InputBlocks(blocksSize, overallChecksumSize);

        for (int i = 0; i < blocksSize; i++) {
            InputBlock inputBlock = new InputBlock(blockLength, blockChecksumSize, PATTERN);
            inputBlock.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
                if (inputBlocks.areValid()) {
                    ecdhResponseKeys.clear();
                    addKeyToKeyStore(manualSessionID, newValue);
                    if (ecdhResponseKeys.size() > 0) {
                        mainApp.showCodeOutput(ecdhResponseKeys);
                    } else {
                        mainApp.showKeyManagement();
                    }
                }
            });
            inputBlocks.addBlock(inputBlock);
            if (i < 7) {
                hbInputBlocks1.getChildren().add(inputBlock);
            } else if (i < 14) {
                hbInputBlocks2.getChildren().add(inputBlock);
            } else if (i < 21) {
                hbInputBlocks3.getChildren().add(inputBlock);
            } else if (i < 28) {
                hbInputBlocks4.getChildren().add(inputBlock);
            }
        }
        vbManualInput.getChildren().add(vbKeyInput);
    }

    private void addKeyToKeyStore(SessionID sessionID, String key) {
        if (sessionID.isSessionKey()) {
            SessionKey sessionKey = new SessionKey(sessionID, key);
            sessionKey.setPartner("Remote");
            sessionKey.setState("3");
            mainApp.getKeys().add(sessionKey);

        } else if (sessionID.isECDHReq()) {
            // Calculating the ECDH response and the SessionKey
            ECDHKey ecdhKey = new ECDHKey(sessionID);
            //mainApp.getKeys().add(ecdhKey);
            SessionKey sessionKey = ecdhKey.getSessionKey(key);
            sessionKey.setPartner("Remote");
            sessionKey.setState("3");
            mainApp.getKeys().add(sessionKey);
            mainApp.getKeys().remove(ecdhKey);
            ecdhResponseKeys.add(ecdhKey);

        } else if (sessionID.isECDHRes()) {
            // Load the ECDHKey from the KeyStore
            ECDHKey ecdhKey = mainApp.getKeys().getECDHKey(sessionID.getRandom());
            SessionKey sessionKey = ecdhKey.getSessionKey(key);
            sessionKey.setPartner(ecdhKey.getPartner());
            sessionKey.setState("0");
            mainApp.getKeys().add(sessionKey);
            mainApp.getKeys().remove(ecdhKey);
        }
    }
}
