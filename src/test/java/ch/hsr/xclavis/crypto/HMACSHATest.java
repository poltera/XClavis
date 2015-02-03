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
public class HMACSHATest {

    public HMACSHATest() {
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
     * Test of getDerivatedKey128 method, of class HMACSHA.
     */
    @Test
    public void testGetDerivatedKey128() {
        System.out.println("getDerivatedKey128");
        int length = 128;
        byte[] key = Base32.bitStringToByte("0000111000011001110101000111111100111110000010001100100101010001111010100000101110100011001010011101100010000101110100110110101101100111110000001000110101001110101010011000000001101010011001010110000011001001100111111100011110010110110000111001000111000111");
        System.out.println("Input: " + Base32.byteToBase32(key));
        HMACSHA instance = new HMACSHA(256);
        byte[] expResult = Base32.bitStringToByte("01000110111000010101001000011010100001101000010110001110001100110011111010011000101000000110110010000101011000101011011011010001");
        byte[] result = instance.getDerivatedKey(length, key);
        System.out.println("Result: " + Base32.byteToBase32(result));
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of getDerivatedKey256 method, of class HMACSHA.
     */
    @Test
    public void testGetDerivatedKey256() {
        System.out.println("getDerivatedKey256");
        int length = 256;
        byte[] key = Base32.bitStringToByte("10000001110101010010000010010100111101000010101111110001010001101001110110111100101101100110011111110001010110000110101110101100110000000000101001111000101100010110011010010100110001011001101011010011010011100111011001111011000010101100011100110011111111101000001001101001101111000100000000100101111101000010001100100000101011111101111101011101011110110110010110010100001110000000001101000101110011110000000011001001110010101111001101110101001111111111110011000010001101010110101011010111001001100111111110101100");
        System.out.println("Input: " + Base32.byteToBase32(key));
        HMACSHA instance = new HMACSHA(512);
        byte[] expResult = Base32.bitStringToByte("1110001010100111000001011110111101000001111111000101110101001101000101010011110011110000111001111110111011101100111101100100100101011001001011011010001000110110011101100110101010110001001011011010011111110010011001110011100100110100111011000110100110110011");
        byte[] result = instance.getDerivatedKey(length, key);
        System.out.println("Result: " + Base32.byteToBase32(result));
        assertArrayEquals(expResult, result);
    }

}
