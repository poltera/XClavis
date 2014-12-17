/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import ch.hsr.xclavis.crypto.RandomGenerator;
import ch.hsr.xclavis.helpers.Base32;

/**
 *
 * @author Gian
 */
public class SessionKey {

    private SessionID sessionID;
    private final byte[] sessionKey;
    private byte[] iv;

    public SessionKey(String type) {
        this.sessionID = new SessionID(type);
        this.sessionKey = RandomGenerator.getRandomBytes(this.sessionID.getFinalKeyLength() / Byte.SIZE);
        this.iv = RandomGenerator.getRandomBytes(12);
    }

    public SessionKey(byte[] sessionKey, SessionID sessionID) {
        this.sessionID = sessionID;
        this.sessionKey = sessionKey;
        this.iv = RandomGenerator.getRandomBytes(12);
    }

    public SessionKey(String base32SessionKey, SessionID sessionID) {
        // Converting back a Base32 String to its Byte Value gives an additional Byte
        byte[] byteSessionKey = Base32.base32ToByte(base32SessionKey);
        // Trim the additional Byte
        byte[] trimmedSessionKey = new byte[byteSessionKey.length - 1];
        System.arraycopy(byteSessionKey, 0, trimmedSessionKey, 0, byteSessionKey.length - 1);
        this.sessionID = sessionID;
        this.sessionKey = trimmedSessionKey;
        this.iv = RandomGenerator.getRandomBytes(12);
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

    public SessionID getSessionID() {
        return sessionID;
    }

    public String getType() {
        return sessionID.getType();
    }

    public void setType(String type) {
        this.sessionID.setType(type);
    }
}
