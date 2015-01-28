/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 * @author Gian
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
