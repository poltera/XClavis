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

import ch.hsr.xclavis.files.FileZipper;
import ch.hsr.xclavis.helpers.Base32;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Gian
 */
public class AESGCMTest {

    public AESGCMTest() {
        File base_path = new File(System.getProperty("user.home") + File.separator + ".xclavis" + File.separator);
        
        if (!base_path.exists()) {
            base_path.mkdir();
        }
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of encrypt method, of class AESGCM.
     */
    @Test
    public void testEncrypt() {
        System.out.println("encrypt 128 bit");
        String plaintext = "XClavis Test";
        SessionID sessionID = new SessionID("A", "EBK");
        String key = "D553NN939X4SQ3Q8BDGL3M5P9S";
        SessionKey sessionKey = new SessionKey(sessionID, key);
        String iv = "WCJ6DAPSBXHH8KS8MKAJ";
        sessionKey.setIV(base32IVtoByteArray(iv));
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Key: " + key);
        System.out.println("IV: " + iv);
        byte[] input = plaintext.getBytes();
        String output = System.getProperty("user.home") + File.separator + ".xclavis" + File.separator + "test128.enc";
        AESGCM instance = new AESGCM(sessionKey.getKey(), sessionKey.getIV());
        boolean expResult = true;
        boolean result = instance.encrypt(input, output, sessionKey);
        assertEquals(expResult, result);

        System.out.println("encrypt 256 bit");
        SessionID sessionID2 = new SessionID("B", "LES");
        String key2 = "D553NN939X4SQ3Q8BDGL3M5P9SD553NN939X4SQ3Q8BDGL3M5P9S";
        SessionKey sessionKey2 = new SessionKey(sessionID2, key2);
        String iv2 = "WCJ6DAPSBXHH8KS8MKAJ";
        sessionKey2.setIV(base32IVtoByteArray(iv2));
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Key: " + key2);
        System.out.println("IV: " + iv2);
        byte[] input2 = plaintext.getBytes();
        String output2 = System.getProperty("user.home") + File.separator + ".xclavis" + File.separator + "test256.enc";
        AESGCM instance2 = new AESGCM(sessionKey2.getKey(), sessionKey2.getIV());
        boolean expResult2 = true;
        boolean result2 = instance2.encrypt(input2, output2, sessionKey2);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of decryptToByteStream method, of class AESGCM.
     */
    @Test
    public void testDecrypt() {
        System.out.println("decrypt 128 bit");
        SessionID sessionID = new SessionID("A", "EBK");
        String key = "D553NN939X4SQ3Q8BDGL3M5P9S";
        SessionKey sessionKey = new SessionKey(sessionID, key);
        String iv = "WCJ6DAPSBXHH8KS8MKAJ";
        sessionKey.setIV(base32IVtoByteArray(iv));
        System.out.println("Key: " + key);
        System.out.println("IV: " + iv);
        byte[] input = fileToByteArrayOutputStream(new File(System.getProperty("user.home") + File.separator + ".xclavis" + File.separator + "test128.enc"));
        AESGCM instance = new AESGCM(sessionKey.getKey(), sessionKey.getIV());
        byte[] expResult = "XClavis Test".getBytes();
        byte[] result = instance.decryptToByteStream(input);
        System.out.println("Result: " + Base32.byteToBase32(result));
        assertArrayEquals(expResult, result);

        System.out.println("decrypt 256 bit");
        SessionID sessionID2 = new SessionID("B", "LES");
        String key2 = "D553NN939X4SQ3Q8BDGL3M5P9SD553NN939X4SQ3Q8BDGL3M5P9S";
        SessionKey sessionKey2 = new SessionKey(sessionID2, key2);
        String iv2 = "WCJ6DAPSBXHH8KS8MKAJ";
        sessionKey2.setIV(base32IVtoByteArray(iv2));
        System.out.println("Key: " + key2);
        System.out.println("IV: " + iv2);
        byte[] input2 = fileToByteArrayOutputStream(new File(System.getProperty("user.home") + File.separator + ".xclavis" + File.separator + "test256.enc"));
        AESGCM instance2 = new AESGCM(sessionKey2.getKey(), sessionKey2.getIV());
        byte[] expResult2 = "XClavis Test".getBytes();
        byte[] result2 = instance2.decryptToByteStream(input2);
        System.out.println("Result: " + Base32.byteToBase32(result2));
        assertArrayEquals(expResult2, result2);
    }

    private byte[] base32IVtoByteArray(String iv) {
        // Converting back a Base32 String to its Byte Value gives an additional Byte
        byte[] byteIV = Base32.base32ToByte(iv);
        // Trim the additional Byte
        byte[] trimmedIV = new byte[byteIV.length - 1];
        System.arraycopy(byteIV, 0, trimmedIV, 0, byteIV.length - 1);
        return trimmedIV;
    }

    private byte[] fileToByteArrayOutputStream(File file) {
        byte[] buffer = new byte[2048];
        try (FileInputStream fis = new FileInputStream(file);
                DataInputStream dis = new DataInputStream(fis);
                ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            // Remove the ID and the IV from the beginning of the file.
            dis.skipBytes(16);
            int length;
            while ((length = dis.read(buffer)) > 0) {
                baos.write(buffer, 0, length);
            }
            return baos.toByteArray();
        } catch (IOException ex) {
            Logger.getLogger(FileZipper.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
}
