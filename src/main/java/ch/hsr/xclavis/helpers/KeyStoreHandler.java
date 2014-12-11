/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.helpers;

import ch.hsr.xclavis.commons.SessionKey;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchProviderException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.SecretKey;

/**
 *
 * @author Gian
 */
public class KeyStoreHandler {
    private KeyStore keyStore;
    private KeyStore.ProtectionParameter protectionParameters;
    
    public KeyStoreHandler() {
        try {
            this.keyStore = KeyStore.getInstance("BKS", "BC");
            this.protectionParameters = new KeyStore.PasswordProtection("XClavis".toCharArray());
        } catch (KeyStoreException | NoSuchProviderException ex) {
            Logger.getLogger(KeyStoreHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void addKey(SessionKey sessionKey) {
        try {
            //SecretKey secretKey = new SecretKey();
            KeyStore.SecretKeyEntry secretKeyEntry = new KeyStore.SecretKeyEntry(null);
            keyStore.setEntry("test", secretKeyEntry, protectionParameters);
        } catch (KeyStoreException ex) {
            Logger.getLogger(KeyStoreHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
