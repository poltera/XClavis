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

import ch.hsr.xclavis.helpers.Logfile;
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
 * This class zips and dezips files.
 *
 * @author Gian Poltéra
 */
public class FileZipper {

    private byte[] buffer = new byte[2048];

    /**
     * Gets zipped-bytes from a filelist.
     *
     * @param files the file list to zip
     * @param compression true, for activate or false for deactivate compression
     * @return the zipped-byted as byte-array
     */
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
                    Logfile.addEntry(file.getName() + " zipped");
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

    /**
     * Dezip a file and write the dezipped files to a specific output.
     *
     * @param input the byte-array of the zipped-file
     * @param output the output-path for the dezipped-files
     */
    public void getFilesFromZippedBytes(byte[] input, String output) {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(input);
                ZipInputStream zis = new ZipInputStream(bais)) {
            int i = 0;
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                String filename = output + File.separator + entry.getName();
                if (checkOverwriteFile(filename)) {
                    try (FileOutputStream fos = new FileOutputStream(filename)) {
                        i++;
                        int length = 0;
                        while ((length = zis.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                        Logfile.addEntry(entry.getName() + " dezipped to " + filename);
                    }
                } else {
                    Logfile.addEntry(entry.getName() + " already exists, file skipped");
                }
            }
            Logfile.addEntry(i + " files results from the decryption");
        } catch (IOException ex) {
            Logger.getLogger(FileZipper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean checkOverwriteFile(String filename) {
        File file = new File(filename);
        if (file.exists()) {
            return false;
        } else {
            return true;
        }
    }
}
