/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.files;

import ch.hsr.xclavis.files.SelectedFile;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.crypto.AESGCM;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;

/**
 *
 * @author Gian
 */
public class FileCrypter {
    private final static int ID_SIZE = 4;
    private final static int IV_SIZE = 96/Byte.SIZE;
    
    
    private byte[] buffer = new byte[2048];
    
    private final static String ENCRYPTED_FILE_EXTENSION = "enc";
    private final static String DELIMITER = "|";
    private final static boolean COMPRESSION = true;

    //private final List<SelectedFile> selectedFiles;
    //private final String outputPath;
    private FileZipper zip;
    private AESGCM aes;

    public FileCrypter() {
    }

    public ReadOnlyDoubleProperty encrypt(SessionKey sessionKey, List<SelectedFile> selectedFiles, String output) {
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

    public ReadOnlyDoubleProperty decrypt(SessionKey sessionKey, SelectedFile selectedFile, String output) {
        this.zip = new FileZipper();
        this.aes = new AESGCM(sessionKey.getKey(), sessionKey.getIV());
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                updateProgress(1, 10);
                byte[] encrypted = fileToByteArrayOutputStream(selectedFile.getFile());
                byte[] decrypted = aes.decryptToByteStream(encrypted);
                updateProgress(5, 10);
                zip.getFilesFromZippedBytes(decrypted, output);
                updateProgress(10, 10);
                return null;
            }
        };
        new Thread(task).start();

        return task.progressProperty();
    }

    private byte[] fileToByteArrayOutputStream(File file) {
        try (FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Remove the ID and the IV from the beginning of the file.
            dis.skipBytes(ID_SIZE + IV_SIZE);
            int length;
            while ((length = dis.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(FileZipper.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
