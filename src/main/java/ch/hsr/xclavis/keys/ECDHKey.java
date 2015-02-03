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

import ch.hsr.xclavis.crypto.ECDH;
import ch.hsr.xclavis.crypto.HMACSHA;
import ch.hsr.xclavis.helpers.Base32;

/**
 *
 * @author Gian Poltéra
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
        super(new SessionID(sessionID.getNextType(), sessionID.getRandom()));
        this.ecdh = new ECDH(getCurve());
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
        SessionID sessionID = new SessionID(getSessionID().getFinalType(), getSessionID().getRandom());
        SessionKey sessionKey = new SessionKey(sessionID, derivatedKey);

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
