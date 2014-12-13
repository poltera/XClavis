/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

/**
 *
 * @author Gian
 */
public class WebcamInfo {
    private String webcamName;
    private int webcamIndex;
    
    public WebcamInfo(int webcamIndex, String webcamName) {
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
