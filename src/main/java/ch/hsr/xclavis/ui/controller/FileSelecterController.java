/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.ui.MainApp;
import ch.hsr.xclavis.commons.SelectedFile;
import ch.hsr.xclavis.helpers.FileHandler;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class FileSelecterController implements Initializable {
    private MainApp mainApp;

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
    private TableColumn<SelectedFile, Button> tcDelete;
    @FXML
    private Button btnEncrypt;
    @FXML
    private Button btnDecrypt;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tableView.setPlaceholder(new Label(rb.getString("empty_table_files")));
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

        //Add observable list data to the table
        tableView.setItems(mainApp.getFileData());
    }

    @FXML
    private void keyPressed(KeyEvent event) {
        if (event.getCode() == KeyCode.DELETE) {
            mainApp.getFileData().remove(tableView.getSelectionModel().getSelectedItem());
            tableView.getSelectionModel().clearSelection();
        }
    }

    public void loadFile(File file) {
        FileHandler.loadFile(file, mainApp.getFileData());
        tableView.setDisable(true);
    }

    @FXML
    private void encryptFiles(ActionEvent event) {
        mainApp.showEncryptionStatus();
    }

    @FXML
    private void decryptFiles(ActionEvent event) {
    }
}
