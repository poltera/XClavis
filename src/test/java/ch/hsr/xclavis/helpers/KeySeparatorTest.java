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
