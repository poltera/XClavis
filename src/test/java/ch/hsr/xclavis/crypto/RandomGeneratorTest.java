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
public class RandomGeneratorTest {

    public RandomGeneratorTest() {
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
     * Test of getRandomBytes method, of class RandomGenerator.
     */
    @Test
    public void testGetRandomBytes() {
        System.out.println("getRandomBytes");
        int bytes = 16;
        System.out.println("Request length: " + bytes);
        int expResult = 16;
        byte[] result = RandomGenerator.getRandomBytes(bytes);
        System.out.println("Result: " + Base32.byteToBase32(result));
        if (result.length != expResult) {
            fail("Length of the random value not correct.");
        }
        
        int bytes2 = 32;
        System.out.println("Request length: " + bytes2);
        int expResult2 = 32;
        byte[] result2 = RandomGenerator.getRandomBytes(bytes2);
        System.out.println("Result: " + Base32.byteToBase32(result2));
        if (result2.length != expResult2) {
            fail("Length of the random value not correct.");
        }
        
        int bytes3 = 64;
        System.out.println("Request length: " + bytes3);
        int expResult3 = 64;
        byte[] result3 = RandomGenerator.getRandomBytes(bytes3);
        System.out.println("Result: " + Base32.byteToBase32(result3));
        if (result3.length != expResult3) {
            fail("Length of the random value not correct.");
        }
    }

    /**
     * Test of getRandomBits method, of class RandomGenerator.
     */
    @Test
    public void testGetRandomBits() {
        System.out.println("getRandomBits");
        int bits = 128;
        System.out.println("Request length: " + bits);
        int expResult = 128;
        String result = RandomGenerator.getRandomBits(bits);
        System.out.println("Result: " + Base32.bitStringToBase32(result));
        if (result.length() != expResult) {
            fail("Length of the random value not correct.");
        }

        int bits2 = 256;
        System.out.println("Request length: " + bits2);
        int expResult2 = 256;
        String result2 = RandomGenerator.getRandomBits(bits2);
        System.out.println("Result: " + Base32.bitStringToBase32(result2));
        if (result2.length() != expResult2) {
            fail("Length of the random value not correct.");
        }

        int bits3 = 512;
        System.out.println("Request length: " + bits3);
        int expResult3 = 512;
        String result3 = RandomGenerator.getRandomBits(bits3);
        System.out.println("Result: " + Base32.bitStringToBase32(result3));
        if (result3.length() != expResult3) {
            fail("Length of the random value not correct.");
        }
    }

}
