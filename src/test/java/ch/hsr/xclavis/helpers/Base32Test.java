/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
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
