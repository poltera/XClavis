/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Gian
 */
public class Keys {
    private List<SessionKey> sessionKeys;
    private List<ECDHKey> ecdhKeys;
    private List<Key> keys;

    public Keys() {
        this.sessionKeys = new ArrayList<>();
        this.ecdhKeys = new ArrayList<>();
        this.keys = new ArrayList<>();
    }

    public void addKey(SessionKey sessionKey) {
        sessionKeys.add(sessionKey);
        keys.add(new Key(sessionKey.getSessionID(), "", 0));
        
    }

    public void addKey(ECDHKey ecdhKey) {
        ecdhKeys.add(ecdhKey);
        keys.add(new Key(ecdhKey.getSessionID(), "", 0));
    }
    
    public List<SessionKey> getSessionKeys() {
        return sessionKeys;
    }
    
    public List<ECDHKey> getECDHKeys() {
        return ecdhKeys;
    }
    
    public List<Key> getKeys() {
        return keys;
    }
}
