/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.webcam;

import ch.hsr.xclavis.commons.WebcamInfo;
import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamResolution;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
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

    private ObservableList<WebcamInfo> webcams;
    private Webcam selectedWebcam;
    private BufferedImage bufferedImage;
    private boolean stopCamera;

    public WebcamHandler() {
        this.webcams = FXCollections.observableArrayList();
        this.selectedWebcam = null;
        this.stopCamera = false;
        scanWebcams();
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
    
    public ObservableList<WebcamInfo> getWebcams() {
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

        ObjectProperty<Image> imageProperty = new SimpleObjectProperty<>();
        Task<Void> task = new Task<Void>() {

            @Override
            protected Void call() throws Exception {
                while (!stopCamera) {
                    try {
                        if ((bufferedImage = selectedWebcam.getImage()) != null) {
                            Platform.runLater(() -> {
                                Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                                imageProperty.set(image);
                                LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
                                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                                try {
                                    Result result = new MultiFormatReader().decode(bitmap);
                                    System.out.println(result);
                                } catch (NotFoundException e) {
                                    // No QR Code in the image
                                }
                            });
                        }
                    } catch (Exception e) {
                    } finally {
                    }
                }
                return null;
            }
        };
        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        return imageProperty;
    }

    private void scanWebcams() {
        int webcamCounter = 0;

        for (Webcam webcam : Webcam.getWebcams()) {
            WebcamInfo webcamInfo = new WebcamInfo(webcamCounter, webcam.getName());
            webcams.add(webcamInfo);
            webcamCounter++;
        }
    }

    private void close() {
        if (selectedWebcam != null) {
            selectedWebcam.close();
        }
    }
}
