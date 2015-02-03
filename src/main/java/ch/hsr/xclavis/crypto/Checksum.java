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
package ch.hsr.xclavis.crypto;

import ch.hsr.xclavis.helpers.Base32;
import java.math.BigInteger;

/**
 * This class calculates and verifys a checksum over a specific value.
 * 
 * @author Gian Poltéra
 */
public class Checksum {

    private static final BigInteger BIG32 = BigInteger.valueOf(32);

    /**
     * Gets the checksum with individual length for a string.
     * 
     * @param string the string to calculate the checksum
     * @param length the length of the checksum
     * @return the checksum as a string
     */
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

    /**
     * Verifys the checksum of a string.
     * 
     * @param string the string to verify the checksum
     * @param checksum the checksum to compare
     * @return true, if the checksum is valid or false otherwise
     */
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