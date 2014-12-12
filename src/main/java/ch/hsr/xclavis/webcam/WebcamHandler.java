/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.webcam;

import com.github.sarxos.webcam.Webcam;

/**
 *
 * @author Gian
 */
public class WebcamHandler {
    public void getWebcams() {
        for (Webcam webcam : Webcam.getWebcams()) {
            System.out.println(webcam.getName());
        }
    }
}
