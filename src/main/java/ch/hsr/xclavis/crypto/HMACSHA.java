/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.crypto.macs.HMac;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 *
 * @author Gian
 */
public class HMACSHA {
    private final static String KEY = "XClavis Key Exchange";
    private byte[] inputBuffer, outputBuffer, derivatedKey;
    private Digest digest;

    public HMACSHA(int length) {
        this.outputBuffer = new byte[length / Byte.SIZE];
        this.inputBuffer = KEY.getBytes();
        
        if (length == 256) {
            digest = new SHA256Digest();
        } else if (length == 512) {
            digest = new SHA512Digest();
        }        
    }

    public byte[] getDerivatedKey(int length, byte[] key) {
        derivatedKey = new byte[length / Byte.SIZE];

        HMac mac = new HMac(digest);
        mac.init(new KeyParameter(key));
        mac.update(inputBuffer, 0, inputBuffer.length);
        mac.doFinal(outputBuffer, 0);

        // Source Array, From Source, Destination Array, To Destination, Count
        System.arraycopy(outputBuffer, 0, derivatedKey, 0, derivatedKey.length);

        return derivatedKey;
    }
}
