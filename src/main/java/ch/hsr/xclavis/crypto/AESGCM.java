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
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.keys.SessionKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bouncycastle.crypto.engines.AESEngine;
import org.bouncycastle.crypto.io.CipherInputStream;
import org.bouncycastle.crypto.io.CipherOutputStream;
import org.bouncycastle.crypto.io.InvalidCipherTextIOException;
import org.bouncycastle.crypto.modes.AEADBlockCipher;
import org.bouncycastle.crypto.modes.GCMBlockCipher;
import org.bouncycastle.crypto.params.AEADParameters;
import org.bouncycastle.crypto.params.KeyParameter;

/**
 *
 * @author Gian
 */
public class AESGCM {

    private final static byte[] BLOCK = new byte[16];
    private final AEADParameters cipherParameters;

    /**
     *
     * @param key
     * @param iv
     */
    public AESGCM(byte[] key, byte[] iv) {
        this.cipherParameters = new AEADParameters(new KeyParameter(key), BLOCK.length * Byte.SIZE, iv);
    }

    /**
     *
     * @param input
     * @param output
     * @param sessionKey
     * @return
     */
    public boolean encrypt(byte[] input, String output, SessionKey sessionKey) {
        try {
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(true, cipherParameters);

            try (FileOutputStream fos = new FileOutputStream(output);
                    DataOutputStream dos = new DataOutputStream(fos);
                    //BufferedOutputStream bos = new BufferedOutputStream(fos);
                    CipherOutputStream cos = new CipherOutputStream(dos, cipher)) {
                // Plaintext ID and IV add at the beginning of the file
                dos.write(sessionKey.getID().getBytes());
                dos.write(sessionKey.getIV());
                // Encrypted Data
                cos.write(input);
            }

            return true;
        } catch (IOException ex) {
            Logger.getLogger(AESGCM.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean encryptKeyStore(byte[] input, String output) {
        try {
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(true, cipherParameters);

            try (FileOutputStream fos = new FileOutputStream(output);
                    DataOutputStream dos = new DataOutputStream(fos);
                    //BufferedOutputStream bos = new BufferedOutputStream(fos);
                    CipherOutputStream cos = new CipherOutputStream(dos, cipher)) {
                // Encrypted Data
                cos.write(input);
            }

            return true;
        } catch (IOException ex) {
            Logger.getLogger(AESGCM.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean decryptToFile(String input, String output) {
        try {
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(false, cipherParameters);

            try (FileInputStream fis = new FileInputStream(input);
                    CipherInputStream cis = new CipherInputStream(fis, cipher);
                    FileOutputStream fos = new FileOutputStream(output)) {
                int i;
                while ((i = cis.read(BLOCK)) != -1) {
                    fos.write(BLOCK, 0, i);
                }
            }
            return true;
        } catch (InvalidCipherTextIOException ex) {
            System.out.println("Hash for the file is not correct!");
            return false;
        } catch (IOException ex) {
            Logger.getLogger(AESGCM.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public byte[] decryptKeyStore(String input) {
        byte[] result = null;
        try {
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(false, cipherParameters);

            try (FileInputStream fis = new FileInputStream(input);
                    CipherInputStream cis = new CipherInputStream(fis, cipher);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                int i;
                while ((i = cis.read(BLOCK)) != -1) {
                    baos.write(BLOCK, 0, i);
                }
                result = baos.toByteArray();
            }
        } catch (InvalidCipherTextIOException ex) {
            System.out.println("Wrong Password or Hash for the file is not correct!");
        } catch (IOException ex) {
            Logger.getLogger(AESGCM.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public byte[] decryptToByteStream(byte[] input) {
        byte[] result = null;
        try {
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(false, cipherParameters);

            try (ByteArrayInputStream bais = new ByteArrayInputStream(input);
                    CipherInputStream cis = new CipherInputStream(bais, cipher);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                int i;
                while ((i = cis.read(BLOCK)) != -1) {
                    baos.write(BLOCK, 0, i);
                }
                result = baos.toByteArray();
            }
        } catch (InvalidCipherTextIOException ex) {
            System.out.println("Hash for the file is not correct!");
        } catch (IOException ex) {
            Logger.getLogger(AESGCM.class.getName()).log(Level.SEVERE, null, ex);
        }

        return result;
    }

    public boolean isKeyCorrect(String input) {
        try {
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(false, cipherParameters);

            try (FileInputStream fis = new FileInputStream(input);
                    CipherInputStream cis = new CipherInputStream(fis, cipher);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                int i;
                while ((i = cis.read(BLOCK)) != -1) {
                    baos.write(BLOCK, 0, i);
                }
            }
        } catch (InvalidCipherTextIOException ex) {
            return false;
        } catch (IOException ex) {
            Logger.getLogger(AESGCM.class.getName()).log(Level.SEVERE, null, ex);
        }

        return true;

    }
}
