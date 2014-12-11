/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.FormatTransformer;
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
        try {
            System.out.println("getRandomBytes");
            int bytes = 0;
            byte[] expResult = null;
            byte[] result = RandomGenerator.getRandomBytes(bytes);
            
            System.out.println(UNICODE_KEY);
            System.out.println(UNICODE_KEY.length());
            System.out.println(new BigInteger(1, UNICODE_KEY.getBytes("UTF-8")).toString(2));
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(RandomGeneratorTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Test of getRandomBits method, of class RandomGenerator.
     */
    @Test
    public void testGetRandomBits() {
        System.out.println("getRandomBits");
        int bits = 15;
        String expResult = "";
        String result = RandomGenerator.getRandomBits(bits);
        System.out.println(FormatTransformer.bitStringToBase32("000000000000000"));
    }
    
}
