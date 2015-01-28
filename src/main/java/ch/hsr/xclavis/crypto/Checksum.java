package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.Base32;
import java.math.BigInteger;

public class Checksum {

    private static final BigInteger BIG32 = BigInteger.valueOf(32);

    public static String get(String string, int length) {
        //Initial the bitstring
        BigInteger bitString = BigInteger.ZERO;

        // initial the checksum
        for (int i = 0; i < string.length(); i++) {
            //Character
            String character = string.substring(i, i + 1);
            //BigInteger bitstring of the character
            BigInteger value = BigInteger.valueOf(Integer.parseInt(Base32.base32ToBitString(character)));
            //Add to the partialChecksum
            bitString = bitString.add(value);
        }

        String checksum = Base32.bitStringToBase32(bitString.mod(BIG32.pow(length)).toString(2));
        while (checksum.length() < length) {
            checksum += "Z";
        }
        
        return checksum;
    }

    public static boolean verify(String string, String checksum) {
        //Initial the bitstring
        BigInteger bitString = BigInteger.ZERO;

        // initial the checksum
        for (int i = 0; i < string.length(); i++) {
            //Character
            String character = string.substring(i, i + 1);
            //BigInteger bitstring of the character
            BigInteger value = BigInteger.valueOf(Integer.parseInt(Base32.base32ToBitString(character)));
            //Add to the partialChecksum
            bitString = bitString.add(value);
        }

        String calculatedChecksum = Base32.bitStringToBase32(bitString.mod(BIG32.pow(checksum.length())).toString(2));
        while (calculatedChecksum.length() < checksum.length()) {
            calculatedChecksum += "Z";
        }

        if (calculatedChecksum.equals(checksum)) {
            return true;
        }

        return false;
    }
}
