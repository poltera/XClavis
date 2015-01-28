/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        String expResult = "AB3U";
        String result = Base32.bitStringToBase32(bitString);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of base32ToBitString method, of class Base32.
     */
    @Test
    public void testBase32ToBitString() {
        System.out.println("base32ToBitString");
        String base32 = "AB3";
        String expResult = "010000100100001";
        String result = Base32.base32ToBitString(base32);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of byteToBase32 method, of class Base32.
     */
    @Test
    public void testByteToBase32() {
        System.out.println("byteToBase32");
        byte[] bytes = {00000100,00000001,00000010};
        String expResult = "AB3";
        String result = Base32.byteToBase32(bytes);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of base32ToByte method, of class Base32.
     */
    @Test
    public void testBase32ToByte() {
        System.out.println("base32ToByte");
        String base32 = "HSR";
        byte[] expResult = {00,00};
        byte[] result = Base32.base32ToByte(base32);
        //assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of byteToBitString method, of class Base32.
     */
    @Test
    public void testByteToBitString() {
        System.out.println("byteToBitString");
        byte[] bytes = null;
        String expResult = "";
        //String result = Base32.byteToBitString(bytes);
        //assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of bitStringToByte method, of class Base32.
     */
    @Test
    public void testBitStringToByte() {
        System.out.println("bitStringToByte");
        String string = "1111111100000001";
        byte[] expResult = null;
        byte[] result = Base32.bitStringToByte(string);
        //assertArrayEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }
    
}
