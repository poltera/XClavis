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
public class ChecksumTest {

    public ChecksumTest() {
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
     * Test of get method, of class Checksum.
     */
    @Test
    public void testGet() {
        System.out.println("get");
        String string = "M2LA";
        System.out.println("Input: " + string);
        int length = 1;
        String expResult = "X";
        String result = Checksum.get(string, length);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);

        String string2 = "M2LAARSSSSTLUWSCCCCCCER";
        System.out.println("Input: " + string2);
        int length2 = 2;
        String expResult2 = "K2";
        String result2 = Checksum.get(string2, length2);
        System.out.println("Result: " + result2);
        assertEquals(expResult2, result2);
    }

    /**
     * Test of verify method, of class Checksum.
     */
    @Test
    public void testVerify() {
        System.out.println("verify");
        String string = "M2LA";
        String checksum = "X";
        System.out.println("Input: " + string + " " + checksum);
        boolean expResult = true;
        boolean result = Checksum.verify(string, checksum);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);

        String string2 = "M2LAARSSSSTLUWSCCCCCCER";
        String checksum2 = "K2";
        System.out.println("Input: " + string2 + " " + checksum2);
        boolean expResult2 = true;
        boolean result2 = Checksum.verify(string2, checksum2);
        System.out.println("Result: " + result2);
        assertEquals(expResult2, result2);
    }

}
