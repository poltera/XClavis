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
package ch.hsr.xclavis.files;

import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.crypto.AESGCM;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.ReadOnlyDoubleProperty;
import javafx.concurrent.Task;

/**
 * This class encrypts and decrypts files.
 * 
 * @author Gian Poltéra
 */
public class FileCrypter {

    private final static int ID_SIZE = 4;
    private final static int IV_SIZE = 96 / Byte.SIZE;
    private byte[] buffer = new byte[2048];
    private final static boolean COMPRESSION = true;

    private FileZipper zip;
    private AESGCM aes;

    /**
     * Encrypts a list of files to a specific output.
     * 
     * @param sessionKey for the encryption
     * @param files list of files to encrypt
     * @param output output-path for the encrpyted file
     * @return the status of the encryption as a ReadOnlyDoubleProperty
     */
    public ReadOnlyDoubleProperty encrypt(SessionKey sessionKey, List<File> files, String output) {
        this.zip = new FileZipper();
        this.aes = new AESGCM(sessionKey.getKey(), sessionKey.getIV());
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                updateProgress(1, 10);
                long before = System.nanoTime();
                // ZIP the files
                byte[] input = zip.getZippedBytes(files, COMPRESSION);
                updateProgress(7, 10);
                // Encrypt the files
                boolean result = aes.encrypt(input, output, sessionKey);
                long after = System.nanoTime();
                long runningTimeMs = (after - before) / 1000000;
                updateProgress(10, 10);
                System.out.println("encryption time: " + runningTimeMs);

                return null;
            }
        };
        new Thread(task).start();

        return task.progressProperty();
    }

    /**
     * Decrypts a file to a specific output.
     * 
     * @param sessionKey for the decryption
     * @param file to decrypt
     * @param output output-path for the decrpyted file
     * @return the status of the encryption as a ReadOnlyDoubleProperty
     */
    public ReadOnlyDoubleProperty decrypt(SessionKey sessionKey, File file, String output) {
        this.zip = new FileZipper();
        this.aes = new AESGCM(sessionKey.getKey(), sessionKey.getIV());
        Task task = new Task<Void>() {
            @Override
            public Void call() {
                updateProgress(1, 10);
                byte[] encrypted = fileToByteArrayOutputStream(file);
                // Decrypt the file
                byte[] decrypted = aes.decryptToByteStream(encrypted);
                updateProgress(5, 10);
                // DeZIP the files
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
