package ch.hsr.xclavis.ui;

import ch.hsr.xclavis.commons.SelectedFile;
import ch.hsr.xclavis.ui.controller.CodeOutputController;
import ch.hsr.xclavis.ui.controller.CodeReaderController;
import ch.hsr.xclavis.ui.controller.EncryptionStatusController;
import ch.hsr.xclavis.ui.controller.FileSelecterController;
import ch.hsr.xclavis.ui.controller.KeyManagementController;
import ch.hsr.xclavis.ui.controller.RootPaneController;
import ch.hsr.xclavis.ui.controller.TopMenuController;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MainApp extends Application {

    private Stage stage;
    private BorderPane rootLayout;
    private ResourceBundle bundle;
    private ObservableList<SelectedFile> fileData;

    /**
     * Constructor
     */
    public MainApp() {
        this.fileData = FXCollections.observableArrayList();
    }

    /**
     * Returns the data as an observable list of SelectedFiles.
     *
     * @return
     */
    public ObservableList<SelectedFile> getFileData() {
        return fileData;
    }

    @Override
    public void start(Stage stage) {
        //try {
        this.stage = stage;
        //stage.getIcons().add(new Image(Start.class.getResourceAsStream("logo.jpg")));
        this.stage.setTitle("XClavis");
        //this.stage.setResizable(false);
        Locale locale = Locale.getDefault();
        this.bundle = ResourceBundle.getBundle("bundles.XClavis", locale);
        //new PropertyResourceBundle(getClass().getResource("/bundles/XClavis_de.properties").openStream());

        initRootLayout();
        showTopMenu();
        showFileSelecter();

        //} catch (IOException ex) {
        //    Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        //}
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            //  Load RootLayout.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/RootPane.fxml"), bundle);
            rootLayout = loader.load();
            Scene scene = new Scene(rootLayout);
            scene.getStylesheets().add("/styles/Styles.css");
            stage.setScene(scene);
            stage.show();

            // Give the controller access to the main app.
            RootPaneController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Shows the TopMenu at the top of the root layout.
     */
    public void showTopMenu() {
        try {
            // Load MainMenu.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/TopMenu.fxml"), bundle);
            VBox topMenu = loader.load();

            // Set MainMenu at the top of root layout.
            rootLayout.setTop(topMenu);

            // Give the controller access to the main app.
            TopMenuController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Shows the FileSelecter inside the root layout.
     */
    public void showFileSelecter() {
        try {
            // Load file overview.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/FileSelecter.fxml"), bundle);
            VBox fileSelecter = loader.load();

            // Set file overview into the center of root layout.
            rootLayout.setCenter(fileSelecter);

            // Give the controller access to the main app.
            FileSelecterController controller = loader.getController();
            controller.setMainApp(this);

        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Shows the Encryption Status inside the root layout.
     */
    public void showEncryptionStatus() {
        try {
            // Load file overview.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/EncryptionStatus.fxml"), bundle);
            VBox encryptionStatus = loader.load();

            // Set file overview into the center of root layout.
            rootLayout.setCenter(encryptionStatus);

            // Give the controller access to the main app.
            EncryptionStatusController controller = loader.getController();

            controller.setMainApp(this);

        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Shows the Code Reader inside the root layout.
     */
    public void showCodeReader() {
        try {
            // Load file overview.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CodeReader.fxml"), bundle);
            VBox codeReader = loader.load();

            // Set file overview into the center of root layout.
            rootLayout.setCenter(codeReader);

            // Give the controller access to the main app.
            CodeReaderController controller = loader.getController();

            controller.setMainApp(this);

        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Shows the Code Output inside the root layout.
     */
    public void showCodeOutput() {
        try {
            // Load file overview.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/CodeOutput.fxml"), bundle);
            VBox codeOuput = loader.load();

            // Set file overview into the center of root layout.
            rootLayout.setCenter(codeOuput);

            // Give the controller access to the main app.
            CodeOutputController controller = loader.getController();

            controller.setMainApp(this);

        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Shows the Key Management inside the root layout.
     */
    public void showKeyManagement() {
        try {
            // Load file overview.
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/KeyManagement.fxml"), bundle);
            VBox keyManagement = loader.load();

            // Set file overview into the center of root layout.
            rootLayout.setCenter(keyManagement);

            // Give the controller access to the main app.
            KeyManagementController controller = loader.getController();

            controller.setMainApp(this);

        } catch (IOException ex) {
            Logger.getLogger(MainApp.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void changeLanguage(Locale locale) {
        bundle = ResourceBundle.getBundle("bundles.XClavis", locale);
        initRootLayout();
        showTopMenu();
        showFileSelecter();
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
