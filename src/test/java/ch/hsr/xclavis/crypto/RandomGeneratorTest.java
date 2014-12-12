/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.FormatTransformer;
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
    }

    /**
     * Test of getRandomBits method, of class RandomGenerator.
     */
    @Test
    public void testGetRandomBits() {
        for (Webcam webcam : Webcam.getWebcams()) {
            System.out.println(webcam.getName());
        }
    }
}
