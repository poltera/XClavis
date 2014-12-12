/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import ch.hsr.xclavis.crypto.RandomGenerator;
import ch.hsr.xclavis.helpers.FormatTransformer;

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
        this.sessionKey = RandomGenerator.getRandomBytes(this.sessionID.getKeyLength() / 8);
        this.iv = RandomGenerator.getRandomBytes(12);
    }

    public SessionKey(byte[] sessionKey, SessionID sessionID) {
        this.sessionID = sessionID;
        this.sessionKey = sessionKey;
        this.iv = RandomGenerator.getRandomBytes(12);
    }

    public SessionKey(String base32SessionKey, SessionID sessionID) {
        this.sessionID = sessionID;
        this.sessionKey = FormatTransformer.base32ToByte(base32SessionKey);
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

    public String getType() {
        return sessionID.getType();
    }

    public void setType(String type) {
        this.sessionID.setType(type);
    }
}
