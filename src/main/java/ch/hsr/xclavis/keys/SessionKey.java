/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.keys;

import ch.hsr.xclavis.crypto.RandomGenerator;
import ch.hsr.xclavis.helpers.Base32;

/**
 *
 * @author Gian
 */
public class SessionKey extends Key {

    private final byte[] sessionKey;
    private byte[] iv;

    /**
     * Creates a new SessionKey.
     *
     * @param type
     */
    public SessionKey(String type) {
        super(new SessionID(type));
        this.sessionKey = RandomGenerator.getRandomBytes(getSessionID().getFinalKeyLength() / Byte.SIZE);
        this.iv = RandomGenerator.getRandomBytes(12);
    }

    /**
     * Creates a SessionKey with given Key and SessionID.
     *
     * @param sessionID
     * @param sessionKey
     */
    public SessionKey(SessionID sessionID, byte[] sessionKey) {
        super(sessionID);
        this.sessionKey = sessionKey;
        this.iv = RandomGenerator.getRandomBytes(12);
    }

    /**
     * Creates a SessionKey with given Base32 Key and SessionID.
     *
     * @param sessionID
     * @param base32SessionKey
     */
    public SessionKey(SessionID sessionID, String base32SessionKey) {
        super(sessionID);
        // Converting back a Base32 String to its Byte Value gives an additional Byte
        byte[] byteSessionKey = Base32.base32ToByte(base32SessionKey);
        // Trim the additional Byte
        byte[] trimmedSessionKey = new byte[byteSessionKey.length - 1];
        System.arraycopy(byteSessionKey, 0, trimmedSessionKey, 0, byteSessionKey.length - 1);
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
}
