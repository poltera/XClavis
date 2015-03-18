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

import ch.hsr.xclavis.ui.MainApp;
import ch.hsr.xclavis.files.SelectedFile;
import ch.hsr.xclavis.keys.Key;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class Shows the table for file selection.
 *
 * @author Gian Poltéra
 */
public class FileSelecterController implements Initializable {

    private MainApp mainApp;
    private ResourceBundle rb;

    @FXML
    private TableView<SelectedFile> tableView;
    @FXML
    private TableColumn<SelectedFile, ImageView> tcIcon;
    @FXML
    private TableColumn<SelectedFile, String> tcFilename;
    @FXML
    private TableColumn<SelectedFile, String> tcExtension;
    @FXML
    private TableColumn<SelectedFile, String> tcSize;
    @FXML
    private TableColumn<SelectedFile, String> tcID;
    @FXML
    private TableColumn<SelectedFile, Button> tcDelete;
    @FXML
    private ComboBox<String> cbExisitingKeys;
    @FXML
    private TextField tfOutputPath;
    @FXML
    private HBox hbButtons;
    @FXML
    private Button btnEncrypt;
    @FXML
    private Button btnDecrypt;
    @FXML
    private Button btnCodeReader;
    @FXML
    private Button changeBtn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        tableView.setPlaceholder(new Label(rb.getString("empty_table_files")));
        tcIcon.setCellValueFactory(cellData -> cellData.getValue().iconProperty());
        tcFilename.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tcExtension.setCellValueFactory(cellData -> cellData.getValue().extensionProperty());
        tcSize.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
        tcID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        tcDelete.setCellValueFactory((TableColumn.CellDataFeatures<SelectedFile, Button> p) -> {
            Button btnDeleteRow = new Button();
            btnDeleteRow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/delete1.png"))));

            btnDeleteRow.setStyle("-fx-background-color:transparent");
            btnDeleteRow.setPadding(new Insets(0, 0, 0, 0));
            btnDeleteRow.setOnMouseEntered((event) -> {
                btnDeleteRow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/delete2.png"))));
            });
            btnDeleteRow.setOnMouseExited((event) -> {
                btnDeleteRow.setGraphic(new ImageView(new Image(getClass().getResourceAsStream("/images/delete1.png"))));
            });
            btnDeleteRow.setOnAction(
                    (event) -> {
                        tableView.requestFocus();
                        tableView.getSelectionModel().select(p.getValue());
                        tableView.getFocusModel().focus(tableView.getSelectionModel().getSelectedIndex());
                        mainApp.getFiles().remove(tableView.getSelectionModel().getSelectedItem());
                        tableView.getSelectionModel().clearSelection();
                    });

            return new ReadOnlyObjectWrapper(btnDeleteRow);
        });

        hbButtons.getChildren().removeAll(btnEncrypt, btnDecrypt, btnCodeReader);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        //Add observable list data to the table
        tableView.setItems(mainApp.getFiles().getObservableFileList());

