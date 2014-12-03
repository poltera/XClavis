/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import java.io.File;
import javafx.beans.property.ObjectProperty;
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
    
    public SelectedFile(File file, ImageView icon, String name, String extension, String size) {
        this.file = new SimpleObjectProperty<>(file);
        this.icon = new SimpleObjectProperty<>(icon);
        this.name = new SimpleStringProperty(name);
        this.extension = new SimpleStringProperty(extension);
        this.size = new SimpleStringProperty(size);
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
}
