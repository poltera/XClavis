package ch.hsr.xclavis.helpers;

public class Base32 {

    private static final String[] CHARACTERS = {"2", "3", "4", "5", "6", "7",
        "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L",
        "M", "N", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private static final int[] VALUES = {16, 8, 4, 2, 1};

    /**
     * Transform a bit-string in a base32 string
     *
     * @param bitstring
     * @return base32 string
     */
    public static String getBase32(String bin) {
        String string = "";

        for (int i = 0; i < bin.length(); i++) {
            int begin = i;
            int end = i + VALUES.length;

            if (end > bin.length()) {
                end = bin.length();
            }

            String binCharacter = bin.substring(begin, end);
            int intCharacter = 0;
            for (int j = 0; j < binCharacter.length(); j++) {
                if (Integer.parseInt(binCharacter.substring(j, j + 1)) == 1) {
                    intCharacter = intCharacter + VALUES[j];
                }
            }

            i = i + VALUES.length - 1;

            string = getBase32Character(intCharacter) + string;
        }
        string = new StringBuilder(string).reverse().toString();

        return string;

    }

    public static String getBitString(String base32) {
        String bitString = "";
        for (int i = 0; i < base32.length(); i++) {
            for (int j = 0; j < CHARACTERS.length; j++) {
                if (CHARACTERS[j].equals(base32.subSequence(i, i + 1))) {
                    String binCharacter = Integer.toBinaryString(j);
                    while (binCharacter.length() < VALUES.length) {
                        binCharacter = "0" + binCharacter;
                    }
                    bitString += binCharacter;
                }
            }
        }

        return bitString;
    }

    private static String getBase32Character(int value) {
        return CHARACTERS[value];
    }
}
