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
        changeTypeToECDHRes();
    }

    // ECDH from KeyStore
    public ECDHKey(SessionID sessionID, String base32PrivateKey, String base32PublicKey) {
        super(sessionID);
        // Converting back a Base32 String to its Byte Value gives in some cases an additional Byte
        byte[] bytePrivateKey = Base32.base32ToByte(base32PrivateKey);
        byte[] bytePublicKey = Base32.base32ToByte(base32PublicKey);

        // Trim the additional Byte
        byte[] trimmedPrivateKey = new byte[bytePrivateKey.length - 1];
        System.arraycopy(bytePrivateKey, 0, trimmedPrivateKey, 0, bytePrivateKey.length - 1);

        if (bytePublicKey.length != 65) {
            byte[] trimmedPublicKey = new byte[bytePublicKey.length - 1];
            System.arraycopy(bytePublicKey, 0, trimmedPublicKey, 0, bytePublicKey.length - 1);
            this.ecdh = new ECDH(getCurve(), trimmedPrivateKey, trimmedPublicKey);
        } else {
            this.ecdh = new ECDH(getCurve(), trimmedPrivateKey, bytePublicKey);
        }
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
        SessionKey sessionKey = new SessionKey(getSessionID(), derivatedKey);
        sessionKey.changeTypeToSessionKey();

        return sessionKey;
    }

    public SessionKey getSessionKey(String base32PublicKey) {
        // Converting back a Base32 String to its Byte Value gives in some cases an additional Byte
        byte[] bytePublicKey = Base32.base32ToByte(base32PublicKey);
        // Trim the additional Byte
        byte[] agreedKey;
        if (bytePublicKey.length != 65) {
            byte[] trimmedPublicKey = new byte[bytePublicKey.length - 1];
            System.arraycopy(bytePublicKey, 0, trimmedPublicKey, 0, bytePublicKey.length - 1);
            agreedKey = ecdh.getAgreedKey(trimmedPublicKey);
        } else {
            agreedKey = ecdh.getAgreedKey(bytePublicKey);
        }
        HMACSHA hmacsha = new HMACSHA(getSessionID().getKeyLength());
        byte[] derivatedKey = hmacsha.getDerivatedKey(getSessionID().getFinalKeyLength(), agreedKey);
        SessionKey sessionKey = new SessionKey(new SessionID(getSessionID().getType(), getSessionID().getRandom()), derivatedKey);
        sessionKey.changeTypeToSessionKey();

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
}
