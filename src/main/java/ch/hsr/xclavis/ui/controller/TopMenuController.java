/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.ui.controller;

import ch.hsr.xclavis.ui.MainApp;
import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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
    
    @FXML
    private VBox topMenu;
    @FXML
    private MenuBar menuBar;
    @FXML
    private MenuItem miOpen;
    @FXML
    private HBox toolBar;

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
        mainApp.changeLanguage(Locale.ENGLISH);
    }

    @FXML
    private void changeLanguageToGerman(ActionEvent event) {
        mainApp.changeLanguage(Locale.GERMAN);
    }

    @FXML
    private void scanQR(ActionEvent event) {
        mainApp.showCodeReader();
    }

    @FXML
    private void keyManagement(ActionEvent event) {
        mainApp.showKeyManagement();
    }
}
