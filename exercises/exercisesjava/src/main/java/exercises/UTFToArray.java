/**
 * Преобразовать массив байтов закодированных в формате UTF, в массив codePoint-ов (int[]).
 */

package exercises;

import java.io.UnsupportedEncodingException;

public class UTFToArray {
    public static void main(String[] args) {
        String string = new String("привет");

        try {
            byte[] byteArray = string.getBytes("UTF-8");

            int[] intArray = convertUTFToCodepoint(byteArray);
            for (int symb : intArray) {
                System.out.println(symb);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    public static int[] convertUTFToCodepoint(byte[] byteArray) {
        int[] intArray = new int[byteArray.length];
        int index = 0;
        int intIndex = 0;
        while (index != byteArray.length) {
            if ((byteArray[index] & 0b10000000) == 0) {
                intArray[intIndex++] = byteArray[index++];
            } else {
                if ((byteArray[index] & 0b11100000) == 0b11100000) {
                    intArray[intIndex++] = ((byteArray[index++] & 0b00001111) << 12) + ((byteArray[index++]
                                             & 0b00111111) << 6) + ((byteArray[index++] & 0b00111111));
                } else {
                    if ((byteArray[index] & 0b11000000) == 0b11000000) {
                        intArray[intIndex++] = ((byteArray[index++] & 0b00011111) << 6) + ((byteArray[index++]
                                                 & 0b00111111));
                    }
                }
            }
        }
        return intArray;
    }
}
