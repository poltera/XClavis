/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.keys;

import ch.hsr.xclavis.helpers.Base32;
import java.util.List;
import javafx.collections.ObservableList;
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
     * Test of getObservableKeyList method, of class KeyStore.
     */
    @Test
    public void testGetObservableKeyList() {

    }

    /**
     * Test of add method, of class KeyStore.
     */
    @Test
    public void testAdd_Key() {
    }

    /**
     * Test of add method, of class KeyStore.
     */
    @Test
    public void testAdd_List() {
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
     * Test of getSessionKeys method, of class KeyStore.
     */
    @Test
    public void testGetSessionKeys() {
        System.out.println("SaveKeys");
        KeyStore instance = new KeyStore();

        SessionKey sessionKey128 = new SessionKey(SessionID.SESSION_KEY_128);
        sessionKey128.setPartner("KeyStoreTest");
        SessionKey sessionKey256 = new SessionKey(SessionID.SESSION_KEY_256);
        sessionKey256.setPartner("KeyStoreTest");
        ECDHKey ecdhKeyReq256 = new ECDHKey(SessionID.ECDH_REQ_256);
        ecdhKeyReq256.setPartner("KeyStoreTest");
        ECDHKey ecdhKeyReq512 = new ECDHKey(SessionID.ECDH_REQ_512);
        ecdhKeyReq512.setPartner("KeyStoreTest");
        ECDHKey ecdhKeyRes256 = new ECDHKey(SessionID.ECDH_RES_256);
        ecdhKeyRes256.setPartner("KeyStoreTest");
        ECDHKey ecdhKeyRes512 = new ECDHKey(SessionID.ECDH_RES_512);
        ecdhKeyRes512.setPartner("KeyStoreTest");

        System.out.println("ID: " + sessionKey128.getID() + " Date: " + sessionKey128.getDate() + " Partner: " + sessionKey128.getPartner() + " State: " + sessionKey128.getState());
        System.out.println("Key: " + Base32.byteToBase32(sessionKey128.getKey()));
        System.out.println("ID: " + sessionKey256.getID() + " Date: " + sessionKey256.getDate() + " Partner: " + sessionKey256.getPartner() + " State: " + sessionKey256.getState());
        System.out.println("Key: " + Base32.byteToBase32(sessionKey256.getKey()));
        System.out.println("ID: " + ecdhKeyReq256.getID() + " Date: " + ecdhKeyReq256.getDate() + " Partner: " + ecdhKeyReq256.getPartner() + " State: " + ecdhKeyReq256.getState());
        System.out.println("Private Key: " + Base32.byteToBase32(ecdhKeyReq256.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyReq256.getPublicKey()));
        System.out.println("ID: " + ecdhKeyReq512.getID() + " Date: " + ecdhKeyReq512.getDate() + " Partner: " + ecdhKeyReq512.getPartner() + " State: " + ecdhKeyReq512.getState());
        System.out.println("Private Key: " + Base32.byteToBase32(ecdhKeyReq512.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyReq512.getPublicKey()));
        System.out.println("ID: " + ecdhKeyRes256.getID() + " Date: " + ecdhKeyRes256.getDate() + " Partner: " + ecdhKeyRes256.getPartner() + " State: " + ecdhKeyRes256.getState());
        System.out.println("Private Key: " + Base32.byteToBase32(ecdhKeyRes256.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyRes256.getPublicKey()));
        System.out.println("ID: " + ecdhKeyRes512.getID() + " Date: " + ecdhKeyRes512.getDate() + " Partner: " + ecdhKeyRes512.getPartner() + " State: " + ecdhKeyRes512.getState());
        System.out.println("Private Key: " + Base32.byteToBase32(ecdhKeyRes512.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyRes512.getPublicKey()));

        instance.add(sessionKey128);
        instance.add(sessionKey256);
        instance.add(ecdhKeyReq256);
        instance.add(ecdhKeyReq512);
        instance.add(ecdhKeyRes256);
        instance.add(ecdhKeyRes512);

        System.out.println("LoadKeys");
        KeyStore instance2 = new KeyStore();

        List<SessionKey> sessionKeys = instance2.getSessionKeys();
        for (SessionKey sessionKey : sessionKeys) {
            System.out.println("ID: " + sessionKey.getID() + " Date: " + sessionKey.getDate() + " Partner: " + sessionKey.getPartner() + " State: " + sessionKey.getState());
            System.out.println("Key: " + Base32.byteToBase32(sessionKey.getKey()));
            instance2.remove(sessionKey);
        }

        List<ECDHKey> ecdhKeys = instance2.getECDHKeys();
        for (ECDHKey ecdhKey : ecdhKeys) {
            System.out.println("ID: " + ecdhKey.getID() + " Date: " + ecdhKey.getDate() + " Partner: " + ecdhKey.getPartner() + " State: " + ecdhKey.getState());
            System.out.println("Private Key: " + Base32.byteToBase32(ecdhKey.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKey.getPublicKey()));
            instance2.remove(ecdhKey);
        }
    }

    /**
     * Test of getECDHKey method, of class KeyStore.
     */
    @Test
    public void testGetECDHKey() {

    }

    /**
     * Test of getECDHKeys method, of class KeyStore.
     */
    @Test
    public void testGetECDHKeys() {
    }

    /**
     * Test of remove method, of class KeyStore.
     */
    @Test
    public void testRemove() {
    }

    /**
     * Test of saveKeys method, of class KeyStore.
     */
    @Test
    public void testSaveKeys() {
    }

}
