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

import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.keys.Key;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.qrcode.QRCodeGenerator;
import ch.hsr.xclavis.ui.MainApp;
import java.lang.reflect.Field;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

/**
 * FXML Controller class Shows the created and obtained keys.
 *
 * @author Gian Poltéra
 */
public class KeyManagementController implements Initializable {

    private MainApp mainApp;
    //private MenuItem miShowQRCode;
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
    private TableColumn<Key, Label> tcState;
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
        // Context menu
        tableView.setRowFactory(
                (call) -> {
                    final TableRow<Key> row = new TableRow<>();
                    final ContextMenu contextMenu = new ContextMenu();
                    final MenuItem miShowQRCode = new MenuItem(rb.getString("show_qr_code"));
                    miShowQRCode.setOnAction(
                            (event) -> {
                                mainApp.showCodeOutput(tableView.getSelectionModel().getSelectedItems());
                            });
                    contextMenu.getItems().add(miShowQRCode);
                    // Set context menu on row, but use a binding to make it only show for non-empty rows:
                    row.contextMenuProperty().bind(
                            Bindings.when(row.emptyProperty())
                            .then((ContextMenu) null)
                            .otherwise(contextMenu)
                    );

                    return row;
                });

        tcID.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        tcPartner.setCellValueFactory(cellData -> cellData.getValue().partnerProperty());
        tcDate.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
        // State image
        tcState.setCellValueFactory((TableColumn.CellDataFeatures<Key, Label> cellData) -> {
            String state = cellData.getValue().stateProperty().get();
            Label lblState = new Label();
            lblState.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            switch (state) {
                case Key.USABLE:
                    ImageView ivOK = new ImageView(new Image(getClass().getResourceAsStream("/images/ok.png")));
                    Tooltip ttOK = new Tooltip(rb.getString("usabel"));
                    hackTooltipStartTiming(ttOK);
                    lblState.setGraphic(ivOK);
                    lblState.setTooltip(ttOK);
                    break;
                case Key.USED:
                    ImageView ivNOK = new ImageView(new Image(getClass().getResourceAsStream("/images/not_ok.png")));
                    Tooltip ttNOK = new Tooltip(rb.getString("used"));
                    hackTooltipStartTiming(ttNOK);
                    lblState.setGraphic(ivNOK);
                    lblState.setTooltip(ttNOK);
                    break;
                case Key.WAIT:
                    ImageView ivWait = new ImageView(new Image(getClass().getResourceAsStream("/images/wait.png")));
                    Tooltip ttWait = new Tooltip(rb.getString("wait_for_remote"));
                    hackTooltipStartTiming(ttWait);
                    lblState.setGraphic(ivWait);
                    lblState.setTooltip(ttWait);
                    break;
                case Key.REMOTE:
                    ImageView ivRemote = new ImageView(new Image(getClass().getResourceAsStream("/images/remote.png")));
                    Tooltip ttRemote = new Tooltip(rb.getString("from_remote"));
                    hackTooltipStartTiming(ttRemote);
                    lblState.setGraphic(ivRemote);
                    lblState.setTooltip(ttRemote);
                    break;
            }

            return new ReadOnlyObjectWrapper(lblState);
        });
        // Delete button
        tcDelete.setCellValueFactory((TableColumn.CellDataFeatures<Key, Button> cellData) -> {
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
                        tableView.getSelectionModel().select(cellData.getValue());
                        tableView.getFocusModel().focus(tableView.getSelectionModel().getSelectedIndex());
                        mainApp.getKeys().remove(tableView.getSelectionModel().getSelectedItem());
                        tableView.getSelectionModel().clearSelection();
                    });

            return new ReadOnlyObjectWrapper(btnDeleteRow);
        }
        );
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
                ecdhKey.setState(Key.WAIT);
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
    
    private void hackTooltipStartTiming(Tooltip tooltip) {
    try {
        Field fieldBehavior = tooltip.getClass().getDeclaredField("BEHAVIOR");
        fieldBehavior.setAccessible(true);
        Object objBehavior = fieldBehavior.get(tooltip);

        Field fieldTimer = objBehavior.getClass().getDeclaredField("activationTimer");
        fieldTimer.setAccessible(true);
        Timeline objTimer = (Timeline) fieldTimer.get(objBehavior);

        objTimer.getKeyFrames().clear();
        objTimer.getKeyFrames().add(new KeyFrame(new Duration(100)));
    } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
        Logger.getLogger(KeyManagementController.class.getName()).log(Level.SEVERE, null, ex);
    }
}
}
