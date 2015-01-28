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
        
        String expResult = value;
        String result = instance.getString(name);
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
        
        int expResult = value;
        int result = instance.getInteger(name);
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
        
        double expResult = value;
        double result = instance.getDouble(name);
        instance.remove(name);
        
        assertEquals(expResult, result, 0.0);
    }
    
}
