/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package ch.hsr.xclavis.qrcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
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
public class QRCodeReaderTest {

    public QRCodeReaderTest() {
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
     * Test of checkImage method, of class QRCodeReader.
     */
    @Test
    public void testCheckImage() {
        try {
            System.out.println("checkImage");
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/test/testqr.png"));
            System.out.println("Input: " + "testqr.png");
            QRCodeReader instance = new QRCodeReader();
            boolean expResult = true;
            boolean result = instance.checkImage(image);
            System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            fail("File not found!");
        }
    }

    /**
     * Test of getResult method, of class QRCodeReader.
     */
    @Test
    public void testGetResult() {
        System.out.println("getResult");
        try {
            System.out.println("checkImage");
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream("/test/testqr.png"));
            System.out.println("Input: " + "testqr.png");
            QRCodeReader instance = new QRCodeReader();
            String expResult = "\ud83d\udd10\u0020XClavis\u0020\ud83c\udd94\u0020CEQ7J\n\uD83D\uDD11\u00202CBYJ-YA8HM-P6DTN-55DH2-LYT8W-UDAWT-EM4AT-D66XS-GGXVS-9U32L-LMS3W-NQG8Q-AD7GQ-JU6JS";
            instance.checkImage(image);
            String result = instance.getResult();;
            System.out.println("Result: " + result);
            assertEquals(expResult, result);
        } catch (IOException ex) {
            fail("File not found!");
        }
    }
}
