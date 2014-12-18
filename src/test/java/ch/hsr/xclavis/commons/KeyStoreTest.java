/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.commons;

import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.keys.KeyStore;
import ch.hsr.xclavis.helpers.Base32;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Gian
 */
public class KeyStoreTest {

    public KeyStoreTest() {
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
     * Test of addSessionKey method, of class KeyStore.
     */
    @Test
    public void testAddSessionKey() {

    }

    /**
     * Test of addECDHKey method, of class KeyStore.
     */
    @Test
    public void testAddECDHKey() {
    }

    /**
     * Test of addSessionKeys method, of class KeyStore.
     */
    @Test
    public void testAddSessionKeys() {

    }

    /**
     * Test of addECDHKeys method, of class KeyStore.
     */
    @Test
    public void testAddECDHKeys() {

    }

    /**
     * Test of existsKey method, of class KeyStore.
     */
    @Test
    public void testExistsKey() {

    }

    /**
     * Test of getSessionKey method, of class KeyStore.
     */
    @Test
    public void testGetSessionKey() {

    }

    /**
     * Test of getECDHKey method, of class KeyStore.
     */
    @Test
    public void testGetECDHKey() {

    }

    /**
     * Test of removeKey method, of class KeyStore.
     */
    @Test
    public void testRemoveKey() {
        int privLengthIncorrect = 0;
        int pubLengthIncorrect = 0;
        
        for (int i = 0; i < 1000; i++) {
            ECDHKey ecdhKey = new ECDHKey(SessionID.ECDH_REQ_256);
            if (ecdhKey.getPrivateKey().length != 32) {
                privLengthIncorrect++;
            }
            if (ecdhKey.getPublicKey().length != 33) {
                pubLengthIncorrect++;
            }
        }
        System.out.println("256BIT ECDH -> PRIV LENGHT INCORRECT: " + privLengthIncorrect + " PUB LENGTH INCORRECT: " + pubLengthIncorrect);
        
        privLengthIncorrect = 0;
        pubLengthIncorrect = 0;
        
        for (int i = 0; i < 1000; i++) {
            ECDHKey ecdhKey = new ECDHKey(SessionID.ECDH_REQ_512);
            if (ecdhKey.getPrivateKey().length != 64) {
                privLengthIncorrect++;
            }
            if (ecdhKey.getPublicKey().length != 65) {
                pubLengthIncorrect++;
            }
        }
        System.out.println("512BIT ECDH -> PRIV LENGHT INCORRECT: " + privLengthIncorrect + " PUB LENGTH INCORRECT: " + pubLengthIncorrect);
    }

    /**
     * Test of saveKeys method, of class KeyStore.
     */
    @Test
    public void testSaveKeys() {
        System.out.println("testSaveKeys");
        KeyStore instance = new KeyStore();
        SessionKey sessionKey1 = new SessionKey(SessionID.SESSION_KEY_128);
        SessionKey sessionKey2 = new SessionKey(SessionID.SESSION_KEY_256);
        ECDHKey ecdhKey1 = new ECDHKey(SessionID.ECDH_REQ_256);
        ECDHKey ecdhKey2 = new ECDHKey(SessionID.ECDH_REQ_512);
        System.out.println("Save the following Keys:");
        System.out.println("ID: " + sessionKey1.getID() + " KEY:" + Base32.byteToBase32(sessionKey1.getKey()));
        System.out.println("ID: " + sessionKey2.getID() + " KEY:" + Base32.byteToBase32(sessionKey2.getKey()));
        System.out.println("PRIVLENGTH: " + ecdhKey1.getPrivateKey().length);
        System.out.println("PUBLENGTH: " + ecdhKey1.getPublicKey().length);
        System.out.println("ID: " + ecdhKey1.getID() + " PRIVATE KEY: " + Base32.byteToBase32(ecdhKey1.getPrivateKey()) + " PUBLIC KEY: " + Base32.byteToBase32(ecdhKey1.getPublicKey()));
        System.out.println("ID: " + ecdhKey2.getID() + " PRIVATE KEY: " + Base32.byteToBase32(ecdhKey2.getPrivateKey()) + " PUBLIC KEY: " + Base32.byteToBase32(ecdhKey2.getPublicKey()));
        instance.add(sessionKey1);
        instance.add(sessionKey2);
        instance.add(ecdhKey1);
        instance.add(ecdhKey2);

        KeyStore instance2 = new KeyStore();
        System.out.println("Read the following Keys:");
        List<SessionKey> sessionKeys = instance2.getSessionKeys();
        List<ECDHKey> ecdhKeys = instance2.getECDHKeys();
        sessionKeys.stream().forEach((sessionKey) -> {
            System.out.println("ID: " + sessionKey.getID() + " KEY:" + Base32.byteToBase32(sessionKey.getKey()));
        });
        ecdhKeys.stream().forEach((ecdhKey) -> {
            System.out.println("ID: " + ecdhKey.getID() + " PRIVATE KEY:" + Base32.byteToBase32(ecdhKey.getPrivateKey()) + " PUBLIC KEY:" + Base32.byteToBase32(ecdhKey.getPublicKey()));
        });

    }

}
