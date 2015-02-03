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

import ch.hsr.xclavis.crypto.RandomGenerator;
import ch.hsr.xclavis.helpers.Base32;

/**
 * This class represents a Session-Key and is a subclass of Key.
 * 
 * @author Gian Poltéra
 */
public class SessionKey extends Key {

    private final byte[] sessionKey;
    private byte[] iv;

    /**
     * Creates a complete new SessionKey.
     *
     * @param type the type of the key to be created
     */
    public SessionKey(String type) {
        super(new SessionID(type));
        this.sessionKey = RandomGenerator.getRandomBytes(getSessionID().getFinalKeyLength() / Byte.SIZE);
        this.iv = RandomGenerator.getRandomBytes(12);
    }

    /**
     * Creates a new SessionKey with given SessionID and Key.
     * Is used from the ECDHKey to create a SessionKey.
     * 
     * @param sessionID the SessionId of the key
     * @param sessionKey the SessionKey as a byte-array
     */
    public SessionKey(SessionID sessionID, byte[] sessionKey) {
        super(sessionID);
        this.sessionKey = sessionKey;
        this.iv = RandomGenerator.getRandomBytes(12);
    }

    /**
     * Creates a new SessionKey with given SessionID and base32 Key.
     * Is used in the load from a QR-Code and the KeyStore.
     *
     * @param sessionID the SessionId of the key
     * @param base32SessionKey the SessionKey as a base32 string
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

    /**
     * Gets the Key.
     * 
     * @return the key as a byte-array.
     */
    public byte[] getKey() {
        return sessionKey;
    }

    /**
     * Gets the initialvector.
     * 
     * @return the IV as a byte-array.
     */
    public byte[] getIV() {
        return iv;
    }

    /**
     * Sets the initialvector.
     * 
     * @param iv the initialvector
     */
    public void setIV(byte[] iv) {
        this.iv = iv;
    }
}
