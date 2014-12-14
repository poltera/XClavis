/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ch.hsr.xclavis.qrcode;

import ch.hsr.xclavis.helpers.QRModel;
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
            String testFile = "/images/testqr.png";
            System.out.println("Check QR Code Reader with png Image: " + testFile);
            BufferedImage image = ImageIO.read(getClass().getResourceAsStream(testFile));
            QRCodeReader instance = new QRCodeReader();
            if (instance.checkImage(image)) {
                System.out.println("QR-Code detected: " + instance.getString());
            } else {
                fail("QR-Code not detected!");
            }
        } catch (IOException ex) {
            fail("File not found!");
        }
    }
    
}
