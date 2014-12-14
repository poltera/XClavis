package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.Base32;
import java.security.SecureRandom;

public class RandomGenerator {
	/**
	 * Create a random-value with individual byte-length
	 * 
	 * @param bytes the byte-length of the random-value
	 * @return random-value
	 */
	public static byte[] getRandomBytes(int bytes) {
            SecureRandom random = new SecureRandom();
            byte[] randomBytes = new byte[bytes];
            random.nextBytes(randomBytes);

            return randomBytes;
	}
        
        public static String getRandomBits(int bits) {
            byte[] randomBytes = getRandomBytes(bits/Byte.SIZE + 1);
            String randomBits = Base32.byteToBitString(randomBytes);
            
            return randomBits.substring(0, bits);
        }
}

