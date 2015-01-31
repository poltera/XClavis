/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.webcam;

import ch.hsr.xclavis.qrcode.QRCodeReader;
import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;

/**
 *
 * @author Gian
 */
public class WebcamHandler {

    private static final int FPS = 24;
    private static final int QRFPS = 5;
    private int sleepTimer;

    private ObservableList<DetectedWebcam> webcams;
    private Webcam selectedWebcam;
    private BufferedImage bufferedImage;
    private boolean stopCamera;
    private QRCodeReader qrCodeReader;
    private StringProperty qrResult;
    
    private int qrFlopsCounter;

    public WebcamHandler() {
        this.webcams = FXCollections.observableArrayList();
        this.sleepTimer = 1000 / FPS;
        this.qrFlopsCounter = 0;
        this.selectedWebcam = null;
        this.stopCamera = false;
        scanWebcams();
        this.qrCodeReader = new QRCodeReader();
        this.qrResult = new SimpleStringProperty("");
    }

    public boolean existsWebcam() {
        if (webcams.size() > 0) {
            return true;
        }

        return false;
    }

    public int getWebcamCount() {
        return webcams.size();
    }

    public ObservableList<DetectedWebcam> getWebcams() {
        return webcams;
    }

    public void initWebcam(final int webcamIndex) {
        Task<Void> webcamInitializer = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                if (!(selectedWebcam == null)) {
                    close();
                }
                selectedWebcam = Webcam.getWebcams().get(webcamIndex);
                //selectedWebcam.setViewSize(WebcamResolution.QVGA.getSize());
                selectedWebcam.open();
                //startWebcamStream();
                return null;
            }
        };
        new Thread(webcamInitializer).start();
        //fpBottomPane.setDisable(false);
    }

    public ObjectProperty<Image> getStream() {
        stopCamera = false;
        ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                
                while (!stopCamera) {
                    if (selectedWebcam.isOpen()) {
                        try {
                            if ((bufferedImage = selectedWebcam.getImage()) != null) {
                                Platform.runLater(() -> {
                                    // Convert the Image for JavaFX
                                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                                    imageProperty.set(image);
                                    qrFlopsCounter++;
                                    if (qrFlopsCounter == 5) {
                                        // Check if QR-Code is in Image
                                        if (qrCodeReader.checkImage(bufferedImage)) {
                                            qrResult.set(qrCodeReader.getResult());
                                        }
                                        qrFlopsCounter = 0;
                                    }

                                });
                            }
                        } catch (Exception e) {
                        } finally {
                        }
                    }
                    // Sleep Timer for FPS
                    Thread.sleep(sleepTimer);
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return imageProperty;
    }

    public StringProperty getScanedQRCode() {
        return qrResult;
    }

    public void stopWebcam() {
        stopCamera = true;
        close();
    }

    private void scanWebcams() {
        int webcamCounter = 0;

        for (Webcam webcam : Webcam.getWebcams()) {
            DetectedWebcam webcamInfo = new DetectedWebcam(webcamCounter, webcam.getName());
            webcams.add(webcamInfo);
            webcamCounter++;
        }
        // Stop searching Webcams
//        WebcamDiscoveryService discovery = Webcam.getDiscoveryService();
//        discovery.stop();
    }

    private void close() {
        if (selectedWebcam != null) {
            selectedWebcam.close();
        }
    }
}
