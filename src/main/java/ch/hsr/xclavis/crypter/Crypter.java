/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypter;

import ch.hsr.xclavis.commons.SelectedFile;
import ch.hsr.xclavis.commons.SessionID;
import ch.hsr.xclavis.commons.SessionKey;
import ch.hsr.xclavis.crypto.AESGCM;
import ch.hsr.xclavis.helpers.FileZipper;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;

/**
 *
 * @author Gian
 */
public class Crypter {

    private final static String ENCRYPTED_FILE_EXTENSION = "enc";
    private final static String DELIMITER = "|";
    private final static boolean COMPRESSION = true;

    //private final List<SelectedFile> selectedFiles;
    //private final String outputPath;
    private SessionKey sessionKey;
    private FileZipper zip;
    private AESGCM aes;

    public Crypter() {
    }

    public ReadOnlyDoubleProperty encrypt(List<SelectedFile> selectedFiles, String output) {
        this.sessionKey = new SessionKey(SessionID.SESSION_KEY_128);
        this.zip = new FileZipper();
        this.aes = new AESGCM(sessionKey.getKey(), sessionKey.getIV());
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                List<File> plaintextFiles = new ArrayList<>();
                selectedFiles.forEach((selectedFile) -> {
                    plaintextFiles.add(selectedFile.getFile());
                });
                updateProgress(1, 10);
                byte[] input = zip.getZippedBytes(plaintextFiles, COMPRESSION);
                updateProgress(7, 10);
                boolean result = aes.encrypt(input, output, sessionKey);
                updateProgress(10, 10);
                return null;
            }
        };
        new Thread(task).start();
        
        return task.progressProperty();
    }

    public void decrypt() {

    }
}
