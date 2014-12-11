package ch.hsr.xclavis.qrcode;

import com.google.zxing.EncodeHintType;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageConfig;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;

public class QRCodeGenerator {

    private static final MatrixToImageConfig DEFAULT_CONFIG = new MatrixToImageConfig();

    public QRCodeGenerator() {
    }

    public Image createQR(String code, int size) {
        try {
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix matrix = qrCodeWriter.encode(code, BarcodeFormat.QR_CODE, size, size, hints);
            int width = matrix.getWidth();
            int height = matrix.getHeight();

            WritableImage image = new WritableImage(width, height);
            PixelWriter writer = image.getPixelWriter();

            int onColor = DEFAULT_CONFIG.getPixelOnColor();
            int offColor = DEFAULT_CONFIG.getPixelOffColor();

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    writer.setArgb(x, y, matrix.get(x, y) ? onColor : offColor);
                }
            }

            return image;
        } catch (WriterException ex) {
            Logger.getLogger(QRCodeGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

        return null;
    }
}
