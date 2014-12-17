/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.helpers;

import ch.hsr.xclavis.commons.SelectedFile;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.ImageIcon;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author Gian
 */
public class FileHandler {

    private final static String ENCRYPTED_FILE_EXTENSION = "enc";

    public void loadFile(File file, ObservableList<SelectedFile> fileData) {
        if (!file.isDirectory() && file.isFile() && file.canRead()) {
            boolean exists = false;
            for (SelectedFile existingFile : fileData) {
                if (existingFile.getFile().equals(file)) {
                    exists = true;
                    break;
                }
            }            
            if (!exists) {
                String fileName = getName(file);
                String fileSize = getSize(file);
                String fileExtension = getExtension(file);
                boolean fileEncrypted = isEncrypted(file);
                
                if (!isEncrypted(file)) {
                    ImageView fileIcon = getIcon(file);
                    SelectedFile selectedFile = new SelectedFile(file, fileIcon, fileName, fileExtension, fileSize, fileEncrypted);
                    fileData.add(selectedFile);
                } else {
                    ImageView fileIcon = new ImageView(new Image(getClass().getResourceAsStream("/images/encrypted_icon.png")));
                    String fileID = getID(file);
                    byte[] fileIV = getIV(file);
                    SelectedFile selectedFile = new SelectedFile(file, fileIcon, fileName, fileExtension, fileSize, fileEncrypted, fileID, fileIV);
                    fileData.add(selectedFile);
                }
            }
        }
    }

    private static ImageView getIcon(File file) {
        ImageIcon iconSWT = (ImageIcon) FileSystemView.getFileSystemView().getSystemIcon(file); //16x16 Icon
        BufferedImage bufferedImage = new BufferedImage(iconSWT.getIconWidth(), iconSWT.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
        bufferedImage.getGraphics().drawImage(iconSWT.getImage(), 0, 0, iconSWT.getImageObserver());
        Image iconFX = SwingFXUtils.toFXImage(bufferedImage, null);
        ImageView imageView = new ImageView();
        imageView.setImage(iconFX);

        return imageView;
    }

    private static String getSize(File file) {
        double bytesize = (double) file.length();
        NumberFormat n = NumberFormat.getInstance();
        n.setMaximumFractionDigits(2);
        String size = bytesize + " Byte";
        if (bytesize >= 1024) {
            size = n.format(bytesize / 1024) + " KB";
        }
        if (bytesize >= 1024 * 1024) {
            size = n.format(bytesize / (1024 * 1024)) + " MB";
        }

        if (bytesize >= 1024 * 1024 * 1024) {
            size = n.format(bytesize / (1024 * 1024 * 1024)) + " GB";
        }

        return size;
    }

    private static String getName(File file) {
        String fileName = file.getName().substring(0, file.getName().lastIndexOf('.'));

        if (fileName.length() > 60) {
            fileName = fileName.substring(0, 60);
            fileName += "...";
        }

        return fileName;
    }

    private static String getExtension(File file) {
        String fileExtension = file.getName().substring(file.getName().lastIndexOf('.'));
        fileExtension = fileExtension.substring(1);

        return fileExtension;
    }

    private static boolean isEncrypted(File file) {
        String fileExtension = getExtension(file);

        return fileExtension.equals(ENCRYPTED_FILE_EXTENSION);
    }

    private static String getID(File file) {
        String id = "";
        if (isEncrypted(file)) {
            //4 Bytes ID
            try (FileInputStream fis = new FileInputStream(file);
                    DataInputStream dis = new DataInputStream(fis)) {
                for (int i = 0; i < 4; i++) {
                    id += (char) dis.readByte();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        return id;
    }

    private static byte[] getIV(File file) {
        byte[] iv = new byte[16];
        if (isEncrypted(file)) {
            //12 Bytes IV
            try (FileInputStream fis = new FileInputStream(file);
                    DataInputStream dis = new DataInputStream(fis)) {
                dis.readByte();
                dis.readByte();
                dis.readByte();
                dis.readByte();
                for (int i = 0; i < 12; i++) {
                    iv[i] = dis.readByte();
                }
            } catch (IOException ex) {
                Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return iv;
    }

    public static Properties loadProperties() {
        Properties properties = new Properties();
        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream("beispiel.properties"))) {
            properties.load(bis);
        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return properties;
    }

    public static void saveProperties(Properties properties, String path) {
        try (BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(""))) {

        } catch (IOException ex) {
            Logger.getLogger(FileHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static boolean checkWritePermissions(String path) {
        File file = new File(path);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            for (int i = 0; i < 10000; i++) {
                byte[] byteTest = Integer.toString(i).getBytes();
                fos.write(byteTest);
            }
            return true;
        } catch (IOException ex) {
            return false;
        } finally {
            file.delete();
        }
    }

    //tba
    public static boolean checkFileExists(String path) {
        return false;
    }
}
