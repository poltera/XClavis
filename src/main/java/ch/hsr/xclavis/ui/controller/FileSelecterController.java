/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
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
 * FXML Controller class
 *
 * @author Gian
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
    private Button btnScanQR;

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
        tfOutputPath.setText(System.getProperty("user.home"));
        hbButtons.getChildren().removeAll(btnEncrypt, btnDecrypt, btnScanQR);
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
                hbButtons.getChildren().add(btnDecrypt);
            } else if (newValue.intValue() == 2) {
                hbButtons.getChildren().add(btnEncrypt);
            }
        });
        for (SessionKey sessionKey : mainApp.getKeys().getSessionKeys()) {
            cbExisitingKeys.getItems().add(sessionKey.getSessionID().getID());
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
        SessionKey sessionKey;
        if (mainApp.getProperties().getInteger("key_size") == 256) {
            sessionKey = new SessionKey(SessionID.SESSION_KEY_256);
        } else {
            sessionKey = new SessionKey(SessionID.SESSION_KEY_128);
        }
        sessionKey.setPartner("Self");
        List<Key> keys = new ArrayList<>();
        keys.add(sessionKey);
        mainApp.getKeys().add(sessionKey);
        mainApp.showCodeOutput(keys);
        mainApp.showCryptionState(sessionKey, true, tfOutputPath.getText() + File.separator + "encryption.enc");
        //mainApp.getFiles().removeAll();
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
            //mainApp.getFiles().removeAll();
        } else {
            mainApp.showCodeReader();
        }
    }

    @FXML
    private void scanQR(ActionEvent event) {
    }

    @FXML
    private void changeOutputPath(ActionEvent event) {
        //TBA Check if permissions for write in this folder!!
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle(rb.getString("select_folder"));
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        File selectedDirectory = directoryChooser.showDialog(new Stage());
        if (selectedDirectory != null) {
            tfOutputPath.setText(selectedDirectory.getAbsolutePath());
        }
    }
}