        mainApp.getFiles().modeProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            hbButtons.getChildren().removeAll(hbButtons.getChildren().sorted());
            if (newValue.intValue() == 1) {
                String id = mainApp.getFiles().getObservableFileList().get(0).getID();
                byte[] iv = mainApp.getFiles().getObservableFileList().get(0).getIV();
                SessionID sessionID = new SessionID(id.substring(0, 1), id.substring(1));
                if (mainApp.getKeys().existsKey(sessionID)) {
                    hbButtons.getChildren().add(btnDecrypt);
                    changeBtn.setDisable(false);
                } else {
                    hbButtons.getChildren().add(btnCodeReader);
                    changeBtn.setDisable(true);
                }
                cbExisitingKeys.setDisable(true);
            } else if (newValue.intValue() == 2) {
                hbButtons.getChildren().add(btnEncrypt);
                cbExisitingKeys.setDisable(false);
                changeBtn.setDisable(false);
            } else {
                cbExisitingKeys.setDisable(true);
                changeBtn.setDisable(true);
            }
        });
        mainApp.getKeys().getSessionKeys().stream().filter((sessionKey) -> (sessionKey.getState().equals("0"))).forEach((sessionKey) -> {
            cbExisitingKeys.getItems().add(sessionKey.getSessionID().getID() + "-" + sessionKey.getPartner());
        });
        if (mainApp.getProperties().getString("output_path").equals("default")) {
            tfOutputPath.setText(System.getProperty("user.home"));
        } else {
            tfOutputPath.setText(mainApp.getProperties().getString("output_path"));
        }
    }

    /**
     * Updates the view if something changed.
     */
    public void updateView() {
        cbExisitingKeys.getItems().clear();
        mainApp.getKeys().getSessionKeys().stream().filter((sessionKey) -> (sessionKey.getState().equals("0"))).forEach((sessionKey) -> {
            cbExisitingKeys.getItems().add(sessionKey.getSessionID().getID() + "-" + sessionKey.getPartner());
        });
        if (mainApp.getProperties().getString("output_path").equals("default")) {
            tfOutputPath.setText(System.getProperty("user.home"));
        } else {
            tfOutputPath.setText(mainApp.getProperties().getString("output_path"));
        }
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            mainApp.getFiles().remove(tableView.getSelectionModel().getSelectedItem());
            tableView.getSelectionModel().clearSelection();
        }
    }

    @FXML
    private void encryptFiles(ActionEvent event) {
        String filename;
        if (cbExisitingKeys.getValue() == null) {
            // Encryption with new key
            SessionKey sessionKey;
            if (mainApp.getProperties().getInteger("key_size") == 256) {
                sessionKey = new SessionKey(SessionID.SESSION_KEY_256);
            } else {
                sessionKey = new SessionKey(SessionID.SESSION_KEY_128);
            }
            filename = tfOutputPath.getText() + File.separator + "ENC_" + sessionKey.getID() + ".enc";
            if (checkOverwriteFile(filename)) {
                sessionKey.setPartner("Self");
                sessionKey.setState(Key.USED);
                List<Key> keys = new ArrayList<>();
                keys.add(sessionKey);
                mainApp.getKeys().add(sessionKey);
                mainApp.showCodeOutput(keys);
                mainApp.showCryptionState(sessionKey, true, filename);
            }
        } else {
            // Encryption with existing key
            String selectedKey = cbExisitingKeys.getValue();
            String[] splittedKey = selectedKey.split("-");
            String type = splittedKey[0].substring(0, 1);
            String random = splittedKey[0].substring(1);
            SessionID sessionID = new SessionID(type, random);
            SessionKey sessionKey = mainApp.getKeys().getSessionKey(sessionID);
            filename = tfOutputPath.getText() + File.separator + "ENC_" + sessionKey.getID() + ".enc";
            if (checkOverwriteFile(filename)) {
                sessionKey.setState(Key.USED);
                List<Key> keys = new ArrayList<>();
                keys.add(sessionKey);
                mainApp.showCodeOutput(keys);
                mainApp.showCryptionState(sessionKey, true, filename);
            }
        }
    }

    @FXML
    private void decryptFiles(ActionEvent event) {
        String id = mainApp.getFiles().getObservableFileList().get(0).getID();
        byte[] iv = mainApp.getFiles().getObservableFileList().get(0).getIV();
        SessionID sessionID = new SessionID(id.substring(0, 1), id.substring(1));
        if (mainApp.getKeys().existsKey(sessionID)) {
            SessionKey sessionKey = mainApp.getKeys().getSessionKey(sessionID);
            sessionKey.setIV(iv);
            mainApp.showCryptionState(sessionKey, false, tfOutputPath.getText());
        } else {
            mainApp.showCodeReader();
        }
    }

    @FXML
    private void changeOutputPath(ActionEvent event) {
        //TBA Check if permissions for write in this folder!!
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(rb.getString("select_folder"));
        if (mainApp.getProperties().getString("output_path").equals("default")) {
            directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        } else {
            directoryChooser.setInitialDirectory(new File(mainApp.getProperties().getString("output_path")));
        }
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            tfOutputPath.setText(selectedDirectory.getAbsolutePath());
        }
    }

    @FXML
    private void codeReader(ActionEvent event) {
        String id = mainApp.getFiles().getObservableFileList().get(0).getID();
        byte[] iv = mainApp.getFiles().getObservableFileList().get(0).getIV();
        SessionID sessionID = new SessionID(id.substring(0, 1), id.substring(1));
        if (mainApp.getKeys().existsKey(sessionID)) {
            SessionKey sessionKey = mainApp.getKeys().getSessionKey(sessionID);
            sessionKey.setIV(iv);
            mainApp.showCryptionState(sessionKey, false, tfOutputPath.getText());
        } else {
            mainApp.showCodeReader();
        }
    }

    private boolean checkOverwriteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle(rb.getString("window_title"));
            alert.setHeaderText(rb.getString("file_exists"));
            alert.setContentText(rb.getString("file_overwritten"));

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                return true;
            } else {
                changeOutputPath(null);
                return false;
            }
        } else {
            return true;
        }
    }
}
