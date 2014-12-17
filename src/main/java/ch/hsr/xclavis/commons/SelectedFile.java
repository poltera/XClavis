/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

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
