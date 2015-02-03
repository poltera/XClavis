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

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gian Poltéra
 */
public class PropertiesHandler {

    private Properties properties;
    private static final String BASE_PATH = System.getProperty("user.home") + File.separator + ".xclavis" + File.separator;
    private static final String CONFIG_PATH = BASE_PATH + "config.properties";
    private static final String DEFAULT_CONFIG = "/config/default.properties";
    private final String regexDecimal = "^-?\\d*\\.\\d+$";
    private final String regexInteger = "^-?\\d+$";
    private String regexDouble = regexDecimal + "|" + regexInteger;

    public PropertiesHandler() {
        properties = new Properties();
        File base_path = new File(BASE_PATH);
        File file = new File(CONFIG_PATH);
        
        if (!base_path.exists()) {
            base_path.mkdir();
        }
        
        if (file.exists()) {
            loadExisting();
        } else {
            loadDefault();
        }
    }

    public String getString(String name) {
        return properties.getProperty(name);
    }

    public int getInteger(String name) {
        String property = properties.getProperty(name);

        if (isInteger(property)) {
            return Integer.parseInt(property);
        } else {
            return 0;
        }
    }

    public double getDouble(String name) {
        String property = properties.getProperty(name);

        if (isDouble(property)) {
            return Double.parseDouble(property);
        } else {
            return 0;
        }
    }
    
    public boolean getBoolean(String name) {
        String property = properties.getProperty(name);
        
        switch (property) {
            case "true":
                return true;
            case "false":
                return false;
        }
        
        return false;
    }

    public void set(String name, String value) {
        properties.setProperty(name, value);
        save();
    }

    public void set(String name, int value) {
        properties.setProperty(name, Integer.toString(value));
        save();
    }

    public void set(String name, double value) {
        properties.setProperty(name, Double.toString(value));
        save();
    }
    
    public void set(String name, boolean value) {
        properties.setProperty(name, Boolean.toString(value));
        save();
    }

    public void remove(String name) {
        properties.remove(name);
        save();
    }

    private void loadDefault() {
        try {
            InputStream source = getClass().getResourceAsStream(DEFAULT_CONFIG);
            File destination = new File(CONFIG_PATH);
            
            Files.copy(source, destination.toPath(), REPLACE_EXISTING);

        } catch (IOException ex) {
            Logger.getLogger(PropertiesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        loadExisting();
        save();
    }

    private void loadExisting() {
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(CONFIG_PATH)))) {
            properties.load(bis);

        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void save() {
        try {
            OutputStream os = new FileOutputStream(new File(CONFIG_PATH));
            properties.store(os, "XClavis Config");
        } catch (FileNotFoundException ex) {
            Logger.getLogger(PropertiesHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PropertiesHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isInteger(String value) {
        return value.matches(regexInteger);
    }

    private boolean isDouble(String value) {
        return value.matches(regexDouble);
    }
}
