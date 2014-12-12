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
public class SessionID {

    public final static String SESSION_KEY_128 = "A";
    public final static String SESSION_KEY_256 = "B";
    public final static String ECDH_REQ_256 = "C";
    public final static String ECDH_REQ_512 = "D";
    public final static String ECDH_RES_256 = "E";
    public final static String ECDH_RES_512 = "F";
    
    private final static int RANDOM_BITS = 15;

    private String type;
    private String random;

    public SessionID(String type) {
        this.type = type;
        this.random = FormatTransformer.bitStringToBase32(RandomGenerator.getRandomBits(RANDOM_BITS));
        //CHECK IF EXISTS TBA
    }
    
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
    
    public int getKeyLength() {
        int keyLength = 0;
        switch (type) {
            case SESSION_KEY_128:
                keyLength = 128;
                break;
            case SESSION_KEY_256:
            case ECDH_REQ_256:
            case ECDH_RES_256:
                keyLength = 256;
                break;
            case ECDH_REQ_512:
            case ECDH_RES_512:
                keyLength = 512;
                break;
        }
        
        return keyLength;
    }
    
    public boolean isECDHReq() {        
        if ((getType().equals(ECDH_REQ_256)) || (getType().equals(ECDH_REQ_512))) {
            return true;
        }
        
        return false;
    }
}
