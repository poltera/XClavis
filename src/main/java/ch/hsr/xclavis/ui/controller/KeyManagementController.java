/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.keys.Key;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.ui.MainApp;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class KeyManagementController implements Initializable {

    private MainApp mainApp;
    @FXML
    private TextField tfName;
    @FXML
    private Slider slKeyNumbers;
    @FXML
    private TableView<Key> tableView;
    @FXML
    private TableColumn<Key, String> tcID;
    @FXML
    private TableColumn<Key, String> tcPartner;
    @FXML
    private TableColumn<Key, String> tcDate;
    @FXML
    private TableColumn<Key, String> tcState;
    @FXML
    private TableColumn<Key, Button> tcDelete;
    @FXML
    private Button btnBegin;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setPlaceholder(new Label(rb.getString("empty_table_keys")));
        tcID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        tcPartner.setCellValueFactory(cellData -> cellData.getValue().partnerProperty());
        tcDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        tcState.setCellValueFactory(cellData -> cellData.getValue().stateProperty());
        tcDelete.setCellValueFactory((TableColumn.CellDataFeatures<Key, Button> p) -> {
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
                        mainApp.getKeys().remove(tableView.getSelectionModel().getSelectedItem());
                        tableView.getSelectionModel().clearSelection();
                    });

            return new ReadOnlyObjectWrapper(btnDeleteRow);
        });
//        btnEncrypt.setVisible(true);
//        btnEncrypt.setDisable(false);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        //Add observable list data to the table
        tableView.setItems(mainApp.getKeys().getObservableKeyList());
    }

    @FXML
    private void startKeyExchange(ActionEvent event) {
        List<Key> keys = new ArrayList<>();
        for (int i = 0; i < slKeyNumbers.getValue(); i++) {
            if (!mainApp.getProperties().getBoolean("extended_security")) {
                SessionKey sessionKey;
                if (mainApp.getProperties().getInteger("key_size") == 256) {
                    sessionKey = new SessionKey(SessionID.SESSION_KEY_256);
                } else {
                    sessionKey = new SessionKey(SessionID.SESSION_KEY_128);
                }
                sessionKey.setPartner(tfName.getText());
                keys.add(sessionKey);
                mainApp.getKeys().add(sessionKey);
            } else {
                ECDHKey ecdhKey;
                if (mainApp.getProperties().getInteger("key_size") == 256) {
                    ecdhKey = new ECDHKey(SessionID.ECDH_REQ_512);
                } else {
                    ecdhKey = new ECDHKey(SessionID.ECDH_REQ_256);
                }
                ecdhKey.setPartner(tfName.getText());
                ecdhKey.setState("2");
                keys.add(ecdhKey);
                mainApp.getKeys().add(ecdhKey);
            }
        }
        mainApp.showCodeOutput(keys);
        //saveKeys(keys);
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            mainApp.getKeys().remove(tableView.getSelectionModel().getSelectedItem());
            tableView.getSelectionModel().clearSelection();
        }
    }

}
