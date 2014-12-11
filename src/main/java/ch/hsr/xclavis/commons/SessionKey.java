/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import ch.hsr.xclavis.crypto.RandomGenerator;

/**
 *
 * @author Gian
 */
public class SessionKey {

    public final static String SESSION_KEY_128 = "A";
    public final static String SESSION_KEY_256 = "B";
    public final static String ECDH_REQ_128 = "C";
    public final static String ECDH_REQ_256 = "D";
    public final static String ECDH_RES_128 = "E";
    public final static String ECDH_RES_256 = "F";

    private SessionID sessionID;
    private final byte[] sessionKey;
    private byte[] iv;

    public SessionKey(int length, String type) {
        this.sessionKey = RandomGenerator.getRandomBytes(length / 8);
        this.iv = RandomGenerator.getRandomBytes(12);
        this.sessionID = new SessionID(type, 15);
    }

    public SessionKey(byte[] sessionKey, SessionID sessionID) {
        this.sessionKey = sessionKey;
        this.sessionID = sessionID;
    }

    public byte[] getKey() {
        return sessionKey;
    }

    public byte[] getIV() {
        return iv;
    }

    public void setIV(byte[] iv) {
        this.iv = iv;
    }

    public String getID() {
        return sessionID.getID();
    }
    
    public String getType() {
        return sessionID.type;
    }
    
    public void setType(String type) {
        this.sessionID.setType(type);
    }
}
