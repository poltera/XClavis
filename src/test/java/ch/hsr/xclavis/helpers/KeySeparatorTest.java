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
public class KeySeparatorTest {
    
    public KeySeparatorTest() {
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
     * Test of getSeparated method, of class KeySeparator.
     */
    @Test
    public void testGetSeparated() {
        System.out.println("getSeparated");
        String string = "HSRHOCHSCHULEFUERTECHNIK";
        System.out.println("Input: " + string);
        int length = 5;
        String[] expResult = {"HSRHO","CHSCH","ULEFU","ERTEC","HNIK"};
        String[] result = KeySeparator.getSeparated(string, length);
        System.out.println("Result: " + "{" + result[0] + "," + result[1] + "," + result[2] + "," + result[3] + "," + result[4] + "}");
        assertArrayEquals(expResult, result);
    }

    /**
     * Test of putTogether method, of class KeySeparator.
     */
    @Test
    public void testPutTogether() {
        System.out.println("putTogether");
        String[] blocks = {"HSRHO","CHSCH","ULEFU","ERTEC","HNIK"};
        System.out.println("Input: " + "{HSRHO,CHSCH,ULEFU,ERTEC,HNIK}");
        String expResult = "HSRHOCHSCHULEFUERTECHNIK";
        String result = KeySeparator.putTogether(blocks);
        System.out.println("Result: " + result);
        assertEquals(expResult, result);
    }   
}
