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

import java.io.File;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.scene.image.ImageView;

/**
 * This class represents a selected file of a user.
 * 
 * @author Gian Poltéra
 */
public class SelectedFile {

    private final ObjectProperty<File> file;
    private final ObjectProperty<ImageView> icon;
    private final StringProperty name;
    private final StringProperty extension;
    private final StringProperty size;
    private final BooleanProperty encrypted;
    private final StringProperty id;
    private final byte[] iv;

    /**
     * Creates a new SelectedFile that is not encrypted.
     * 
     * @param file the file
     * @param icon the icon of the file
     * @param name the name of the file
     * @param extension the extension of the file
     * @param size the size of the file
     * @param encrypted true, for encrypted or false otherwise
     */
    public SelectedFile(File file, ImageView icon, String name, String extension, String size, boolean encrypted) {
        this.file = new SimpleObjectProperty<>(file);
        this.icon = new SimpleObjectProperty<>(icon);
        this.name = new SimpleStringProperty(name);
        this.extension = new SimpleStringProperty(extension);
        this.size = new SimpleStringProperty(size);
        this.encrypted = new SimpleBooleanProperty(encrypted);
        this.id = new SimpleStringProperty("");
        this.iv = null;
    }

    /**
     * Creates a new SelectedFile that is encrypted.
     * 
     * @param file the file
     * @param icon the icon of the file
     * @param name the name of the file
     * @param extension the extension of the file
     * @param size the size of the file
     * @param encrypted true, for encrypted or false otherwise
     * @param id the SessionID of the encrypted file
     * @param iv the initialvector of the encrpyted file
     */
    public SelectedFile(File file, ImageView icon, String name, String extension, String size, boolean encrypted, String id, byte[] iv) {
        this.file = new SimpleObjectProperty<>(file);
        this.icon = new SimpleObjectProperty<>(icon);
        this.name = new SimpleStringProperty(name);
        this.extension = new SimpleStringProperty(extension);
        this.size = new SimpleStringProperty(size);
        this.encrypted = new SimpleBooleanProperty(encrypted);
        this.id = new SimpleStringProperty(id);
        this.iv = iv;
    }

    /**
     * Gets the file.
     * 
     * @return the file
     */
    public File getFile() {
        return file.get();
    }

    /**
     * Gets the icon of the file.
     * 
     * @return the icon as ImageView
     */
    public ImageView getIcon() {
        return icon.get();
    }

    /**
     * Gets the icon of the file.
     * 
     * @return the icon as ObjectProperty
     */
    public ObjectProperty iconProperty() {
        return icon;
    }

    /**
     * Gets the name of the file.
     * 
     * @return the name as String
     */
    public String getName() {
        return name.get();
    }

    /**
     * Gets the name of the file.
     * 
     * @return the name as StringProperty
     */
    public StringProperty nameProperty() {
        return name;
    }

    /**
     * Gets the extension of the file.
     * 
     * @return the extension as String
     */
    public String getExtension() {
        return extension.get();
    }

    /**
     * Gets the extension of the file.
     * 
     * @return the extension as StringProperty
     */
    public StringProperty extensionProperty() {
        return extension;
    }

    /**
     * Gets the size of the file.
     * 
     * @return the size as String
     */
    public String getSize() {
        return size.get();
    }

    /**
     * Gets the size of the file.
     * 
     * @return the size as StringProperty
     */
    public StringProperty sizeProperty() {
        return size;
    }

    /**
     * Returns the boolean value, whether a file is encrypted.
     * 
     * @return true, if the file is encrypted or false otherwise
     */
    public boolean isEncrypted() {
        return encrypted.get();
    }

    /**
     * Returns the boolean value, whether a file is encrypted.
     * 
     * @return the cryption state as a BooleanProperty
     */
    public BooleanProperty encryptedProperty() {
        return encrypted;
    }

    /**
     * Gets the SessionID of a file.
     * 
     * @return the SessionID as a String
     */
    public String getID() {
        return id.get();
    }

    /**
     * Gets the SessionID of a file.
     * 
     * @return the SessionID as a StringProperty
     */
    public StringProperty idProperty() {
        return id;
    }

    /**
     * Gets the initialvector of a file.
     * 
     * @return the IV as a byte-array
     */
    public byte[] getIV() {
        return iv;
    }
}
