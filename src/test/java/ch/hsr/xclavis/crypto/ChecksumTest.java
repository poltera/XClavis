/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
        int length = 1;
        String expResult = "X";
        String result = Checksum.get(string, length);
        System.out.println("Input: " + string + " Checksum: " + result);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        //fail("The test case is a prototype.");
    }

    /**
     * Test of verify method, of class Checksum.
     */
    @Test
    public void testVerify() {
    }
    
}
