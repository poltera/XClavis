/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

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
    private static final byte[] block = new byte[16];
    private final AEADParameters cipherParameters;

    public AESGCM(byte[] key, byte[] iv) {
        this.cipherParameters = new AEADParameters(new KeyParameter(key), 128, iv);
    }
    
    public boolean encrypt(byte[] input, String output) {
        try {
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(true, cipherParameters);

            try (FileOutputStream fos = new FileOutputStream(output);
                    DataOutputStream dos = new DataOutputStream(fos);
                    //BufferedOutputStream bos = new BufferedOutputStream(fos);
                    CipherOutputStream cos = new CipherOutputStream(dos, cipher)) {
                cos.write(input);   
            }  
            
            
            return true;
        } catch (IOException ex) {
            Logger.getLogger(AESGCM.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean decrypt(String input, String output) {
        try {
            AEADBlockCipher cipher = new GCMBlockCipher(new AESEngine());
            cipher.init(false, cipherParameters);

            try (FileInputStream fis = new FileInputStream(input);
                    CipherInputStream cis = new CipherInputStream(fis, cipher);
                    FileOutputStream fos = new FileOutputStream(output)) {
                int i;
                while ((i = cis.read(block)) != -1) {
                    fos.write(block, 0, i);
                }
            }
            return true;
        } catch (InvalidCipherTextIOException ex) {
            //System.out.println("Hash for the file is not correct!");
            return false;
        } catch (IOException ex) {
            Logger.getLogger(AESGCM.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }
}
