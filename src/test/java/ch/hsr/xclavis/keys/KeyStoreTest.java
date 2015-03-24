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
     * Test all methods of class KeyStore.
     */
    @Test
    public void testKeyStore() {
        System.out.println("AddKeys");

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

        System.out.println("Input:");
        System.out.println("ID: " + sessionKey128.getID() + " Date: " + sessionKey128.getCreationDate() + " Partner: " + sessionKey128.getPartner() + " State: " + sessionKey128.getState());
        System.out.println("Key: " + Base32.byteToBase32(sessionKey128.getKey()));
        System.out.println("ID: " + sessionKey256.getID() + " Date: " + sessionKey256.getCreationDate() + " Partner: " + sessionKey256.getPartner() + " State: " + sessionKey256.getState());
        System.out.println("Key: " + Base32.byteToBase32(sessionKey256.getKey()));
        System.out.println("ID: " + ecdhKeyReq256.getID() + " Date: " + ecdhKeyReq256.getCreationDate() + " Partner: " + ecdhKeyReq256.getPartner() + " State: " + ecdhKeyReq256.getState());
        System.out.println("Private Key: " + Base32.byteToBase32(ecdhKeyReq256.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyReq256.getPublicKey()));
        System.out.println("ID: " + ecdhKeyReq512.getID() + " Date: " + ecdhKeyReq512.getCreationDate() + " Partner: " + ecdhKeyReq512.getPartner() + " State: " + ecdhKeyReq512.getState());
        System.out.println("Private Key: " + Base32.byteToBase32(ecdhKeyReq512.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyReq512.getPublicKey()));
        System.out.println("ID: " + ecdhKeyRes256.getID() + " Date: " + ecdhKeyRes256.getCreationDate() + " Partner: " + ecdhKeyRes256.getPartner() + " State: " + ecdhKeyRes256.getState());
        System.out.println("Private Key: " + Base32.byteToBase32(ecdhKeyRes256.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyRes256.getPublicKey()));
        System.out.println("ID: " + ecdhKeyRes512.getID() + " Date: " + ecdhKeyRes512.getCreationDate() + " Partner: " + ecdhKeyRes512.getPartner() + " State: " + ecdhKeyRes512.getState());
        System.out.println("Private Key: " + Base32.byteToBase32(ecdhKeyRes512.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKeyRes512.getPublicKey()));

        KeyStore instance = new KeyStore();

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
            System.out.println("ID: " + sessionKey.getID() + " Date: " + sessionKey.getCreationDate() + " Partner: " + sessionKey.getPartner() + " State: " + sessionKey.getState());
            System.out.println("Key: " + Base32.byteToBase32(sessionKey.getKey()));
            instance2.remove(sessionKey);
        }

        List<ECDHKey> ecdhKeys = instance2.getECDHKeys();
        for (ECDHKey ecdhKey : ecdhKeys) {
            System.out.println("ID: " + ecdhKey.getID() + " Date: " + ecdhKey.getCreationDate() + " Partner: " + ecdhKey.getPartner() + " State: " + ecdhKey.getState());
            System.out.println("Private Key: " + Base32.byteToBase32(ecdhKey.getPrivateKey()) + " Public Key: " + Base32.byteToBase32(ecdhKey.getPublicKey()));
            instance2.remove(ecdhKey);
        }
    }
}
