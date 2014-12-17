/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.commons.ECDHKey;
import ch.hsr.xclavis.commons.Key;
import ch.hsr.xclavis.commons.Keys;
import ch.hsr.xclavis.commons.SessionID;
import ch.hsr.xclavis.commons.SessionKey;
import ch.hsr.xclavis.ui.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
    private CheckBox cbExtendedSecurity;
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
                        mainApp.getKeyData().remove(tableView.getSelectionModel().getSelectedItem());
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
        tableView.setItems(mainApp.getKeyData());
    }

    @FXML
    private void startKeyExchange(ActionEvent event) {
        Keys keys = new Keys();
        for (int i = 0; i < slKeyNumbers.getValue(); i++) {
            if (!cbExtendedSecurity.isSelected()) {
                SessionKey sessionKey = new SessionKey(SessionID.SESSION_KEY_128);
                Key key = new Key(sessionKey.getSessionID(), tfName.getText(), 0); 
                keys.addKey(sessionKey);
                mainApp.getKeyData().add(key);
            } else {
                ECDHKey ecdhKey = new ECDHKey(SessionID.ECDH_REQ_256);
                Key key = new Key(ecdhKey.getSessionID(), tfName.getText(), 0);
                keys.addKey(ecdhKey);
                mainApp.getKeyData().add(key);
            }
        }
        mainApp.showCodeOutput(keys);
        //saveKeys(keys);
    }

}
