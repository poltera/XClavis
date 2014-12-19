/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.files;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

/**
 *
 * @author Gian
 */
public class FileZipper {
    
    private byte[] buffer = new byte[2048];
    
    public FileZipper() {
    }
    
    public byte[] getZippedBytes(List<File> files, boolean compression) {
        byte[] result = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //BufferedOutputStream bos = new BufferedOutputStream(baos);
        ZipOutputStream zos = new ZipOutputStream(baos);
        zos.setComment("Created by XClavis");
        zos.setMethod(ZipOutputStream.DEFLATED);
        if (compression) {
            zos.setLevel(Deflater.DEFAULT_COMPRESSION);
        } else {
            zos.setLevel(Deflater.NO_COMPRESSION);
        }
                
        try {
            //Put each File in the ZipStream
            files.stream().forEach((file) -> {
                try {
                    zos.putNextEntry(new ZipEntry(file.getName()));
                    zos.write(Files.readAllBytes(Paths.get(file.getPath())));
                    zos.closeEntry();
                } catch (IOException ex) {
                    Logger.getLogger(FileZipper.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
        } finally {
            try {
                zos.close();
                result = baos.toByteArray();
                baos.close();
            } catch (IOException ex) {
                Logger.getLogger(FileZipper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return result;
    }
    
    public void getFilesFromZippedBytes(byte[] input, String output) {        
        try (ByteArrayInputStream bais = new ByteArrayInputStream(input);
                ZipInputStream zis = new ZipInputStream(bais)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                try (FileOutputStream fos = new FileOutputStream(output + File.separator + entry.getName())) {
                    int length = 0;
                    while ((length  = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, length);
                    }
                }
            }
        } catch(IOException ex) {
            Logger.getLogger(FileZipper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}