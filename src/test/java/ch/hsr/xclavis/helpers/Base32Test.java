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
package ch.hsr.xclavis.helpers;

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
public class Base32Test {

    public Base32Test() {
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
     * Test of bitStringToBase32 method, of class Base32.
     */
    @Test
    public void testBitStringToBase32() {
        System.out.println("bitStringToBase32");
        String bitString = "0100001001000011101";
        System.out.println("Input: " + bitString);
        String expResult = "AB3U";
        String result = Base32.bitStringToBase32(bitString);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of base32ToBitString method, of class Base32.
     */
    @Test
    public void testBase32ToBitString() {
        System.out.println("base32ToBitString");
        String base32 = "AB3";
        System.out.println("Input: " + base32);
        String expResult = "010000100100001";
        String result = Base32.base32ToBitString(base32);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of byteToBase32 method, of class Base32.
     */
    @Test
    public void testByteToBase32() {
        System.out.println("byteToBase32");
        byte[] bytes = {100,1,10};
        System.out.println("Input: " + "{100,1,10}");
        String expResult = "EJ2JN";
        String result = Base32.byteToBase32(bytes);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of base32ToByte method, of class Base32.
     */
    @Test
    public void testBase32ToByte() {
        System.out.println("base32ToByte");
        String base32 = "HSR";
        System.out.println("Input: " + base32);
        byte[] expResult = {126,46};
        byte[] result = Base32.base32ToByte(base32);
        System.out.println("Result: " + "{" + result[0] + "," + result[1] + "}");
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of byteToBitString method, of class Base32.
     */
    @Test
    public void testByteToBitString() {
        System.out.println("byteToBitString");
        byte[] bytes = {126,46};
        System.out.println("Input: " + "{" + bytes[0] + "," + bytes[1] + "}");
        String expResult = "0111111000101110";
        String result = Base32.byteToBitString(bytes);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }

    /**
     * Test of bitStringToByte method, of class Base32.
     */
    @Test
    public void testBitStringToByte() {
        System.out.println("bitStringToByte");
        String string = "0111111000101110";
        System.out.println("Input: " + string);
        byte[] expResult = {126,46};
        byte[] result = Base32.bitStringToByte(string);
        System.out.println("Result: " + "{" + result[0] + "," + result[1] + "}");
        assertArrayEquals(expResult, result);
    }
}
