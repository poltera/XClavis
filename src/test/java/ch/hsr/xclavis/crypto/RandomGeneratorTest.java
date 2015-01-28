/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.Base32;
import com.github.sarxos.webcam.Webcam;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
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

    private final static String UNICODE_KEY = "\uD83D\uDD11";

    /**
     * Test of getRandomBytes method, of class RandomGenerator.
     */
    @Test
    public void testGetRandomBytes() {
        byte[] bytes = RandomGenerator.getRandomBytes(16);

        for (byte b : bytes) {

            String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.print("{" + s1 + "} ");
        }
        System.out.println();
        
        String base32 = Base32.byteToBase32(bytes);
        System.out.println(base32);
        
        byte[] bytes2 = Base32.base32ToByte(base32);
        for (byte b : bytes2) {

            String s1 = String.format("%8s", Integer.toBinaryString(b & 0xFF)).replace(' ', '0');
            System.out.print("{" + s1 + "} ");
        }
        System.out.println();
        
    }

    /**
     * Test of getRandomBits method, of class RandomGenerator.
     */
    @Test
    public void testGetRandomBits() {

    }
}
