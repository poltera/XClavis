/*
 * Here comes the text of your license
 * Each line should be prefixed with  * 
 */
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.Base32;
import java.util.Arrays;
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
public class ECDHTest {

    public ECDHTest() {
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
     * Test ECDH Key Exchange Brainpool P256.
     */
    @Test
    public void testBrainpoolP256() {
        String curve1 = "brainpoolP256t1";

        System.out.println("start test the curve " + curve1);
        if (!testECDHCurve(curve1)) {
            fail("Test not successful for the curve: " + curve1);
        }
    }

    /**
     * Test ECDH Key Exchange Brainpool P512.
     */
    @Test
    public void testBrainpoolP512() {
        String curve2 = "brainpoolP512t1";

        System.out.println("start test the curve " + curve2);
        if (!testECDHCurve(curve2)) {
            fail("Test not successful for the curve: " + curve2);
        }
    }

    private boolean testECDHCurve(String curve) {
        ECDH ecdh1 = new ECDH(curve);
        ECDH ecdh2 = new ECDH(curve);

        byte[] publicKey1 = ecdh1.getPublicKey();
        System.out.println("PublicKey1: " + Base32.byteToBase32(publicKey1));
        System.out.println("PublicKey1 Length: " + publicKey1.length * Byte.SIZE);

        byte[] publicKey2 = ecdh2.getPublicKey();
        System.out.println("PublicKey2: " + Base32.byteToBase32(publicKey2));
        System.out.println("PublicKey2 Length: " + publicKey2.length * Byte.SIZE);

        byte[] agreedKey1 = ecdh1.getAgreedKey(publicKey2);
        System.out.println("KeyAgreement1: " + Base32.byteToBase32(agreedKey1));

        byte[] agreedKey2 = ecdh2.getAgreedKey(publicKey1);
        System.out.println("KeyAgreement2: " + Base32.byteToBase32(agreedKey2));
        System.out.println("KeyAgreement Length: " + agreedKey2.length * Byte.SIZE);

        return Arrays.equals(agreedKey1, agreedKey2);
    }
}
