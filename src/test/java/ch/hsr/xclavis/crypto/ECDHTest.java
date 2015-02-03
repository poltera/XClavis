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

import ch.hsr.xclavis.helpers.Base32;
import java.util.Arrays;
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
public class ECDHTest {

    public ECDHTest() {
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
     * Test ECDH Key Exchange Brainpool P256.
     */
    @Test
    public void testBrainpoolP256() {
        String curve1 = "brainpoolP256t1";

        System.out.println("start test the curve " + curve1);
        if (!testECDHCurve(curve1)) {
            fail("Test not successful for the curve: " + curve1);
        }
    }

    /**
     * Test ECDH Key Exchange Brainpool P512.
     */
    @Test
    public void testBrainpoolP512() {
        String curve2 = "brainpoolP512t1";

        System.out.println("start test the curve " + curve2);
        if (!testECDHCurve(curve2)) {
            fail("Test not successful for the curve: " + curve2);
        }
    }

    private boolean testECDHCurve(String curve) {
        ECDH ecdh1 = new ECDH(curve);
        ECDH ecdh2 = new ECDH(curve);

        byte[] publicKey1 = ecdh1.getPublicKey();
        System.out.println("PublicKey1: " + Base32.byteToBase32(publicKey1));
        System.out.println("PublicKey1 Length: " + publicKey1.length * Byte.SIZE);

        byte[] publicKey2 = ecdh2.getPublicKey();
        System.out.println("PublicKey2: " + Base32.byteToBase32(publicKey2));
        System.out.println("PublicKey2 Length: " + publicKey2.length * Byte.SIZE);

        byte[] agreedKey1 = ecdh1.getAgreedKey(publicKey2);
        System.out.println("KeyAgreement1: " + Base32.byteToBase32(agreedKey1));

        byte[] agreedKey2 = ecdh2.getAgreedKey(publicKey1);
        System.out.println("KeyAgreement2: " + Base32.byteToBase32(agreedKey2));
        System.out.println("KeyAgreement Length: " + agreedKey2.length * Byte.SIZE);

        return Arrays.equals(agreedKey1, agreedKey2);
    }
}
