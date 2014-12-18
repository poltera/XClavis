/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.keys;

import ch.hsr.xclavis.crypto.AESGCM;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Gian
 */
public class KeyStore {

    //private final static String KEYSTORE_PATH = System.getProperty("user.home") + File.separator + ".xclavis" + File.separator + "keystore" + File.separator + "keystore.keys";
    private final static String KEYSTORE_PATH = System.getProperty("user.home") + File.separator + "keystore.keys";
    private final static String DELIMITER = "|";
    private final static byte[] KEYSTORE_KEY = "LSKSMDIALKSNBWEI".getBytes();
    private final static byte[] KEYSTORE_IV = "LSJFGWWWSDSS".getBytes();
    private final static int DELIMITER_COUNT = 3;
    private final static int ID_LENGTH = 4;

    private final AESGCM aes;
    private final ObservableList<Key> keys;

    public KeyStore() {
        this.aes = new AESGCM(KEYSTORE_KEY, KEYSTORE_IV);
        this.keys = FXCollections.observableArrayList();
        loadKeys();
    }

    public ObservableList<Key> getObservableKeyList() {
        return keys;
    }

    public void add(Key key) {
        keys.add(key);
        saveKeys();
    }

    public void add(List<Key> keys) {
        keys.stream().forEach((key) -> {
            this.keys.add(key);
        });
        saveKeys();
    }

    public boolean existsKey(SessionID sessionID) {
        if (keys.stream().anyMatch((key) -> (key.getSessionID().getID().equals(sessionID.getID())))) {
            return true;
        }

        return false;
    }

    public SessionKey getSessionKey(SessionID sessionID) {
        for (Key key : keys) {
            if (key.getSessionID().getID().equals(sessionID.getID())) {
                return (SessionKey) key;
            }
        }

        return null;
    }

    public List<SessionKey> getSessionKeys() {
        List<SessionKey> sessionKeys = new ArrayList<>();
        keys.stream().filter((key) -> (key.getSessionID().isSessionKey())).forEach((key) -> {
            sessionKeys.add((SessionKey) key);
        });

        return sessionKeys;
    }

    /**
     * Gives the ECDHKey with the specificated sessionID.
     *
     * @param sessionID
     * @return ecdhKey
     */
    public ECDHKey getECDHKey(SessionID sessionID) {
        for (Key key : keys) {
            if (key.getSessionID().getID().equals(sessionID.getID())) {
                return (ECDHKey) key;
            }
        }

        return null;
    }

    public List<ECDHKey> getECDHKeys() {
        List<ECDHKey> ecdhKeys = new ArrayList<>();
        keys.stream().filter((key) -> (key.getSessionID().isECDH())).forEach((key) -> {
            ecdhKeys.add((ECDHKey) key);
        });

        return ecdhKeys;
    }

    /**
     * Removes a key from the KeyStore.
     *
     * @param key
     */
    public void remove(Key key) {
        keys.remove(key);

        saveKeys();
    }

    public void saveKeys() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (Key key : keys) {
                if (key.getSessionID().isSessionKey()) {
                    SessionKey sessionKey = (SessionKey) key;
                    baos.write(sessionKey.getID().getBytes());
                    baos.write(sessionKey.getKey());
                    for (int i = 0; i < DELIMITER_COUNT; i++) {
                        baos.write(DELIMITER.getBytes());
                    }
                } else if (key.getSessionID().isECDH()) {
                    ECDHKey ecdhKey = (ECDHKey) key;
                    baos.write(ecdhKey.getID().getBytes());
                    baos.write(ecdhKey.getPrivateKey());
                    baos.write(ecdhKey.getPublicKey());
                    for (int i = 0; i < DELIMITER_COUNT; i++) {
                        baos.write(DELIMITER.getBytes());
                    }
                }
            }
            aes.encrypt2(baos.toByteArray(), KEYSTORE_PATH);

        } catch (IOException ex) {
            Logger.getLogger(KeyStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadKeys() {
        File file = new File(KEYSTORE_PATH);
        if (file.exists()) {
            byte[] keysBytes = aes.decryptToByteStream(KEYSTORE_PATH);
            int delimiters = 0;
            int length = 0;

            for (int i = 0; i < keysBytes.length; i++) {
                length++;
                if (keysBytes[i] == DELIMITER.getBytes()[0]) {
                    delimiters++;
                    if (delimiters == DELIMITER_COUNT) {
                        int keyLenght = length - DELIMITER_COUNT - ID_LENGTH;

                        byte[] idBytes = new byte[4];
                        byte[] keyBytes = new byte[keyLenght];

                        // src, src pos, dest, dest pos, length
                        System.arraycopy(keysBytes, i - (length - 1), idBytes, 0, ID_LENGTH);
                        System.arraycopy(keysBytes, i - (length - 1) + ID_LENGTH, keyBytes, 0, keyLenght);

                        String id = "";
                        for (byte b : idBytes) {
                            id += (char) b;
                        }
                        SessionID sessionID = new SessionID(id.substring(0, 1), id.substring(1));

                        if (sessionID.isSessionKey()) {
                            SessionKey sessionKey = new SessionKey(sessionID, keyBytes);
                            keys.add(sessionKey);
                        } else if (sessionID.isECDH()) {
                            int privateKeyLength = (keyBytes.length - 1) / 2;
                            int publicKeyLength = ((keyBytes.length - 1) / 2) + 1;
                            byte[] privateKey = new byte[privateKeyLength];
                            byte[] publicKey = new byte[publicKeyLength];
                            // src, src pos, dest, dest pos, length
                            System.arraycopy(keyBytes, 0, privateKey, 0, privateKeyLength);
                            System.arraycopy(keyBytes, privateKeyLength, publicKey, 0, publicKeyLength);
                            ECDHKey ecdhKey = new ECDHKey(sessionID, privateKey, publicKey);
                            keys.add(ecdhKey);
                        }

                        delimiters = 0;
                        length = 0;
                    }
                } else {
                    delimiters = 0;
                }
            }
        }
    }
}
