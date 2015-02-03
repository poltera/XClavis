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
 *
 * @author Gian
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
    //private final StringProperty iv;

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

    public File getFile() {
        return file.get();
    }

    public ImageView getIcon() {
        return icon.get();
    }

    public ObjectProperty iconProperty() {
        return icon;
    }

    public String getName() {
        return name.get();
    }

    public StringProperty nameProperty() {
        return name;
    }

    public String getExtension() {
        return extension.get();
    }

    public StringProperty extensionProperty() {
        return extension;
    }

    public String getSize() {
        return size.get();
    }

    public StringProperty sizeProperty() {
        return size;
    }

    public boolean isEncrypted() {
        return encrypted.get();
    }

    public BooleanProperty encryptedProperty() {
        return encrypted;
    }

    public String getID() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public byte[] getIV() {
        return iv;
    }
}
