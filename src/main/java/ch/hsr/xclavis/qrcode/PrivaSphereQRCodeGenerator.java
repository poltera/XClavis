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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * NO COMMIT TO PUBLIC
 * This class generats a QR-Code from a existing key
 *
 * @author Gian Poltéra
 */
public class PrivaSphereQRCodeGenerator {

    private final static MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();
    private final static String TITLE = "PrivaSphere";
    private final static String NEWLINE = "\n";
    private final static String UNICODE_SPACE = "\u0020";
    private final static String UNICODE_LOGO = "\u2709";
    private final static String UNICODE_KEY = "\uD83D\uDD11";
    private final static String FROM = "F:";
    private final static String TO = "T:";
    private final static String DATE = "D:";
    private final static String TEXT_BEFORE_ID = "open the encrypted pdf message (";
    private final static String TEXT_AFTER_ID = ") you received by mail, use key:";
    private final static int QR_SIZE = 500;
    private final static String QR_CHARACTER_SET = "UTF-8";
    private final static String IMAGE_FORMAT = "png";

    /**
     *
     * @param from the sender of the message
     * @param to the receiver of the message 
     * @param id the id of the message
     * @param key the key of the encrypted PDF
     * @return a byte-array of the generated QR-code
     */
    public byte[] createQRCode(String from, String to, String id, String key) {
        String code = "";
        code += UNICODE_LOGO + UNICODE_SPACE + TITLE + NEWLINE
                + FROM + UNICODE_SPACE + from + NEWLINE
                + TO + UNICODE_SPACE + to + NEWLINE
                + DATE + UNICODE_SPACE + getDate() + NEWLINE
                + TEXT_BEFORE_ID + id + TEXT_AFTER_ID + NEWLINE
                + UNICODE_KEY + UNICODE_SPACE + key;
        
        //System.out.println(code);
        
        return createQRCodeBAOS(code).toByteArray();
    }

    private ByteArrayOutputStream createQRCodeBAOS(String code) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, QR_CHARACTER_SET);
            BitMatrix bm = new QRCodeWriter().encode(code, BarcodeFormat.QR_CODE, QR_SIZE, QR_SIZE, hints);
            int width = bm.getWidth();
            int height = bm.getHeight();

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

            int onColor = DEFAULT_CONFIG.getPixelOnColor();
            int offColor = DEFAULT_CONFIG.getPixelOffColor();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? onColor : offColor);
                }
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            // Write to baos
            ImageIO.write(image, IMAGE_FORMAT, baos);
            // Write to file
            //ImageIO.write(image, IMAGE_FORMAT, new File("test.png"));

            return baos;
        } catch (WriterException | IOException ex) {
            Logger.getLogger(PrivaSphereQRCodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }

    private String getDate() {
        // Needs Java 8+
        LocalDateTime now = LocalDateTime.now();

        return now.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
