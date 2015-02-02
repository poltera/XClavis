/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
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
