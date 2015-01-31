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
     * Constructor
     */
    public MainApp() {
        Locale locale = Locale.getDefault();
        this.rb = ResourceBundle.getBundle("bundles.XClavis", locale);
        this.properties = new PropertiesHandler();
        this.files = new FileHandler();
        this.keys = new KeyStore();

        while (!keys.isPasswordCorrect()) {
            showPasswordInput();
        }
    }

    /**
     * Returns the Properties.
     *
     * @return
     */
    public PropertiesHandler getProperties() {
        return properties;
    }

    /**
     * Returns the Files.
     *
     * @return
     */
    public FileHandler getFiles() {
        return files;
    }

    /**
     * Return the Keys.
     *
     * @return
     */
    public KeyStore getKeys() {
        return keys;
    }

    private void showPasswordInput() {
        TextInputDialog dialog = new TextInputDialog(rb.getString("password"));
        dialog.setTitle("XClavis");
        dialog.setHeaderText(rb.getString("password_input"));
        dialog.setContentText("");

        Optional<String> password = dialog.showAndWait();
        password.ifPresent(choice -> keys = new KeyStore(choice));
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        this.stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/xclavis.png")));
        this.stage.setTitle("XClavis");
        this.stage.setResizable(false);

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
     * Shows the Encryption Status inside the root layout.
     *
     * @param sessionKey
     * @param encryption, true for encryption and false for decryption
     * @param output, Path for the output files
     */
    public void showCryptionState(SessionKey sessionKey, boolean encryption, String output) {
        rootPane.setBottom(cryptionStateBox);
        cryptionStateController.setParameters(sessionKey, encryption, output);
    }

    public void removeCryptionState() {
        rootPane.setBottom(null);
    }

    /**
     * Shows the Code Reader inside the root layout.
     */
    public void showCodeReader() {
        rootPane.setCenter(codeReaderBox);
        topMenuController.markCodeReader();
        codeReaderController.startWebcam();
    }

    /**
     * Shows the Code Output inside the root layout.
     *
     * @param keys
     */
    public void showCodeOutput(List<Key> keys) {
        rootPane.setCenter(codeOutputBox);
        codeOutputController.setKeys(keys);
    }

    /**
     * Shows the Key Management inside the root layout.
     */
    public void showKeyManagement() {
        rootPane.setCenter(keyManagementBox);
        topMenuController.markKeyManagement();
    }

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
}
