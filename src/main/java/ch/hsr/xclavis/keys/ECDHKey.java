/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.keys;

import ch.hsr.xclavis.crypto.ECDH;
import ch.hsr.xclavis.crypto.HMACSHA;
import ch.hsr.xclavis.helpers.Base32;

/**
 *
 * @author Gian
 */
public class ECDHKey extends Key {

    public final static String ECDH_BRAINPOOL_256 = "brainpoolP256t1";
    public final static String ECDH_BRAINPOOL_512 = "brainpoolP512t1";

    private final ECDH ecdh;

    // ECDH Request
    public ECDHKey(String type) {
        super(new SessionID(type));
        this.ecdh = new ECDH(getCurve());
    }
    
    // ECDH Response
    public ECDHKey(SessionID sessionID) {
        super(sessionID);
        this.ecdh = new ECDH(getCurve());
        changeType();
    }

    // ECDH from KeyStore
    public ECDHKey(SessionID sessionID, byte[] privateKey, byte[] publicKey) {
        super(sessionID);
        this.ecdh = new ECDH(getCurve(), privateKey, publicKey);
    }
    
    public byte[] getPrivateKey() {
        return ecdh.getPrivateKey();
    }

    public byte[] getPublicKey() {        
        return ecdh.getPublicKey();
    }

    public SessionKey getSessionKey(byte[] publicKey) {
        byte[] agreedKey = ecdh.getAgreedKey(publicKey);
        HMACSHA hmacsha = new HMACSHA(getSessionID().getKeyLength());
        byte[] derivatedKey = hmacsha.getDerivatedKey(getSessionID().getFinalKeyLength(), agreedKey);
        changeType();
        SessionKey sessionKey = new SessionKey(getSessionID(), derivatedKey);

        return sessionKey;
    }

    public SessionKey getSessionKey(String base32PublicKey) {
        // Converting back a Base32 String to its Byte Value gives an additional Byte
        byte[] bytePublicKey = Base32.base32ToByte(base32PublicKey);
        // Trim the additional Byte
        byte[] trimmedPublicKey = new byte[bytePublicKey.length - 1];
        System.arraycopy(bytePublicKey, 0, trimmedPublicKey, 0, bytePublicKey.length - 1);
        byte[] agreedKey = ecdh.getAgreedKey(trimmedPublicKey);
        HMACSHA hmacsha = new HMACSHA(getSessionID().getKeyLength());
        byte[] derivatedKey = hmacsha.getDerivatedKey(getSessionID().getFinalKeyLength(), agreedKey);
        changeType();
        SessionKey sessionKey = new SessionKey(getSessionID(), derivatedKey);

        return sessionKey;
    }

    private String getCurve() {
        String type = getSessionID().getType();
        String curve = "";
        switch (type) {
            case SessionID.ECDH_REQ_256:
            case SessionID.ECDH_RES_256:
                curve = ECDH_BRAINPOOL_256;
                break;
            case SessionID.ECDH_REQ_512:
            case SessionID.ECDH_RES_512:
                curve = ECDH_BRAINPOOL_512;
                break;
        }

        return curve;
    }

    
    //EDIT!!!!!!!!!!!! TBA
    private void changeType() {
        String type = getSessionID().getType();
        switch (type) {
            case SessionID.ECDH_REQ_256:
                getSessionID().setType(SessionID.ECDH_RES_256);
                break;
            case SessionID.ECDH_REQ_512:
                getSessionID().setType(SessionID.ECDH_RES_512);
                break;
            case SessionID.ECDH_RES_256:
                getSessionID().setType(SessionID.SESSION_KEY_128);
                break;
            case SessionID.ECDH_RES_512:
                getSessionID().setType(SessionID.SESSION_KEY_256);
                break;
        }
    }
}
