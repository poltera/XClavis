/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.keys;

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
        System.out.println("getSessionKeys");
        KeyStore instance = new KeyStore();
        List<SessionKey> result = instance.getSessionKeys();

        for (SessionKey sessionKey : result) {
            System.out.println("ID: " + sessionKey.getID() + " KEY: " + sessionKey.getKey() + " EXISTS: " + instance.existsKey(sessionKey.getSessionID()));
            
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
