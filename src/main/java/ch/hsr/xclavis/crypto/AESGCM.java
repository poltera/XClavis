/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.keys.SessionKey;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.TextInputDialog;
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
                // Plaintext ID and IV add the beginning of the file
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
            TextInputDialog dialog = new TextInputDialog("password");
            dialog.setTitle("XClavis");
            dialog.setHeaderText("Passwort für die Entschlüsselung des KeyStores eingeben");
            dialog.setContentText("");

            Optional<String> password = dialog.showAndWait();
            password.ifPresent(choice -> System.out.println("test"));
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
}
