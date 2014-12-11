package ch.hsr.xclavis.helpers;

public class FormatTransformer {

    private static final String[] BASE32_CHARACTERS = {"2", "3", "4", "5", "6", "7",
        "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L",
        "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static final int[] BASE32_VALUES = {16, 8, 4, 2, 1};

    /**
     * Transform a bit-string in a base32 string
     *
     * @param bitString
     * @return base32 string
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

    public static String byteToBase32(byte[] bytes) {
        String bitString = byteToBitString(bytes);
        
        return bitStringToBase32(bitString);
    }
    
    public static byte[] base32ToByte(String base32) {
        String bitString = base32ToBitString(base32);
        
        return bitStringToByte(bitString);
    }

    public static String byteToBitString(byte[] bytes) {
        StringBuilder sb = new StringBuilder(bytes.length * Byte.SIZE);
        for (int i = 0; i < Byte.SIZE * bytes.length; i++) {
            sb.append((bytes[i / Byte.SIZE] << i % Byte.SIZE & 0x80) == 0 ? '0' : '1');
        }
        return sb.toString();
    }

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
