package ch.hsr.xclavis.qrcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import java.awt.image.BufferedImage;

public class QRCodeReader {

    private Result result;
    
    public QRCodeReader() {
        this.result = null;
    }
    
    public boolean checkImage(BufferedImage image) {
        result = null;
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        try {
            result = new MultiFormatReader().decode(bitmap);
        } catch (NotFoundException e) {
            // No QR Code in the image
        }
        if (result != null) {
            return true;
        }
        
        return false;
    }
    
    public String getResult() {
        if (result != null) {
            return result.getText();
        }
        return "";
    }
}
