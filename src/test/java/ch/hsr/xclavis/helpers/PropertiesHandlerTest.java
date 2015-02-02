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
