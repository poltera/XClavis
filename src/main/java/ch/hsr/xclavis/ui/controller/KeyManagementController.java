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
import ch.hsr.xclavis.ui.MainApp;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

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
    private TableColumn<Key, String> tcPartner;
    @FXML
    private TableColumn<Key, String> tcDate;
    @FXML
    private TableColumn<Key, String> tcSessionID;
    @FXML
    private TableColumn<Key, String> tcState;
    @FXML
    private TableColumn<?, ?> tcDelete;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setPlaceholder(new Label(rb.getString("empty_table_keys")));
        tcPartner.setCellValueFactory(cellData -> cellData.getValue().);
        tcIcon.setCellValueFactory(cellData -> cellData.getValue().iconProperty());
        tcFilename.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        tcExtension.setCellValueFactory(cellData -> cellData.getValue().extensionProperty());
        tcSize.setCellValueFactory(cellData -> cellData.getValue().sizeProperty());
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
                        mainApp.getFileData().remove(tableView.getSelectionModel().getSelectedItem());
                        tableView.getSelectionModel().clearSelection();
                    });

            return new ReadOnlyObjectWrapper(btnDeleteRow);
        });
        btnEncrypt.setVisible(true);
        btnEncrypt.setDisable(false);
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    @FXML
    private void startKeyExchange(ActionEvent event) {
        mainApp.getECDHKeyData().add(new ECDHKey(SessionID.ECDH_REQ_256));
        mainApp.showCodeOutput();
    }

}
