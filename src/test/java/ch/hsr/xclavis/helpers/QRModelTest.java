/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.helpers;

import ch.hsr.xclavis.qrcode.QRModel;
import ch.hsr.xclavis.keys.ECDHKey;
import ch.hsr.xclavis.keys.Key;
import ch.hsr.xclavis.keys.SessionID;
import ch.hsr.xclavis.keys.SessionKey;
import java.util.List;
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
public class QRModelTest {

    public QRModelTest() {
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
     * Test of addSessionKey method, of class QRModel.
     */
    @Test
    public void testAddSessionKey() {

    }

    /**
     * Test of addECDHKey method, of class QRModel.
     */
    @Test
    public void testAddECDHKey() {

    }

    /**
     * Test of getModell method, of class QRModel.
     */
    @Test
    public void testGetModell() {

    }

    /**
     * Test of getKeys method, of class QRModel.
     */
    @Test
    public void testGetKeys() {
        QRModel qrModel = new QRModel();
        ECDHKey ecdhKey = new ECDHKey(SessionID.ECDH_REQ_256);
        qrModel.addECDHKey(ecdhKey);
        ECDHKey ecdhKey2 = new ECDHKey(SessionID.ECDH_REQ_512);
        qrModel.addECDHKey(ecdhKey2);
        SessionKey sessionKey = new SessionKey(SessionID.SESSION_KEY_128);
        qrModel.addSessionKey(sessionKey);
        SessionKey sessionKey2 = new SessionKey(SessionID.SESSION_KEY_256);
        qrModel.addSessionKey(sessionKey2);
        String result = qrModel.getModell();

        List<Key> keys = qrModel.getKeys(result);
    }

}
