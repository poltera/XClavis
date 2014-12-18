/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.helpers;

import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.keys.SessionKey;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class KeyStoreHandler {
    private List<SessionKey> sessionKeys;
    private List<ECDHKey> ecdhKeys;
    
    public KeyStoreHandler() {
        this.sessionKeys = new ArrayList<>();
        this.ecdhKeys = new ArrayList<>();
    }
    
    public void addKey(SessionKey sessionKey) {
        sessionKeys.add(sessionKey);
    }
    
    public void addKey(ECDHKey ecdhKey) {
        ecdhKeys.add(ecdhKey);
    }
}
