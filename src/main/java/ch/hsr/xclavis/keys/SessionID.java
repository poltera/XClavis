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
 *
 * @author Gian Poltéra
 */
public class SessionID {

    public final static String SESSION_KEY_128 = "A";
    public final static String SESSION_KEY_256 = "B";
    public final static String ECDH_REQ_256 = "C";
    public final static String ECDH_REQ_512 = "D";
    public final static String ECDH_RES_256 = "E";
    public final static String ECDH_RES_512 = "F";
    public final static int KEY_128_SIZE = 128;
    public final static int KEY_256_SIZE = 256;
    public final static int KEY_512_SIZE = 512;
    private final static int RANDOM_BITS = 15;

    private String type;
    private String random;

    /**
     *
     * @param type
     */
    public SessionID(String type) {
        this.type = type;
        this.random = Base32.bitStringToBase32(RandomGenerator.getRandomBits(RANDOM_BITS));
        //CHECK IF EXISTS TBA
    }

    /**
     *
     * @param type
     * @param random
     */
    public SessionID(String type, String random) {
        this.type = type;
        this.random = random;
    }

    public String getID() {
        return type + random;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRandom() {
        return random;
    }

    public int getKeyLength() {
        switch (getType()) {
            case SESSION_KEY_128:
                return KEY_128_SIZE;
            case SESSION_KEY_256:
            case ECDH_REQ_256:
            case ECDH_RES_256:
                return KEY_256_SIZE;
            case ECDH_REQ_512:
            case ECDH_RES_512:
                return KEY_512_SIZE;
        }

        return 0;
    }

    public int getFinalKeyLength() {
        switch (type) {
            case SESSION_KEY_128:
            case ECDH_REQ_256:
            case ECDH_RES_256:
                return KEY_128_SIZE;
            case SESSION_KEY_256:
            case ECDH_REQ_512:
            case ECDH_RES_512:
                return KEY_256_SIZE;
        }

        return 0;
    }

    public String getNextType() {
        switch (type) {
            case ECDH_REQ_256:
                return ECDH_RES_256;
            case ECDH_REQ_512:
                return ECDH_RES_512;
            case ECDH_RES_256:
                return SESSION_KEY_128;
            case ECDH_RES_512:
                return SESSION_KEY_256;
        }

        return null;
    }

    public String getFinalType() {
        switch (type) {
            case SESSION_KEY_128:
                return SESSION_KEY_128;
            case SESSION_KEY_256:
                return SESSION_KEY_256;            
            case ECDH_REQ_256:
                return SESSION_KEY_128;
            case ECDH_REQ_512:
                return SESSION_KEY_256;
            case ECDH_RES_256:
                return SESSION_KEY_128;
            case ECDH_RES_512:
                return SESSION_KEY_256;
        }

        return null;
    }

    public int getAddCordLength() {
        if (isECDH()) {
            return Byte.SIZE;
        }

        return 0;
    }

    public boolean isECDHReq() {
        return (getType().equals(ECDH_REQ_256)) || (getType().equals(ECDH_REQ_512));
    }

    public boolean isECDHRes() {
        return (getType().equals(ECDH_RES_256)) || (getType().equals(ECDH_RES_512));
    }

    public boolean isECDH() {
        return (getType().equals(ECDH_REQ_256)) || (getType().equals(ECDH_REQ_512)) || (getType().equals(ECDH_RES_256)) || (getType().equals(ECDH_RES_512));
    }

    public boolean isSessionKey() {
        return (getType().equals(SESSION_KEY_128)) || (getType().equals(SESSION_KEY_256));
    }
}
