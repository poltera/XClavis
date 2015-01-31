/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.ui.MainApp;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Gian
 */
public class TopMenuController implements Initializable {

    private MainApp mainApp;
    private ResourceBundle rb;
    private static final String POPUP_TITLE = "XClavis";

    @FXML
    private VBox topMenu;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem miOpen;
    @FXML
    private HBox toolBar;
    @FXML
    private Button fileListBtn;
    @FXML
    private Button qrScannerBtn;
    @FXML
    private Button keyManagementBtn;

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
    }

    /**
     * Is called by the main application to give a reference back to itself.
     *
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void markFileSelecter() {
        fileListBtn.setStyle("-fx-base: #b6e7c9;");
        qrScannerBtn.setStyle("-fx-base: #eaeaea;");
        keyManagementBtn.setStyle("-fx-base: #eaeaea;");
    }

    public void markCodeReader() {
        fileListBtn.setStyle("-fx-base: #eaeaea;");
        qrScannerBtn.setStyle("-fx-base: #b6e7c9;");
        keyManagementBtn.setStyle("-fx-base: #eaeaea;");
    }

    public void markKeyManagement() {
        fileListBtn.setStyle("-fx-base: #eaeaea;");
        qrScannerBtn.setStyle("-fx-base: #eaeaea;");
        keyManagementBtn.setStyle("-fx-base: #b6e7c9;");
    }

    @FXML
    private void openFile(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle(rb.getString("select_file"));
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        List<File> selectedFiles = fileChooser.showOpenMultipleDialog(new Stage());
        if (selectedFiles != null) {
            selectedFiles.stream().forEach((selectedFile) -> {
                mainApp.showFileSelecter();
                mainApp.getFiles().add(selectedFile);
            });
        }
    }

    @FXML
    private void exitApplication(ActionEvent event) {
        Platform.exit();
    }

    @FXML
    private void changeLanguageToEnglish(ActionEvent event) {
        mainApp.getProperties().set("language", "en");
        mainApp.changeLanguage(Locale.ENGLISH);
    }

    @FXML
    private void changeLanguageToGerman(ActionEvent event) {
        mainApp.getProperties().set("language", "de");
        mainApp.changeLanguage(Locale.GERMAN);
    }

    @FXML
    private void showKeyLengthSettings(ActionEvent event) {
        List<String> choices = new ArrayList<>();
        choices.add("128");
        choices.add("256");

        ChoiceDialog<String> dialog = new ChoiceDialog<>(mainApp.getProperties().getString("key_size"), choices);
        dialog.setTitle(POPUP_TITLE);
        dialog.setHeaderText(rb.getString("encryption_key_length"));
        dialog.setContentText(rb.getString("bit_length") + ":");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> mainApp.getProperties().set("key_size", choice));
    }

    @FXML
    private void showFileList(ActionEvent event) {
        mainApp.showFileSelecter();
    }

    @FXML
    private void showQRScanner(ActionEvent event) {
        mainApp.showCodeReader();
    }

    @FXML
    private void showKeyList(ActionEvent event) {
        mainApp.showKeyManagement();
    }

    @FXML
    private void showKeyStorePasswordSettings(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog(rb.getString("password"));
        dialog.setTitle(POPUP_TITLE);
        dialog.setHeaderText(rb.getString("password_protection"));
        dialog.setContentText(rb.getString("password") + ":");

        Optional<String> result = dialog.showAndWait();
        result.ifPresent(choice -> mainApp.getKeys().updatePassword(choice));
    }

    @FXML
    private void showOutputPathSettings(ActionEvent event) {
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
            mainApp.getProperties().set("output_path", selectedDirectory.getAbsolutePath());
            mainApp.showFileSelecter();
        }
    }

    @FXML
    private void showAbout(ActionEvent event) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(POPUP_TITLE);
        alert.setHeaderText(rb.getString("about"));
        alert.setContentText(rb.getString("about_text"));

        alert.showAndWait();
    }

    @FXML
    private void showExtendedSecuritySettings(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(POPUP_TITLE);
        alert.setHeaderText(rb.getString("extended_security"));
        alert.setContentText(rb.getString("activating_extended_security"));

        ButtonType buttonTypeSwitcher;
        if (!mainApp.getProperties().getBoolean("extended_security")) {
            buttonTypeSwitcher = new ButtonType("Aktivieren", ButtonData.OK_DONE);
        } else {
            buttonTypeSwitcher = new ButtonType("Deaktivieren", ButtonData.OK_DONE);
        }
        ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(buttonTypeSwitcher, buttonTypeCancel);

        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == buttonTypeSwitcher) {
            if (!mainApp.getProperties().getBoolean("extended_security")) {
                mainApp.getProperties().set("extended_security", true);
            } else {
                mainApp.getProperties().set("extended_security", false);
            }
        }
    }
}
