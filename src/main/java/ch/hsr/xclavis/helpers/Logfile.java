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
package ch.hsr.xclavis.helpers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gian
 */
public class Logfile {

    private static final String BASE_PATH = System.getProperty("user.home") + File.separator + ".xclavis" + File.separator;
    private final static String LOGFILE_PATH = BASE_PATH + "logfile.log";
    private final static String TITLE_DELIMITER = "----------------------------------------------------------------------";
    private final static String ENTRY_DELIMITER = "======================================================================";

    public static void addEntry(String entry) {
        checkIfExists();
        save("\r\n" + getActualTime() + " -> " + entry);
    }

    public static void addTitle(String title) {
        checkIfExists();
        save("\r\n" + "\r\n" + title + "\r\n" + TITLE_DELIMITER);
    }

    public static String getLast() {
        String full = getFull();
        String[] results = full.split(TITLE_DELIMITER);
        String result = results[results.length - 1];

        return result;
    }

    public static String getFull() {
        checkIfExists();
        String result = "";
        try (FileReader fileReader = new FileReader(LOGFILE_PATH);
                BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            String zeile = "";
            while ((zeile = bufferedReader.readLine()) != null) {
                result += zeile + "\n";
            }
        } catch (IOException ex) {
            Logger.getLogger(Logfile.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    private static String getActualTime() {
        LocalDateTime now = LocalDateTime.now();

        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    private static void checkIfExists() {
        if (!new File(LOGFILE_PATH).exists()) {
            save("XClavis Logfile" + "\r\n" + ENTRY_DELIMITER);
        }
    }

    private static void save(String entry) {
        try (FileWriter fileWriter = new FileWriter(LOGFILE_PATH, true)) {
            fileWriter.write(entry);
        } catch (IOException ex) {
            Logger.getLogger(Logfile.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
