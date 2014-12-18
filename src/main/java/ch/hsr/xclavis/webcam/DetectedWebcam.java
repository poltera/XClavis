/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.webcam;

/**
 *
 * @author Gian
 */
public class DetectedWebcam {
    private String webcamName;
    private int webcamIndex;
    
    public DetectedWebcam(int webcamIndex, String webcamName) {
        this.webcamIndex = webcamIndex;
        this.webcamName = webcamName;
    }
    
    public String getWebcamName() {
        return webcamName;
    }
    
    public int getWebcamIndex() {
        return webcamIndex;
    }
    
    @Override
    public String toString() {
        return webcamName;
    }
}
