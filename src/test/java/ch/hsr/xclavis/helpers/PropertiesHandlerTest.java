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
public class PropertiesHandlerTest {
    
    public PropertiesHandlerTest() {
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
     * Test of getString method, of class PropertiesHandler.
     */
    @Test
    public void testGetString() {
        System.out.println("getString");
        String name = "string";
        String value = "XClavis";
        PropertiesHandler instance = new PropertiesHandler();
        instance.set(name, value);
        
        System.out.println("Input: " + name);
        String expResult = "XClavis";
        String result = instance.getString(name);
        System.out.println("Result: " + result);
        instance.remove(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of getInteger method, of class PropertiesHandler.
     */
    @Test
    public void testGetInteger() {
        System.out.println("getInteger");
        String name = "integer";
        int value = -159;
        PropertiesHandler instance = new PropertiesHandler();
        instance.set(name, value);
        
        System.out.println("Input: " + name);
        int expResult = -159;
        int result = instance.getInteger(name);
        System.out.println("Result: " + result);
        instance.remove(name);
        assertEquals(expResult, result);
    }

    /**
     * Test of getDouble method, of class PropertiesHandler.
     */
    @Test
    public void testGetDouble() {
        System.out.println("getDouble");
        String name = "double";
        double value = 15.59;
        PropertiesHandler instance = new PropertiesHandler();
        instance.set(name, value);
        
        System.out.println("Input: " + name);
        double expResult = 15.59;
        double result = instance.getDouble(name);
        System.out.println("Result: " + result);
        instance.remove(name);
        assertEquals(expResult, result, 0.0);
    }

    /**
     * Test of getBoolean method, of class PropertiesHandler.
     */
    @Test
    public void testGetBoolean() {
        System.out.println("getBoolean");
        String name = "boolean";
        boolean value = true;
        PropertiesHandler instance = new PropertiesHandler();
        instance.set(name, value);
        
        System.out.println("Input: " + name);
        boolean expResult = true;
        boolean result = instance.getBoolean(name);
        System.out.println("Result: " + result);
        instance.remove(name);
        assertEquals(expResult, result);
    }    
}
