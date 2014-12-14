/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import ch.hsr.xclavis.crypto.ECDH;
import ch.hsr.xclavis.crypto.HMACSHA;
import ch.hsr.xclavis.helpers.Base32;

/**
 *
 * @author Gian
 */
public class ECDHKey {

    public final static String ECDH_BRAINPOOL_256 = "brainpoolP256t1";
    public final static String ECDH_BRAINPOOL_512 = "brainpoolP512t1";

    private SessionID sessionID;
    private ECDH ecdh;
    //private KeyPair keyPair;
    //private ECPublicKey ecPublicKey;
    //private ECPrivateKey ecPrivateKey;

    // ECDH Request
    public ECDHKey(String type) {
        this.sessionID = new SessionID(type);
        this.ecdh = new ECDH(getCurve());
        //this.keyPair = this.ecdh.getKeyPair();
        //this.ecPublicKey = (ECPublicKey) this.keyPair.getPublic();
        //this.ecPrivateKey = (ECPrivateKey) this.keyPair.getPrivate();
    }

    // ECDH Response
    public ECDHKey(SessionID sessionID) {
        this.sessionID = sessionID;
        this.ecdh = new ECDH(getCurve());
        changeType();
    }

    public byte[] getPublicKey() {        
        return ecdh.getPublicKey();
    }

    public SessionKey getSessionKey(byte[] publicKey) {
        byte[] agreedKey = ecdh.getAgreedKey(publicKey);
        HMACSHA hmacsha = new HMACSHA(sessionID.getKeyLength());
        byte[] derivatedKey = hmacsha.getDerivatedKey(sessionID.getFinalKeyLength(), agreedKey);
        changeType();
        SessionKey sessionKey = new SessionKey(derivatedKey, sessionID);

        return sessionKey;
    }

    public SessionKey getSessionKey(String base32PublicKey) {
        // Converting back a Base32 String to its Byte Value gives an additional Byte
        byte[] bytePublicKey = Base32.base32ToByte(base32PublicKey);
        // Trim the additional Byte
        byte[] trimmedPublicKey = new byte[bytePublicKey.length - 1];
        System.arraycopy(bytePublicKey, 0, trimmedPublicKey, 0, bytePublicKey.length - 1);
        byte[] agreedKey = ecdh.getAgreedKey(trimmedPublicKey);
        HMACSHA hmacsha = new HMACSHA(sessionID.getKeyLength());
        byte[] derivatedKey = hmacsha.getDerivatedKey(sessionID.getFinalKeyLength(), agreedKey);
        changeType();
        SessionKey sessionKey = new SessionKey(derivatedKey, sessionID);

        return sessionKey;
    }

    public String getID() {
        return sessionID.getID();
    }

    private String getCurve() {
        String type = sessionID.getType();
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

    private void changeType() {
        String type = sessionID.getType();
        switch (type) {
            case SessionID.ECDH_REQ_256:
                sessionID.setType(SessionID.ECDH_RES_256);
                break;
            case SessionID.ECDH_REQ_512:
                sessionID.setType(SessionID.ECDH_RES_512);
                break;
            case SessionID.ECDH_RES_256:
                sessionID.setType(SessionID.SESSION_KEY_128);
                break;
            case SessionID.ECDH_RES_512:
                sessionID.setType(SessionID.SESSION_KEY_256);
                break;
        }
    }
}
