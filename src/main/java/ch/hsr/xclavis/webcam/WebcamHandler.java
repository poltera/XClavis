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
package ch.hsr.xclavis.webcam;

import ch.hsr.xclavis.qrcode.QRCodeReader;
import com.github.sarxos.webcam.Webcam;
import java.awt.image.BufferedImage;
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
 * @author Gian Poltéra
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
