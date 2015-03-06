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
package ch.hsr.xclavis.ui;

import ch.hsr.xclavis.keys.KeyStore;
import ch.hsr.xclavis.files.FileHandler;
import ch.hsr.xclavis.helpers.PropertiesHandler;
import ch.hsr.xclavis.keys.Key;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.ui.controller.CodeOutputController;
import ch.hsr.xclavis.ui.controller.CodeReaderController;
import ch.hsr.xclavis.ui.controller.CryptionStateController;
import ch.hsr.xclavis.ui.controller.FileSelecterController;
import ch.hsr.xclavis.ui.controller.KeyManagementController;
import ch.hsr.xclavis.ui.controller.RootPaneController;
import ch.hsr.xclavis.ui.controller.TopMenuController;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class is the main application of the user interface and the start class.
 *
 * @author Gian Poltéra
 */
public class MainApp extends Application {

    private Stage stage;

    private FXMLLoader codeOutputLoader, codeReaderLoader, cryptionStateLoader, fileSelecterLoader, keyManagementLoader, rootPaneLoader, topMenuLoader;
    private CodeOutputController codeOutputController;
    private CodeReaderController codeReaderController;
    private CryptionStateController cryptionStateController;
    private FileSelecterController fileSelecterController;
    private KeyManagementController keyManagementController;
    private RootPaneController rootPaneController;
    private TopMenuController topMenuController;
    private VBox codeOutputBox, codeReaderBox, cryptionStateBox, fileSelecterBox, keyManagementBox, topMenuBox;
    private BorderPane rootPane;

    private ResourceBundle rb;
    private PropertiesHandler properties;
    private FileHandler files;
    private KeyStore keys;

    /**
     * Creates a new MainApp.
     */
    public MainApp() {
        Locale locale = Locale.getDefault();
        this.rb = ResourceBundle.getBundle("bundles.XClavis", locale);
        this.properties = new PropertiesHandler();
        this.files = new FileHandler();
        this.keys = new KeyStore();

        //TBA
        String version = System.getProperty("java.version");
        char minor = version.charAt(2);
        char point = version.charAt(6);
        if (minor < '8' || point < '4') {
            throw new RuntimeException("JDK 1.8.40 or higher "
                    + "is required to run XClavis.");
        }
        System.out.println("JDK version " + version + " found");
    }

    /**
     * The main() method is ignored in correctly deployed JavaFX application.
     * main() serves only as fallback in case the application can not be
     * launched through deployment artifacts, e.g., in IDEs with limited FX
     * support. NetBeans ignores main().
     *
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Gets the properties.
     *
     * @return the properties as a PropertiesHandler
     */
    public PropertiesHandler getProperties() {
        return properties;
    }

    /**
     * Gets the files.
     *
     * @return the files as a FileHandler
     */
    public FileHandler getFiles() {
        return files;
    }

    /**
     * Gets the Keys.
     *
     * @return the keys as a KeyStore
     */
    public KeyStore getKeys() {
        return keys;
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/xclavis.png")));
        this.stage.setTitle("XClavis");
        this.stage.setResizable(false);

        while (!keys.isPasswordCorrect()) {
            showPasswordInput();
        }

        initAllWindows();
        showRootPane();
        showTopMenu();
        showFileSelecter();
    }

    /**
     * Initializes all windows.
     */
    public void initAllWindows() {
        try {
            codeOutputLoader = getLoader("/fxml/CodeOutput.fxml");
            codeOutputBox = codeOutputLoader.load();
            codeOutputController = codeOutputLoader.getController();
            codeOutputController.setMainApp(this);

            codeReaderLoader = getLoader("/fxml/CodeReader.fxml");
            codeReaderBox = codeReaderLoader.load();
            codeReaderController = codeReaderLoader.getController();
            codeReaderController.setMainApp(this);

            cryptionStateLoader = getLoader("/fxml/CryptionState.fxml");
            cryptionStateBox = cryptionStateLoader.load();
            cryptionStateController = cryptionStateLoader.getController();
            cryptionStateController.setMainApp(this);

            fileSelecterLoader = getLoader("/fxml/FileSelecter.fxml");
            fileSelecterBox = fileSelecterLoader.load();
            fileSelecterController = fileSelecterLoader.getController();
            fileSelecterController.setMainApp(this);

            keyManagementLoader = getLoader("/fxml/KeyManagement.fxml");
            keyManagementBox = keyManagementLoader.load();
            keyManagementController = keyManagementLoader.getController();
            keyManagementController.setMainApp(this);

            rootPaneLoader = getLoader("/fxml/RootPane.fxml");
            rootPane = rootPaneLoader.load();
            rootPaneController = rootPaneLoader.getController();
            rootPaneController.setMainApp(this);

            topMenuLoader = getLoader("/fxml/TopMenu.fxml");
            topMenuBox = topMenuLoader.load();
            topMenuController = topMenuLoader.getController();
            topMenuController.setMainApp(this);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Shows the RootPane.
     */
    public void showRootPane() {
        Scene scene = new Scene(rootPane);
        scene.getStylesheets().add("/styles/Styles.css");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Shows the TopMenu at the top of the root layout.
     */
    public void showTopMenu() {
        rootPane.setTop(topMenuBox);
    }

    /**
     * Shows the FileSelecter inside the root layout.
     */
    public void showFileSelecter() {
        rootPane.setCenter(fileSelecterBox);
        topMenuController.markFileSelecter();
        fileSelecterController.updateView();
    }

    /**
     * Shows the CryptionState inside the root layout.
     *
     * @param sessionKey the SessionKey for the cryption
     * @param encryption true for encryption or false for decryption
     * @param output the output path for the files
     */
    public void showCryptionState(SessionKey sessionKey, boolean encryption, String output) {
        rootPane.setBottom(cryptionStateBox);
        cryptionStateController.setParameters(sessionKey, encryption, output);
    }

    /**
     * Removes the CryptionState from the root layout.
     */
    public void removeCryptionState() {
        rootPane.setBottom(null);
    }

    /**
     * Shows the CodeReader inside the root layout.
     */
    public void showCodeReader() {
        rootPane.setCenter(codeReaderBox);
        topMenuController.markCodeReader();
        codeReaderController.startWebcam();
    }

    /**
     * Shows the CodeOutput inside the root layout.
     *
     * @param keys
     */
    public void showCodeOutput(List<Key> keys) {
        rootPane.setCenter(codeOutputBox);
        codeOutputController.setKeys(keys);
    }

    /**
     * Shows the KeyManagement inside the root layout.
     */
    public void showKeyManagement() {
        rootPane.setCenter(keyManagementBox);
        topMenuController.markKeyManagement();
    }

    /**
     * Changes the language.
     *
     * @param locale the locale for the new language
     */
    public void changeLanguage(Locale locale) {
        rb = ResourceBundle.getBundle("bundles.XClavis", locale);
        initAllWindows();
        showRootPane();
        showTopMenu();
        showFileSelecter();
    }

    private FXMLLoader getLoader(String path) {
        return new FXMLLoader(getClass().getResource(path), rb);
    }

    private void showPasswordInput() {
        TextInputDialog dialog = new TextInputDialog(rb.getString("password"));
        dialog.setTitle("XClavis");
        dialog.setHeaderText(rb.getString("password_input"));
        dialog.setContentText("");

        Optional<String> password = dialog.showAndWait();
        password.ifPresent(choice -> keys = new KeyStore(choice));
    }
}
