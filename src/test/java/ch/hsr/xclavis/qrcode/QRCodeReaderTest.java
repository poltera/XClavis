/*
 * Copyright (c) 2015, Gian Poltéra
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1.	Redistributions of source code must retain the above copyright notice,
 *   	this list of conditions and the following disclaimer.
 * 2.	Redistributions in binary form must reproduce the above copyright 
 *   	notice, this list of conditions and the following disclaimer in the 
 *   	documentation and/or other materials provided with the distribution.
 * 3.	Neither the name of HSR University of Applied Sciences Rapperswil nor 
 * 	the names of its contributors may be used to endorse or promote products
 * 	derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ch.hsr.xclavis.qrcode;

import java.awt.image.BufferedImage;
import java.io.IOException;
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
