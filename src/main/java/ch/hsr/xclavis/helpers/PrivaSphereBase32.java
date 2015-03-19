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
package ch.hsr.xclavis.helpers;

/**
 * This class provides the methods to convert a value to PrivaSphere base32 and back.
 *
 * @author Gian Poltéra
 */
public class PrivaSphereBase32 {

    public final static int SIZE = 5;
    private final static String[] BASE32_CHARACTERS = {"0", "1", "2", "3", "4", "5",
        "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h", "i",
        "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v"};
    private final static int[] BASE32_VALUES = {16, 8, 4, 2, 1};

    /**
     * Converts a bit-string to a base32 string.
     *
     * @param bitString the bit-string to convert
     * @return the base32 value as string
     */
    public static String bitStringToBase32(String bitString) {
        String string = "";

        for (int i = 0; i < bitString.length(); i++) {
            int begin = i;
            int end = i + BASE32_VALUES.length;

            if (end > bitString.length()) {
                end = bitString.length();
            }

            String binCharacter = bitString.substring(begin, end);
            int intCharacter = 0;
            for (int j = 0; j < binCharacter.length(); j++) {
                if (Integer.parseInt(binCharacter.substring(j, j + 1)) == 1) {
                    intCharacter = intCharacter + BASE32_VALUES[j];
                }
            }

            i = i + BASE32_VALUES.length - 1;

            string = getBase32Character(intCharacter) + string;
        }
        string = new StringBuilder(string).reverse().toString();

        return string;
    }

    /**
     * Converts a base32 string to a bit-string.
     *
     * @param base32 the base32 string to convert
     * @return the bit-string as string
     */
    public static String base32ToBitString(String base32) {
        String bitString = "";
        for (int i = 0; i < base32.length(); i++) {
            for (int j = 0; j < BASE32_CHARACTERS.length; j++) {
                if (BASE32_CHARACTERS[j].equals(base32.subSequence(i, i + 1))) {
                    String binCharacter = Integer.toBinaryString(j);
                    while (binCharacter.length() < BASE32_VALUES.length) {
                        binCharacter = "0" + binCharacter;
                    }
                    bitString += binCharacter;
                }
            }
        }

        return bitString;
    }

    /**
     * Converts a byte-array to a base32 string.
     *
     * @param bytes the byte-array to convert
     * @return the base32 value as string
     */
    public static String byteToBase32(byte[] bytes) {
        String bitString = byteToBitString(bytes);

        return bitStringToBase32(bitString);
    }

    /**
     * Converts a byte-array to a PrivaSphere base32 string.
     *
     * @param bytes the byte-array to convert
     * @return the PrivaSphere base32 value as string
     */
    public static String byteToPrivaSphereBase32(byte[] bytes) {
        String bitString = byteToBitString(bytes);

        return bitStringToBase32(bitString);
    }

    /**
     * Converts a base32 string to a byte-array.
     *
     * @param base32 the base32 string to convert
     * @return the byte-array
     */
    public static byte[] base32ToByte(String base32) {
        String bitString = base32ToBitString(base32);

        return bitStringToByte(bitString);
    }

    /**
     * Converts a byte-array to a bit-string.
     *
     * @param bytes the byte-array to convert
     * @return the bit-string as string
     */
    public static String byteToBitString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

    /**
     * Converts a bit-string to a byte-array.
     *
     * @param string the bit-string to convert
     * @return the byte-array
     */
    public static byte[] bitStringToByte(String string) {
        int sLen = string.length();
        byte[] toReturn = new byte[(sLen + Byte.SIZE - 1) / Byte.SIZE];
        char c;
        for (int i = 0; i < sLen; i++) {
            if ((c = string.charAt(i)) == '1') {
                toReturn[i / Byte.SIZE] = (byte) (toReturn[i / Byte.SIZE] | (0x80 >>> (i % Byte.SIZE)));
            } else if (c != '0') {
                throw new IllegalArgumentException();
            }
        }
        return toReturn;
    }

    private static String getBase32Character(int value) {
        return BASE32_CHARACTERS[value];
    }
}
