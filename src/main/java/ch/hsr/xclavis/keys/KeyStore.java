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
package ch.hsr.xclavis.keys;

import ch.hsr.xclavis.crypto.AESGCM;
import ch.hsr.xclavis.helpers.Base32;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * This class handles and save the keys of the project.
 *
 * @author Gian Poltéra
 */
public class KeyStore {

    private static final String BASE_PATH = System.getProperty("user.home") + File.separator + ".xclavis" + File.separator;
    private final static String KEYSTORE_PATH = BASE_PATH + "keystore.keys";
    private final static String DELIMITER = "|";
    private byte[] keystoreKey = "LSKSMDIALKSNBWEI".getBytes();
    private final static byte[] KEYSTORE_IV = "LSJFGWWWSDSS".getBytes();
    private final static int DELIMITER_COUNT = 3;

    private AESGCM aes;
    private final ObservableList<Key> keys;

    /**
     * Creates a new KeyStore and load the keys without password from the
     * harrdisk.
     */
    public KeyStore() {
        this.aes = new AESGCM(keystoreKey, KEYSTORE_IV);
        this.keys = FXCollections.observableArrayList();

        if (isPasswordCorrect()) {
            loadKeys();
        }
    }

    /**
     * Creates a new KeyStore and load the keys with password from the harrdisk.
     *
     * @param password the password for the KeyStore stored on the harddisk
     */
    public KeyStore(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            this.keystoreKey = digest.digest(password.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException | NoSuchAlgorithmException ex) {
            Logger.getLogger(KeyStore.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.aes = new AESGCM(keystoreKey, KEYSTORE_IV);
        this.keys = FXCollections.observableArrayList();

        if (isPasswordCorrect()) {
            loadKeys();
        }
    }

    /**
     * Gets the key list from the KeyStore.
     *
     * @return the key list as a ObseravableList
     */
    public ObservableList<Key> getObservableKeyList() {
        return keys;
    }

    /**
     * Adds a key to the KeyStore.
     *
     * @param key the key to be added
     */
    public void add(Key key) {
        if (!existsKey(key.getSessionID())) {
            keys.add(key);
            saveKeys();
        }
    }

    /**
     * Adds a list of keys to the KeyStore.
     *
     * @param keys the keys to be added
     */
    public void add(List<Key> keys) {
        keys.stream().forEach((key) -> {
            this.keys.add(key);
        });
        saveKeys();
    }

    /**
     * Replace an existing Key in the KeyStore.
     * 
     * @param key the new key to which the old replaced
     */
    public void replace(Key key) {
        if (existsKey(key.getSessionID())) {
            remove(getKey(key.getSessionID()));
            add(key);
        }
    }

    /**
     * Removes a key from the KeyStore.
     *
     * @param key the key to be removed
     */
    public void remove(Key key) {
        keys.remove(key);

        saveKeys();
    }

    /**
     * Checks whether a key exists in the KeyStore.
     *
     * @param sessionID the SessionID to be searched
     * @return true, if the key exists or false otherwise
     */
    public boolean existsKey(SessionID sessionID) {
        if (keys.stream().anyMatch((key) -> (key.getSessionID().getID().equals(sessionID.getID())))) {
            return true;
        }

        return false;
    }

    /**
     * Gets the Key with the specified SessionID.
     * 
     * @param sessionID the SessionID for which the Key to be returned
     * @return the Key
     */
    public Key getKey(SessionID sessionID) {
        for (Key key : keys) {
            if (key.getSessionID().getID().equals(sessionID.getID())) {
                return key;
            }
        }

        return null;
    }

    /**
     * Gets the SessionKey with the specified SessionID.
     *
     * @param sessionID the SessionID for which the SessionKey to be returned
     * @return the SessionKey
     */
    public SessionKey getSessionKey(SessionID sessionID) {
        return (SessionKey) getKey(sessionID);
    }

    /**
     * Gets all SessionKey's as a list.
     *
     * @return the SessionKey's a List
     */
    public List<SessionKey> getSessionKeys() {
        List<SessionKey> sessionKeys = new ArrayList<>();
        keys.stream().filter((key) -> (key.getSessionID().isSessionKey())).forEach((key) -> {
            sessionKeys.add((SessionKey) key);
        });

        return sessionKeys;
    }

    /**
     * Gets the ECDHKey with the specified SessionID.
     *
     * @param sessionID the SessionID for which the SessionKey to be returned
     * @return the ECDHKey
     */
    public ECDHKey getECDHKey(SessionID sessionID) {
        return (ECDHKey) getKey(sessionID);
    }

    /**
     * Gets the ECDHKey with the specified random value from the SessionID.
     *
     * @param random the ranom value for which the ECDHKey to be returned
     * @return the ECDHKey
     */
    public ECDHKey getECDHKey(String random) {
        for (Key key : keys) {
            if (key.getSessionID().getRandom().equals(random)) {
                return (ECDHKey) key;
            }
        }

        return null;
    }

    /**
     * Gets all ECDHKey's as a list.
     *
     * @return the ECDHKey's a List
     */
    public List<ECDHKey> getECDHKeys() {
        List<ECDHKey> ecdhKeys = new ArrayList<>();
        keys.stream().filter((key) -> (key.getSessionID().isECDH())).forEach((key) -> {
            ecdhKeys.add((ECDHKey) key);
        });

        return ecdhKeys;
    }

    /**
     * Checks if the password for KeyStore on the harddisk is correct.
     *
     * @return true, if the password is correct or false otherwise
     */
    public boolean isPasswordCorrect() {
        File base_path = new File(BASE_PATH);
        File file = new File(KEYSTORE_PATH);

        if (!base_path.exists()) {
            base_path.mkdir();
        }

        if (file.exists()) {
            return aes.isKeyCorrect(KEYSTORE_PATH);
        } else {
            saveKeys();
        }

        return true;
    }

    /**
     * Updates the password of the KeyStore.
     *
     * @param password the new password for the KeyStore
     */
    public void updatePassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            keystoreKey = digest.digest(password.getBytes("UTF-8"));
            aes = new AESGCM(keystoreKey, KEYSTORE_IV);
            saveKeys();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException ex) {
            Logger.getLogger(KeyStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void loadKeys() {
        File base_path = new File(BASE_PATH);
        File file = new File(KEYSTORE_PATH);

        if (!base_path.exists()) {
            base_path.mkdir();
        }

        if (file.exists()) {
            byte[] byteKeys = aes.decryptKeyStore(KEYSTORE_PATH);

            // Separate Keys
            String encodedKeys = "";
            for (byte b : byteKeys) {
                encodedKeys += (char) b;
            }
            String[] splittedKeys = encodedKeys.split("\\" + DELIMITER + "\\" + DELIMITER + "\\" + DELIMITER);

            // Separate Key
            for (String key : splittedKeys) {
                if (key.length() < 10) {
                    break;
                }
                String[] splittedKey = key.split("\\" + DELIMITER);

                SessionID sessionID = new SessionID(splittedKey[0].substring(0, 1), splittedKey[0].substring(1));
                if (sessionID.isSessionKey()) {
                    SessionKey sessionKey = new SessionKey(sessionID, splittedKey[6]);
                    sessionKey.setCreationDate(splittedKey[1]);
                    sessionKey.setLastUseDate(splittedKey[2]);
                    sessionKey.setLastActivity(splittedKey[3]);
                    sessionKey.setPartner(splittedKey[4]);
                    sessionKey.setState(splittedKey[5]);
                    keys.add(sessionKey);
                } else if (sessionID.isECDH()) {
                    ECDHKey ecdhKey = new ECDHKey(sessionID, splittedKey[6], splittedKey[7]);
                    ecdhKey.setCreationDate(splittedKey[1]);
                    ecdhKey.setLastUseDate(splittedKey[2]);
                    ecdhKey.setLastActivity(splittedKey[3]);
                    ecdhKey.setPartner(splittedKey[4]);
                    ecdhKey.setState(splittedKey[5]);
                    keys.add(ecdhKey);
                } else if (sessionID.isPrivaSphereKey()) {
                    PrivaSphereKey privaSphereKey = new PrivaSphereKey(sessionID, splittedKey[6]);
                    privaSphereKey.setCreationDate(splittedKey[1]);
                    privaSphereKey.setLastUseDate(splittedKey[2]);
                    privaSphereKey.setLastActivity(splittedKey[3]);
                    privaSphereKey.setPartner(splittedKey[4]);
                    privaSphereKey.setState(splittedKey[5]);
                    keys.add(privaSphereKey);
                }
            }

        }
    }

    private void saveKeys() {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            for (Key key : keys) {
                baos.write(key.getID().getBytes());
                baos.write(DELIMITER.getBytes());
                baos.write(key.getCreationDate().getBytes());
                baos.write(DELIMITER.getBytes());
                baos.write(key.getLastUseDate().getBytes());
                baos.write(DELIMITER.getBytes());
                baos.write(key.getLastActivity().getBytes());
                baos.write(DELIMITER.getBytes());
                baos.write(key.getPartner().getBytes());
                baos.write(DELIMITER.getBytes());
                baos.write(key.getState().getBytes());
                baos.write(DELIMITER.getBytes());
                if (key.getSessionID().isSessionKey()) {
                    SessionKey sessionKey = (SessionKey) key;
                    baos.write(Base32.byteToBase32(sessionKey.getKey()).getBytes());
                    for (int i = 0; i < DELIMITER_COUNT; i++) {
                        baos.write(DELIMITER.getBytes());
                    }
                } else if (key.getSessionID().isECDH()) {
                    ECDHKey ecdhKey = (ECDHKey) key;
                    baos.write(Base32.byteToBase32(ecdhKey.getPrivateKey()).getBytes());
                    baos.write(DELIMITER.getBytes());
                    baos.write(Base32.byteToBase32(ecdhKey.getPublicKey()).getBytes());
                    for (int i = 0; i < DELIMITER_COUNT; i++) {
                        baos.write(DELIMITER.getBytes());
                    }
                } else if (key.getSessionID().isPrivaSphereKey()) {
                    PrivaSphereKey privaSphereKey = (PrivaSphereKey) key;
                    baos.write(privaSphereKey.getKey().getBytes());
                    for (int i = 0; i < DELIMITER_COUNT; i++) {
                        baos.write(DELIMITER.getBytes());
                    }
                }
            }
            aes.encryptKeyStore(baos.toByteArray(), KEYSTORE_PATH);

        } catch (IOException ex) {
            Logger.getLogger(KeyStore.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
